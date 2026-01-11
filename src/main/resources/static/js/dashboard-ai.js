document.addEventListener("DOMContentLoaded", () => {
    fetch("/api/ai/restock")
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

                li.innerText =
                    `${item.productName} | Stock: ${item.currentStock} | ` +
                    `Stockout in ${item.predictedDaysUntilStockout} days | ` +
                    `Reorder: ${item.recommendedReorderQty} | ` +
                    `Confidence: ${(item.confidence * 100).toFixed(0)}%`;

                list.appendChild(li);
            });
        })
        .catch(err => console.error("AI fetch error:", err));
});
