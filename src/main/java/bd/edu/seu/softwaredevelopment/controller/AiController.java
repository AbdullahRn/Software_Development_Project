package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dto.RestockPredictionDto;
import bd.edu.seu.softwaredevelopment.services.AiPredictionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiPredictionService aiPredictionService;

    public AiController(AiPredictionService aiPredictionService) {
        this.aiPredictionService = aiPredictionService;
    }

    @GetMapping("/restock")
    public List<RestockPredictionDto> restock() {
        return aiPredictionService.getRestockPredictions();
    }
}
