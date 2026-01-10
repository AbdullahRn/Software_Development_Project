package bd.edu.seu.softwaredevelopment.dto;

public class SalesPointDto {
    private String date;
    private double sales;
    private int quantity;

    public SalesPointDto() {}

    public SalesPointDto(String date, double sales, int quantity) {
        this.date = date;
        this.sales = sales;
        this.quantity = quantity;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getSales() { return sales; }
    public void setSales(double sales) { this.sales = sales; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
