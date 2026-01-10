package bd.edu.seu.softwaredevelopment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionTrainingRowDto {
    public String productId;
    public LocalDate saleDate;
    public Integer totalProducts;
    public BigDecimal totalPrice;

    public TransactionTrainingRowDto(String productId, LocalDate saleDate, Integer totalProducts, BigDecimal totalPrice) {
        this.productId = productId;
        this.saleDate = saleDate;
        this.totalProducts = totalProducts;
        this.totalPrice = totalPrice;
    }
}
