package bd.edu.seu.softwaredevelopment.dto;

public class DashboardForecastDto {
    private int basisMonthsUsed;

    private int estimatedNextMonthQuantity;
    private double estimatedNextMonthSales;

    private int estimatedNextYearQuantity;
    private double estimatedNextYearSales;

    public DashboardForecastDto() {}

    public DashboardForecastDto(int basisMonthsUsed,
                                int estimatedNextMonthQuantity,
                                double estimatedNextMonthSales,
                                int estimatedNextYearQuantity,
                                double estimatedNextYearSales) {
        this.basisMonthsUsed = basisMonthsUsed;
        this.estimatedNextMonthQuantity = estimatedNextMonthQuantity;
        this.estimatedNextMonthSales = estimatedNextMonthSales;
        this.estimatedNextYearQuantity = estimatedNextYearQuantity;
        this.estimatedNextYearSales = estimatedNextYearSales;
    }

    public int getBasisMonthsUsed() { return basisMonthsUsed; }
    public void setBasisMonthsUsed(int basisMonthsUsed) { this.basisMonthsUsed = basisMonthsUsed; }

    public int getEstimatedNextMonthQuantity() { return estimatedNextMonthQuantity; }
    public void setEstimatedNextMonthQuantity(int estimatedNextMonthQuantity) { this.estimatedNextMonthQuantity = estimatedNextMonthQuantity; }

    public double getEstimatedNextMonthSales() { return estimatedNextMonthSales; }
    public void setEstimatedNextMonthSales(double estimatedNextMonthSales) { this.estimatedNextMonthSales = estimatedNextMonthSales; }

    public int getEstimatedNextYearQuantity() { return estimatedNextYearQuantity; }
    public void setEstimatedNextYearQuantity(int estimatedNextYearQuantity) { this.estimatedNextYearQuantity = estimatedNextYearQuantity; }

    public double getEstimatedNextYearSales() { return estimatedNextYearSales; }
    public void setEstimatedNextYearSales(double estimatedNextYearSales) { this.estimatedNextYearSales = estimatedNextYearSales; }
}
