async function loadRestockPredictions() {
    const res = await fetch("/api/ai/restock");
    const predictions = await res.json();

    const list = document.getElementById("restockList");
    if (!list) return;

    list.innerHTML = "";

    predictions.forEach(p => {
        const li = document.createElement("li");
        li.innerHTML = `<b>${p.productName}</b> → Restock in ${p.daysUntilStockOut} days (Suggested qty: ${p.recommendedQty})`;
        list.appendChild(li);
    });
}

window.addEventListener("load", function () {
    loadRestockPredictions();
});
