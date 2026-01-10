package bd.edu.seu.softwaredevelopment.dtos;

public class PredictionResponse {
    private String productId;// [cite: 285, 514]
    private Integer predictedUnits; // Forecasted volume [cite: 294, 515]
    private Double predictedRevenue; // Forecasted sales value [cite: 294, 516]
    private String modelVersion; // Tracking which model generated this [cite: 294, 517]

    public PredictionResponse() {
    }

    // Getters and Setters [cite: 295]
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public Integer getPredictedUnits() { return predictedUnits; }
    public void setPredictedUnits(Integer predictedUnits) { this.predictedUnits = predictedUnits; }
    public Double getPredictedRevenue() { return predictedRevenue; }
    public void setPredictedRevenue(Double predictedRevenue) { this.predictedRevenue = predictedRevenue; }
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
}


