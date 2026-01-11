package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dto.RestockPredictionDto;
import bd.edu.seu.softwaredevelopment.services.AiPredictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiPredictionService aiPredictionService;

    public AiController(AiPredictionService aiPredictionService) {
        this.aiPredictionService = aiPredictionService;
    }

    @GetMapping("/restock")
    public ResponseEntity<List<RestockPredictionDto>> getRestockPredictions() {
        try {
            List<RestockPredictionDto> result = aiPredictionService.getRestockPredictions();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }
}
