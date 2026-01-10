let selectedKey = "amount";
let chart = null;

function buildChart(key) {
    selectedKey = key;

    const txData = window.transactionData || [];

    const labels = txData.map(item => item.day);
    const values = txData.map(item => item[key]);

    const canvas = document.getElementById("transactionChart");
    if (!canvas) return;

    const ctx = canvas.getContext("2d");

    if (chart) chart.destroy();

    chart = new Chart(ctx, {
        type: "line",
        data: {
            labels: labels,
            datasets: [{
                label: key.toUpperCase(),
                data: values,
                borderWidth: 2,
                fill: false
            }]
        },
        options: {
            responsive: true
        }
    });
}

function changeChartData(key) {
    buildChart(key);
}

function reloadChart() {
    buildChart(selectedKey);
}

document.addEventListener("DOMContentLoaded", () => {
    buildChart("amount");
});
