package bd.edu.seu.softwaredevelopment.dto;

public class RestockPredictionDto {
    private String productId;
    private String productName;
    private int currentStock;
    private int predictedDaysUntilStockout;
    private int recommendedReorderQty;
    private double confidence;

    public RestockPredictionDto() {}

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getCurrentStock() { return currentStock; }
    public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }

    public int getPredictedDaysUntilStockout() { return predictedDaysUntilStockout; }
    public void setPredictedDaysUntilStockout(int predictedDaysUntilStockout) { this.predictedDaysUntilStockout = predictedDaysUntilStockout; }

    public int getRecommendedReorderQty() { return recommendedReorderQty; }
    public void setRecommendedReorderQty(int recommendedReorderQty) { this.recommendedReorderQty = recommendedReorderQty; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

}
