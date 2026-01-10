package bd.edu.seu.softwaredevelopment.services;

import bd.edu.seu.softwaredevelopment.dtos.TransactionDto;
import bd.edu.seu.softwaredevelopment.dtos.TransactionRequest;
import bd.edu.seu.softwaredevelopment.interfaces.TransactionServiceInterface;
import bd.edu.seu.softwaredevelopment.models.Product;
import bd.edu.seu.softwaredevelopment.models.Transaction;
import bd.edu.seu.softwaredevelopment.repositories.ProductRepository;
import bd.edu.seu.softwaredevelopment.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService implements TransactionServiceInterface {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public TransactionDto sell(TransactionRequest req) {
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < req.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // 1. Update Inventory Stock Level [cite: 532, 540]
        product.setStockQuantity(product.getStockQuantity() - req.getQuantity());
        productRepository.save(product);

        // 2. Log Sale Entity with ML features [cite: 658, 705-708]
        Transaction sale = new Transaction();
        sale.setProductId(req.getProductId());
        sale.setUserId(req.getUserId());
        sale.setTransactionType("SELL");
        sale.setTotalProducts(req.getQuantity()); // actualUnits [cite: 32]
        sale.setTotalPrice(product.getPrice().multiply(new BigDecimal(req.getQuantity()))); // actualRevenue [cite: 32]

        // Capturing ML Features [cite: 181-182]
        sale.setDiscount(req.getDiscount());
        sale.setPromotion(req.getPromotion());
        sale.setSaleDate(LocalDate.now()); // For time-series x-axis [cite: 34]

        Transaction saved = transactionRepository.save(sale);
        return mapToDto(saved);
    }
    @Override
    public TransactionDto purchase(TransactionRequest req) {
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStockQuantity(product.getStockQuantity() + req.getQuantity());
        productRepository.save(product);

        Transaction purchase = new Transaction();
        purchase.setProductId(req.getProductId());
        purchase.setUserId(req.getUserId());
        purchase.setSupplierId(req.getSupplierId());
        purchase.setTransactionType("PURCHASE");
        purchase.setTotalProducts(req.getQuantity());
        purchase.setCreatedAt(LocalDateTime.now());
        purchase.setStatus("COMPLETED");

        return mapToDto(transactionRepository.save(purchase));
    }

    @Override
    public List<TransactionDto> getAllTransactions() {
        return null;
    }

    @Override
    public TransactionDto getTransactionById(String id) {
        return null;
    }

    @Override
    public List<TransactionDto> getSalesHistoryForProduct(String productId) {
        return null;
    }

    @Override
    public List<TransactionDto> getTransactionsByMonthAndYear(int month, int year) {
        return null;
    }

    @Override
    public TransactionDto updateTransactionStatus(String transactionId, String status) {
        return null;
    }

    private TransactionDto mapToDto(Transaction t) { /* Mapping Logic */ return new TransactionDto(); }
}
