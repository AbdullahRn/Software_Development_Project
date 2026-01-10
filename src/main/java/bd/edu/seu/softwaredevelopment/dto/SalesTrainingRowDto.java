package bd.edu.seu.softwaredevelopment.dto;

import java.time.LocalDate;

public class SalesTrainingRowDto {
    public Long productId;
    public LocalDate date;
    public int quantitySold;
    public double totalPrice;

    public SalesTrainingRowDto(Long productId, LocalDate date, int quantitySold, double totalPrice) {
        this.productId = productId;
        this.date = date;
        this.quantitySold = quantitySold;
        this.totalPrice = totalPrice;
    }
}
