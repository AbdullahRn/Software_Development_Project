from fastapi import FastAPI
import pandas as pd
import requests

app = FastAPI()

SPRING_BASE = "http://localhost:8080"

def fetch_transactions():
    url = f"{SPRING_BASE}/api/ml/training-data/transactions"
    data = requests.get(url).json()
    return pd.DataFrame(data)

@app.get("/predict/restock")
def predict_restock():
    df = fetch_transactions()

    if df.empty:
        return []

    df["saleDate"] = pd.to_datetime(df["saleDate"])
    df = df[df["saleDate"] >= df["saleDate"].max() - pd.Timedelta(days=30)]

    stats = df.groupby("productId")["totalProducts"].mean().reset_index()
    stats.columns = ["productId", "avgDailySales"]

    stats["currentStock"] = 50
    stats["daysUntilStockOut"] = (stats["currentStock"] / stats["avgDailySales"].replace(0, 1)).round()
    stats["recommendedQty"] = (stats["avgDailySales"] * 15).round().astype(int)

    results = []
    for _, row in stats.iterrows():
        results.append({
            "productId": row["productId"],
            "productName": f"Product {row['productId']}",
            "daysUntilStockOut": int(row["daysUntilStockOut"]),
            "recommendedQty": int(row["recommendedQty"])
        })

    return results
