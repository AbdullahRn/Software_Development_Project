document.addEventListener("DOMContentLoaded", () => {
    fetch("http://127.0.0.1:8000/predict/restock")
        .then(res => res.json())
        .then(data => {
            const list = document.getElementById("restockList");
            if (!list) return;

            list.innerHTML = "";

            if (!Array.isArray(data) || data.length === 0) {
                list.innerHTML = "<li>No restock suggestions available</li>";
                return;
            }

            data.forEach(item => {
                const li = document.createElement("li");
                li.innerText = `${item.productName} → Restock ${item.recommendedQty} (Stock out in ${item.daysUntilStockOut} days)`;
                list.appendChild(li);
            });
        })
        .catch(err => {
            console.error("AI fetch error:", err);
        });
});
