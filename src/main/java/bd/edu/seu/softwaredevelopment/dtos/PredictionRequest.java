package bd.edu.seu.softwaredevelopment.dtos;

import java.time.LocalDate;
import java.util.List;

public class PredictionRequest {
    private String ownerId; // Seller or Supplier ID [cite: 272]
    private String productId; //[cite: 273]
    private LocalDate predictionDate; // Target date for forecast [cite: 274]

    // ML Features [cite: 275]
    private List<Integer> last7DaysUnits; // Historical trend [cite: 276, 512]
    private Double unitPrice;// [cite: 277, 509]
    private Double discount; //[cite: 278, 510]
    private Boolean promotion; //[cite: 278, 511]

    public PredictionRequest() {}

    // Getters and Setters [cite: 279]
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public LocalDate getPredictionDate() { return predictionDate; }
    public void setPredictionDate(LocalDate predictionDate) { this.predictionDate = predictionDate; }
    public List<Integer> getLast7DaysUnits() { return last7DaysUnits; }
    public void setLast7DaysUnits(List<Integer> last7DaysUnits) { this.last7DaysUnits = last7DaysUnits; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }
    public Boolean getPromotion() { return promotion; }
    public void setPromotion(Boolean promotion) { this.promotion = promotion; }
}

