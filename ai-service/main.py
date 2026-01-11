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
    for _, row in merged.iterrows():
        avg_sales = float(row.get("avgDailySales", 0))
        stock_val = row.get("stockQuantity", 0)

        try:
            stock = int(stock_val)
        except Exception:
            stock = 0

        pid = str(row.get("productId"))
        pname = str(row.get("name", "Unknown"))

        # --- compute restock values ---
        if avg_sales <= 0:
            days_left = stock
            recommended_qty = 0
        else:
            days_left = round(stock / avg_sales)
            recommended_qty = round(avg_sales * 15)

        # ✅ add confidence here
        confidence = 0.60
        if model is not None:
            confidence = 0.85

        # ✅ return fields exactly matching RestockPredictionDto
        results.append({
            "productId": pid,
            "productName": pname,
            "currentStock": stock,
            "predictedDaysUntilStockout": int(days_left),
            "recommendedReorderQty": int(recommended_qty),
            "confidence": float(confidence)
        })

    return results
