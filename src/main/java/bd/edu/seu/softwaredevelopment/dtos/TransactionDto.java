package bd.edu.seu.softwaredevelopment.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionDto {
    private String id;
    private Integer totalProducts; // Represents actualUnits for graphs [cite: 32]
    private BigDecimal totalPrice; // Represents actualRevenue for graphs [cite: 32]
    private String transactionType; // "SELL" or "PURCHASE"
    private String status;
    private String description;
    private String note;

    // ML Features added per Project Plan [cite: 132, 181-182]
    private Double discount;
    private Boolean promotion;
    private LocalDate saleDate; // X-axis for graphs [cite: 34, 179]

    private LocalDateTime createdAt;
    private ProductDto product;
    private UserDto user; // Represents the Seller [cite: 176]
    private UserDto supplier; // Now links to a User with role SUPPLIER [cite: 4, 129]

    public TransactionDto() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Integer getTotalProducts() { return totalProducts; }
    public void setTotalProducts(Integer totalProducts) { this.totalProducts = totalProducts; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }
    public Boolean getPromotion() { return promotion; }
    public void setPromotion(Boolean promotion) { this.promotion = promotion; }
    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public ProductDto getProduct() { return product; }
    public void setProduct(ProductDto product) { this.product = product; }
    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }
    public UserDto getSupplier() { return supplier; }
    public void setSupplier(UserDto supplier) { this.supplier = supplier; }
}


