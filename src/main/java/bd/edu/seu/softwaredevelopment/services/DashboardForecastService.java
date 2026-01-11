package bd.edu.seu.softwaredevelopment.services;

import bd.edu.seu.softwaredevelopment.dto.DashboardForecastDto;
import bd.edu.seu.softwaredevelopment.models.Transaction;
import bd.edu.seu.softwaredevelopment.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

@Service
public class DashboardForecastService {

    private final TransactionRepository transactionRepository;

    public DashboardForecastService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    private static class MonthlyAgg {
        int qty = 0;
        BigDecimal sales = BigDecimal.ZERO;

        void add(Integer q, BigDecimal s) {
            qty += (q == null ? 0 : q);
            sales = sales.add(s == null ? BigDecimal.ZERO : s);
        }
    }

    public DashboardForecastDto estimateForSeller(String userId) {
        List<Transaction> tx = transactionRepository.findByUserId(userId);
        return estimate(tx);
    }

    public DashboardForecastDto estimateForSupplier(String supplierId) {
        List<Transaction> tx = transactionRepository.findBySupplierId(supplierId);
        return estimate(tx);
    }

    private DashboardForecastDto estimate(List<Transaction> tx) {
        if (tx == null) tx = Collections.emptyList();

        Map<YearMonth, MonthlyAgg> byMonth = new HashMap<>();

        for (Transaction t : tx) {
            if (t == null || t.getSaleDate() == null) continue;
            YearMonth ym = YearMonth.from(t.getSaleDate());
            byMonth.computeIfAbsent(ym, k -> new MonthlyAgg())
                    .add(t.getTotalProducts(), t.getTotalPrice());
        }

        if (byMonth.isEmpty()) {
            return new DashboardForecastDto(0, 0, 0.0, 0, 0.0);
        }

        List<YearMonth> months = new ArrayList<>(byMonth.keySet());
        months.sort(Comparator.naturalOrder());

        int K = 3; // basis months
        List<YearMonth> basis = months.size() <= K ? months : months.subList(months.size() - K, months.size());

        int basisUsed = basis.size();

        double totalQty = 0;
        double totalSales = 0;

        for (YearMonth ym : basis) {
            MonthlyAgg agg = byMonth.get(ym);
            totalQty += agg.qty;
            totalSales += agg.sales.doubleValue();
        }

        double avgQty = totalQty / basisUsed;
        double avgSales = totalSales / basisUsed;

        int nextMonthQty = (int) Math.round(avgQty);
        double nextMonthSales = avgSales;

        int nextYearQty = (int) Math.round(avgQty * 12.0);
        double nextYearSales = avgSales * 12.0;

        return new DashboardForecastDto(
                basisUsed,
                nextMonthQty, nextMonthSales,
                nextYearQty, nextYearSales
        );
    }
}
