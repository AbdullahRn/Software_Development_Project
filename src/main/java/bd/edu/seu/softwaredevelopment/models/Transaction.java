package bd.edu.seu.softwaredevelopment.models;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transaction {
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Id
    private String id;
    private String productId;
    private String userId;      // Added to fix the "red" error
    private String supplierId;
    private String transactionType;
    private Integer totalProducts;
    private BigDecimal totalPrice;
    private Double discount;    // Added for ML [cite: 9]
    private Boolean promotion;  // Added for ML [cite: 9]
    private LocalDate saleDate; // Required for graph x-axis [cite: 8]
    private String status;
    private LocalDateTime createdAt;
    public Transaction() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String type) { this.transactionType = type; }
    public Integer getTotalProducts() { return totalProducts; }
    public void setTotalProducts(Integer totalProducts) { this.totalProducts = totalProducts; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }
    public Boolean getPromotion() { return promotion; }
    public void setPromotion(Boolean promotion) { this.promotion = promotion; }
    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
}


