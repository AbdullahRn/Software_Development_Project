//package bd.edu.seu.softwaredevelopment.controller;
//
//import bd.edu.seu.softwaredevelopment.models.Transaction;
//import bd.edu.seu.softwaredevelopment.repositories.TransactionRepository;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.*;
//
//@RestController
//@RequestMapping("/api/analytics")
//public class AnalyticsController {
//
//    private final TransactionRepository transactionRepository;
//
//    public AnalyticsController(TransactionRepository transactionRepository) {
//        this.transactionRepository = transactionRepository;
//    }
//
//    @GetMapping("/daily-sales")
//    public Map<String, Object> dailySales(@RequestParam(defaultValue = "30") int days) {
//
//        LocalDate from = LocalDate.now().minusDays(days);
//
//        List<Transaction> txs = transactionRepository.findAll();
//
//        // Group by saleDate, sum totalPrice
//        Map<LocalDate, BigDecimal> dailySum = new TreeMap<>();
//        for (Transaction t : txs) {
//            if (t.getSaleDate() != null && !t.getSaleDate().isBefore(from)) {
//                dailySum.putIfAbsent(t.getSaleDate(), BigDecimal.ZERO);
//                dailySum.put(t.getSaleDate(), dailySum.get(t.getSaleDate()).add(t.getTotalPrice()));
//            }
//        }
//
//        List<String> labels = new ArrayList<>();
//        List<Double> values = new ArrayList<>();
//
//        for (Map.Entry<LocalDate, BigDecimal> entry : dailySum.entrySet()) {
//            labels.add(entry.getKey().toString());
//            values.add(entry.getValue().doubleValue());
//        }
//
//        Map<String, Object> res = new HashMap<>();
//        res.put("labels", labels);
//        res.put("values", values);
//        return res;
//    }
//}
