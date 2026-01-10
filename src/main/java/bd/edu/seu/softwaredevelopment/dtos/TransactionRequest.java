package bd.edu.seu.softwaredevelopment.dtos;

public class TransactionRequest {
    private String productId;
    private String userId;     // Required field (The Seller ID)
    private Integer quantity;
    private String supplierId; // The Supplier ID for Purchases
    private Double discount;   // Required for ML features [cite: 9]
    private Boolean promotion; // Required for ML features [cite: 9]
    private String description;
    private String note;

    public TransactionRequest() {}

    public TransactionRequest(String productId, Integer quantity, String supplierId, String description, String note) {
        this.productId = productId;
        this.quantity = quantity;
        this.supplierId = supplierId;
        this.description = description;
        this.note = note;
    }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }

    public Boolean getPromotion() { return promotion; }
    public void setPromotion(Boolean promotion) { this.promotion = promotion; }
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}


