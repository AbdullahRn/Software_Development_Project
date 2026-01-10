package bd.edu.seu.softwaredevelopment.models;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Prediction {
    @Id
    private String id;
    private String productId;
    private String ownerId; // ID of the Seller or Supplier [cite: 212]
    private LocalDate predictionDate; // X-axis for graphs [cite: 34, 493]

    // ML Outputs [cite: 33, 494, 495]
    private Integer predictedUnits;
    private Double predictedRevenue;

    // For graph comparison [cite: 32, 233, 524]
    private Integer actualUnits;
    private Double actualRevenue;

    private String modelVersion; // For tracking ML accuracy [cite: 244, 517]
    private LocalDateTime createdAt = LocalDateTime.now();

    public Prediction() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public LocalDate getPredictionDate() { return predictionDate; }
    public void setPredictionDate(LocalDate date) { this.predictionDate = date; }
    public Integer getPredictedUnits() { return predictedUnits; }
    public void setPredictedUnits(Integer units) { this.predictedUnits = units; }
    public Double getPredictedRevenue() { return predictedRevenue; }
    public void setPredictedRevenue(Double revenue) { this.predictedRevenue = revenue; }
    public Integer getActualUnits() { return actualUnits; }
    public void setActualUnits(Integer units) { this.actualUnits = units; }
    public Double getActualRevenue() { return actualRevenue; }
    public void setActualRevenue(Double revenue) { this.actualRevenue = revenue; }
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String version) { this.modelVersion = version; }
}

