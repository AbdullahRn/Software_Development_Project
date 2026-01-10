package bd.edu.seu.softwaredevelopment.dto;

import java.util.List;

public class AiForecastResponseDto {
    private List<SalesPointDto> history;
    private List<SalesPointDto> forecast;

    public AiForecastResponseDto() {}

    public List<SalesPointDto> getHistory() { return history; }
    public void setHistory(List<SalesPointDto> history) { this.history = history; }

    public List<SalesPointDto> getForecast() { return forecast; }
    public void setForecast(List<SalesPointDto> forecast) { this.forecast = forecast; }
}