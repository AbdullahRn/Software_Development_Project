package bd.edu.seu.softwaredevelopment.services;

import bd.edu.seu.softwaredevelopment.dto.RestockPredictionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AiPredictionService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.service.base-url}")
    private String aiBaseUrl;

    public List<RestockPredictionDto> getRestockPredictions() {
        String url = aiBaseUrl + "/predict/restock";
        RestockPredictionDto[] response = restTemplate.getForObject(url, RestockPredictionDto[].class);
        return Arrays.asList(response);
    }
}
