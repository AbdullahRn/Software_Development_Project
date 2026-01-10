package bd.edu.seu.softwaredevelopment.services;

import bd.edu.seu.softwaredevelopment.dtos.PredictionRequest;
import bd.edu.seu.softwaredevelopment.dtos.PredictionResponse;
import bd.edu.seu.softwaredevelopment.interfaces.PredictionServiceInterface;
import bd.edu.seu.softwaredevelopment.models.Prediction;
import bd.edu.seu.softwaredevelopment.models.Product;
import bd.edu.seu.softwaredevelopment.models.Transaction;
import bd.edu.seu.softwaredevelopment.repositories.PredictionRepository;
import bd.edu.seu.softwaredevelopment.repositories.ProductRepository;
import bd.edu.seu.softwaredevelopment.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PredictionService implements PredictionServiceInterface {

    @Autowired
    private WebClient mlWebClient; // Configured to http://localhost:8000 [cite: 846]
    @Autowired
    private PredictionRepository predictionRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Prediction generatePrediction(String ownerId, String productId, LocalDate predictionDate) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Build feature data: last 7 days of sales [cite: 802, 1038]
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        List<Transaction> recentSales = transactionRepository
                .findByProductIdAndSaleDateBetweenOrderBySaleDateAsc(productId, sevenDaysAgo, LocalDate.now());

        List<Integer> last7DaysUnits = recentSales.stream()
                .map(Transaction::getTotalProducts)
                .collect(Collectors.toList());

        // Prepare JSON Request for Python [cite: 1029]
        PredictionRequest request = new PredictionRequest();
        request.setOwnerId(ownerId);
        request.setProductId(productId);
        request.setPredictionDate(predictionDate);
        request.setUnitPrice(product.getPrice().doubleValue());
        request.setLast7DaysUnits(last7DaysUnits);
        request.setDiscount(0.0); // Default or fetch from latest promo
        request.setPromotion(false);

        // Call Python ML API [cite: 940, 957]
        PredictionResponse response = mlWebClient.post()
                .uri("/predict")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PredictionResponse.class)
                .block();

        // Save result as Prediction [cite: 959, 970]
        Prediction prediction = new Prediction();
        prediction.setProductId(productId);
        prediction.setOwnerId(ownerId);
        prediction.setPredictionDate(predictionDate);
        prediction.setPredictedUnits(response.getPredictedUnits());
        prediction.setPredictedRevenue(response.getPredictedRevenue());
        prediction.setModelVersion(response.getModelVersion());

        return predictionRepository.save(prediction);
    }

    @Override
    public List<Prediction> getPredictionHistory(String ownerId, String productId) {
        return null;
    }

    @Override
    public List<String> getProactiveSuggestions(String userId) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getFormattedGraphData(String productId) {
        return null;
    }
}

