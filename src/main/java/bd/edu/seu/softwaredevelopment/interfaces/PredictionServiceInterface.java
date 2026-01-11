package bd.edu.seu.softwaredevelopment.interfaces;

import bd.edu.seu.softwaredevelopment.models.Prediction;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PredictionServiceInterface {

    Prediction generatePrediction(String ownerId, String productId, LocalDate predictionDate);

    List<Prediction> getPredictionHistory(String ownerId, String productId);

    List<String> getProactiveSuggestions(String userId);

    // ✅ Change signature: graph is based on user + role
    List<Map<String, Object>> getFormattedGraphData(String userId, boolean isSupplier);
}
