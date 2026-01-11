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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PredictionService implements PredictionServiceInterface {

    @Autowired
    private WebClient mlWebClient; // Configured to http://localhost:8000

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

        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        List<Transaction> recentSales = transactionRepository
                .findByProductIdAndSaleDateBetweenOrderBySaleDateAsc(productId, sevenDaysAgo, LocalDate.now());

        List<Integer> last7DaysUnits = recentSales.stream()
                .map(Transaction::getTotalProducts)
                .collect(Collectors.toList());

        PredictionRequest request = new PredictionRequest();
        request.setOwnerId(ownerId);
        request.setProductId(productId);
        request.setPredictionDate(predictionDate);
        request.setUnitPrice(product.getPrice().doubleValue());
        request.setLast7DaysUnits(last7DaysUnits);
        request.setDiscount(0.0);
        request.setPromotion(false);

        PredictionResponse response = mlWebClient.post()
                .uri("/predict")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PredictionResponse.class)
                .block();

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

    // ✅ FIXED: Use userId/supplierId instead of productId
    @Override
    public List<Map<String, Object>> getFormattedGraphData(String userId, boolean isSupplier) {

        List<Transaction> transactions;

        // If userId empty => show all (safe fallback)
        if (userId == null || userId.isBlank()) {
            transactions = transactionRepository.findAll();
        } else {
            // Seller = transactions created by this user
            // Supplier = transactions assigned to this supplier
            transactions = isSupplier
                    ? transactionRepository.findBySupplierId(userId)
                    : transactionRepository.findByUserId(userId);
        }

        if (transactions == null || transactions.isEmpty()) {
            return List.of();
        }

        // Group by saleDate and calculate count, quantity, amount
        return transactions.stream()
                .filter(t -> t.getSaleDate() != null) // avoid null dates breaking grouping
                .collect(Collectors.groupingBy(Transaction::getSaleDate))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {

                    LocalDate date = entry.getKey();
                    List<Transaction> txList = entry.getValue();

                    int count = txList.size();
                    int quantity = txList.stream().mapToInt(t -> t.getTotalProducts() == null ? 0 : t.getTotalProducts()).sum();

                    double amount = txList.stream()
                            .map(Transaction::getTotalPrice)
                            .filter(Objects::nonNull)
                            .mapToDouble(BigDecimal::doubleValue)
                            .sum();

                    Map<String, Object> data = new HashMap<>();
                    data.put("day", date.toString());
                    data.put("count", count);
                    data.put("quantity", quantity);
                    data.put("amount", amount);

                    return data;
                })
                .collect(Collectors.toList());
    }
}
