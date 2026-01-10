from fastapi import FastAPI
import pandas as pd
import requests
import joblib
import os

app = FastAPI()

SPRING_BASE = "http://localhost:8080"
MODEL_PATH = "models/sales_model.pkl"


def safe_get_json(url):
    try:
        r = requests.get(url, timeout=10)
        if r.status_code != 200:
            return []
        if not r.text.strip():
            return []
        return r.json()
    except Exception:
        return []


def fetch_transactions():
    url = f"{SPRING_BASE}/api/ml/training-data/transactions"
    data = safe_get_json(url)
    if not isinstance(data, list) or len(data) == 0:
        return pd.DataFrame()
    return pd.DataFrame(data)


def fetch_products():
    url = f"{SPRING_BASE}/api/ml/training-data/products"
    data = safe_get_json(url)
    if not isinstance(data, list) or len(data) == 0:
        return pd.DataFrame()
    return pd.DataFrame(data)


def load_model():
    if os.path.exists(MODEL_PATH):
        return joblib.load(MODEL_PATH)
    return None


@app.get("/health")
def health():
    return {"status": "ok"}


@app.get("/predict/restock")
def predict_restock():
    tx = fetch_transactions()
    products = fetch_products()

    if tx.empty or products.empty:
        return []

    required_tx_cols = {"saleDate", "productId", "totalProducts"}
    required_prod_cols = {"id", "name", "stockQuantity"}

    if not required_tx_cols.issubset(tx.columns):
        return []

    if not required_prod_cols.issubset(products.columns):
        return []

    # Format
    tx["saleDate"] = pd.to_datetime(tx["saleDate"], errors="coerce")
    tx = tx.dropna(subset=["saleDate"])
    tx["productId"] = tx["productId"].astype(str)

    products["id"] = products["id"].astype(str)

    if tx.empty:
        return []

    model = load_model()

    last_date = tx["saleDate"].max()

    # Use only last 60 days for better stats
    tx = tx[tx["saleDate"] >= last_date - pd.Timedelta(days=60)]
    if tx.empty:
        return []

    # Aggregate daily sales per product
    daily = tx.groupby(["productId", tx["saleDate"].dt.date])["totalProducts"].sum().reset_index()
    daily.columns = ["productId", "saleDate", "qty"]
    daily["saleDate"] = pd.to_datetime(daily["saleDate"])

    # Generate lag features
    daily = daily.sort_values(["productId", "saleDate"])
    daily["dayOfWeek"] = daily["saleDate"].dt.dayofweek
    daily["dayOfMonth"] = daily["saleDate"].dt.day
    daily["month"] = daily["saleDate"].dt.month

    daily["lag1"] = daily.groupby("productId")["qty"].shift(1)
    daily["lag7_avg"] = daily.groupby("productId")["qty"].rolling(7).mean().reset_index(level=0, drop=True)
    daily["lag30_avg"] = daily.groupby("productId")["qty"].rolling(30).mean().reset_index(level=0, drop=True)

    daily = daily.dropna()

    results = []

    for _, prod in products.iterrows():
        pid = str(prod["id"])
        pname = str(prod.get("name", "Unknown"))

        try:
            stock = int(prod.get("stockQuantity", 0))
        except:
            stock = 0

        product_daily = daily[daily["productId"] == pid]

        # If no history → skip
        if product_daily.empty:
            continue

        # Fallback heuristic if no model
        if model is None:
            avg_sales = float(product_daily["qty"].mean())
            if avg_sales <= 0:
                days_left = stock
                rec_qty = 0
            else:
                days_left = round(stock / avg_sales)
                rec_qty = round(avg_sales * 15)

            results.append({
                "productId": pid,
                "productName": pname,
                "daysUntilStockOut": int(days_left),
                "recommendedQty": int(rec_qty),
                "method": "heuristic"
            })
            continue

        # ML prediction: predict sales for next 15 days
        last_row = product_daily.iloc[-1]

        lag1 = last_row["qty"]
        lag7 = last_row["lag7_avg"]
        lag30 = last_row["lag30_avg"]

        predicted_15_day_demand = 0

        for i in range(1, 16):
            future_date = last_date + pd.Timedelta(days=i)

            X_future = pd.DataFrame([{
                "dayOfWeek": future_date.dayofweek,
                "dayOfMonth": future_date.day,
                "month": future_date.month,
                "lag1": lag1,
                "lag7_avg": lag7,
                "lag30_avg": lag30
            }])

            pred = float(model.predict(X_future)[0])
            pred = max(pred, 0)

            predicted_15_day_demand += pred

            # update lag rolling approximation
            lag1 = pred
            lag7 = (lag7 * 6 + pred) / 7
            lag30 = (lag30 * 29 + pred) / 30

        avg_daily_pred = predicted_15_day_demand / 15
        days_left = int(stock / avg_daily_pred) if avg_daily_pred > 0 else stock
        rec_qty = int(predicted_15_day_demand)

        results.append({
            "productId": pid,
            "productName": pname,
            "daysUntilStockOut": int(days_left),
            "recommendedQty": int(rec_qty),
            "method": "ml_model"
        })

    return results
