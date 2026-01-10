package bd.edu.seu.softwaredevelopment.controller.api;

import bd.edu.seu.softwaredevelopment.dto.SalesPointDto;
import bd.edu.seu.softwaredevelopment.models.Transaction;
import bd.edu.seu.softwaredevelopment.repositories.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsApiController {

    private final TransactionRepository transactionRepository;

    public AnalyticsApiController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/sales/daily")
    public List<SalesPointDto> getDailySales(@RequestParam(defaultValue = "90") int days) {

        LocalDate startDate = LocalDate.now().minusDays(days);


        List<Transaction> transactions = transactionRepository.findAll();

        Map<LocalDate, List<Transaction>> byDay = transactions.stream()
                .filter(t -> t.getTransactionType() != null)
                .filter(t -> t.getTransactionType().equalsIgnoreCase("SELL"))
                .filter(t -> (t.getSaleDate() != null && !t.getSaleDate().isBefore(startDate))
                        || (t.getSaleDate() == null && t.getCreatedAt() != null && !t.getCreatedAt().toLocalDate().isBefore(startDate)))
                .collect(Collectors.groupingBy(t -> {
                    if (t.getSaleDate() != null) return t.getSaleDate();
                    return t.getCreatedAt().toLocalDate();
                }));

        List<SalesPointDto> result = new ArrayList<>();

        for (LocalDate date : byDay.keySet()) {

            BigDecimal totalSales = byDay.get(date).stream()
                    .map(Transaction::getTotalPrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int totalQty = byDay.get(date).stream()
                    .map(Transaction::getTotalProducts)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();

            result.add(new SalesPointDto(date.toString(), totalSales.doubleValue(), totalQty));
        }

        result.sort(Comparator.comparing(SalesPointDto::getDate));
        return result;
    }
}
