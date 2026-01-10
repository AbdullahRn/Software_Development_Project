import pandas as pd
import requests
from sklearn.ensemble import RandomForestRegressor
import joblib
import os

SPRING_BASE = "http://localhost:8080"
MODEL_DIR = "models"
MODEL_FILE = "sales_model.pkl"

url = f"{SPRING_BASE}/api/ml/training-data/transactions"
r = requests.get(url, timeout=10)

if r.status_code != 200 or not r.text.strip():
    raise Exception("Failed to fetch transaction data from Spring Boot")

data = r.json()
df = pd.DataFrame(data)

if df.empty:
    raise Exception("No transactions found")

df["saleDate"] = pd.to_datetime(df["saleDate"], errors="coerce")
df = df.dropna(subset=["saleDate"])

df["productId"] = df["productId"].astype(str)
df = df.sort_values(["productId", "saleDate"])

daily = df.groupby(["productId", df["saleDate"].dt.date])["totalProducts"].sum().reset_index()
daily.columns = ["productId", "saleDate", "qty"]
daily["saleDate"] = pd.to_datetime(daily["saleDate"])

daily = daily.sort_values(["productId", "saleDate"])

daily["dayOfWeek"] = daily["saleDate"].dt.dayofweek
daily["dayOfMonth"] = daily["saleDate"].dt.day
daily["month"] = daily["saleDate"].dt.month

daily["lag1"] = daily.groupby("productId")["qty"].shift(1)
daily["lag7_avg"] = daily.groupby("productId")["qty"].rolling(7).mean().reset_index(level=0, drop=True)
daily["lag30_avg"] = daily.groupby("productId")["qty"].rolling(30).mean().reset_index(level=0, drop=True)

daily = daily.dropna()

if daily.empty:
    raise Exception("Not enough transaction history to build lag features. Seed more transactions first.")

X = daily[["dayOfWeek", "dayOfMonth", "month", "lag1", "lag7_avg", "lag30_avg"]]
y = daily["qty"]

model = RandomForestRegressor(n_estimators=200, random_state=42)
model.fit(X, y)

os.makedirs(MODEL_DIR, exist_ok=True)
joblib.dump(model, os.path.join(MODEL_DIR, MODEL_FILE))

print("✅ Model trained and saved to:", os.path.join(MODEL_DIR, MODEL_FILE))
