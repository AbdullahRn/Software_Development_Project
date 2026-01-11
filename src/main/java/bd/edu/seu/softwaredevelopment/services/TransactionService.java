package bd.edu.seu.softwaredevelopment.services;

import bd.edu.seu.softwaredevelopment.dtos.TransactionDto;
import bd.edu.seu.softwaredevelopment.dtos.TransactionRequest;
import bd.edu.seu.softwaredevelopment.dto.TransactionDetailsDto;
import bd.edu.seu.softwaredevelopment.interfaces.TransactionServiceInterface;
import bd.edu.seu.softwaredevelopment.models.Product;
import bd.edu.seu.softwaredevelopment.models.Supplier;
import bd.edu.seu.softwaredevelopment.models.Transaction;
import bd.edu.seu.softwaredevelopment.models.User;
import bd.edu.seu.softwaredevelopment.repositories.ProductRepository;
import bd.edu.seu.softwaredevelopment.repositories.SupplierRepository;
import bd.edu.seu.softwaredevelopment.repositories.TransactionRepository;
import bd.edu.seu.softwaredevelopment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService implements TransactionServiceInterface {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    // ==========================================================
    // ✅ SELL TRANSACTION (updates stock and logs transaction)
    // ==========================================================
    @Override
    public TransactionDto sell(TransactionRequest req) {

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < req.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // ✅ Update Inventory Stock
        product.setStockQuantity(product.getStockQuantity() - req.getQuantity());
        productRepository.save(product);

        // ✅ Create Sale Transaction
        Transaction sale = new Transaction();
        sale.setProductId(req.getProductId());
        sale.setUserId(req.getUserId());

        // NOTE: Your code uses "SELL" for sale
        sale.setTransactionType("SELL");

        sale.setTotalProducts(req.getQuantity());
        sale.setTotalPrice(product.getPrice().multiply(new BigDecimal(req.getQuantity())));

        // ✅ ML Fields
        sale.setDiscount(req.getDiscount());
        sale.setPromotion(req.getPromotion());
        sale.setSaleDate(LocalDate.now());

        sale.setCreatedAt(LocalDateTime.now());
        sale.setStatus("COMPLETED");

        Transaction saved = transactionRepository.save(sale);
        return mapToDto(saved);
    }

    // ==========================================================
    // ✅ PURCHASE TRANSACTION (adds stock and logs transaction)
    // ==========================================================
    @Override
    public TransactionDto purchase(TransactionRequest req) {

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ Update Inventory Stock
        product.setStockQuantity(product.getStockQuantity() + req.getQuantity());
        productRepository.save(product);

        // ✅ Create Purchase Transaction
        Transaction purchase = new Transaction();
        purchase.setProductId(req.getProductId());
        purchase.setUserId(req.getUserId());
        purchase.setSupplierId(req.getSupplierId());

        purchase.setTransactionType("PURCHASE");
        purchase.setTotalProducts(req.getQuantity());

        // totalPrice might be optional in your purchase flow;
        // if required, set it here
        if (product.getPrice() != null) {
            purchase.setTotalPrice(product.getPrice().multiply(new BigDecimal(req.getQuantity())));
        } else {
            purchase.setTotalPrice(BigDecimal.ZERO);
        }

        // ✅ ML Fields (keep consistent)
        purchase.setDiscount(req.getDiscount());
        purchase.setPromotion(req.getPromotion());
        purchase.setSaleDate(LocalDate.now());

        purchase.setCreatedAt(LocalDateTime.now());
        purchase.setStatus("COMPLETED");

        Transaction saved = transactionRepository.save(purchase);
        return mapToDto(saved);
    }

    // ==========================================================
    // ✅ GET ALL TRANSACTIONS (Newest first)
    // ==========================================================
    @Override
    public List<TransactionDto> getAllTransactions() {
        List<Transaction> list = transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // ==========================================================
    // ✅ GET TRANSACTION BY ID
    // ==========================================================
    @Override
    public TransactionDto getTransactionById(String id) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return mapToDto(t);
    }

    // ==========================================================
    // ✅ SALES HISTORY FOR PRODUCT (SELL only)
    // ==========================================================
    @Override
    public List<TransactionDto> getSalesHistoryForProduct(String productId) {

        List<Transaction> list = transactionRepository.findAll(Sort.by(Sort.Direction.ASC, "saleDate"));
        if (list == null || list.isEmpty()) return Collections.emptyList();

        return list.stream()
                .filter(t -> productId.equals(t.getProductId()))
                .filter(t -> "SELL".equalsIgnoreCase(t.getTransactionType()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // ✅ TRANSACTIONS FILTER BY MONTH/YEAR (for chart/reports)
    // ==========================================================
    @Override
    public List<TransactionDto> getTransactionsByMonthAndYear(int month, int year) {

        List<Transaction> list = transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "saleDate"));
        if (list == null || list.isEmpty()) return Collections.emptyList();

        return list.stream()
                .filter(t -> t.getSaleDate() != null)
                .filter(t -> t.getSaleDate().getMonthValue() == month && t.getSaleDate().getYear() == year)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // ✅ UPDATE STATUS (used in API or controller)
    // ==========================================================
    @Override
    public TransactionDto updateTransactionStatus(String transactionId, String status) {

        Transaction t = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        t.setStatus(status);
        Transaction saved = transactionRepository.save(t);
        return mapToDto(saved);
    }

    // ==========================================================
    // ✅ Helper for Controller
    // ==========================================================
    public void updateStatus(String transactionId, String status) {
        Transaction t = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        t.setStatus(status);
        transactionRepository.save(t);
    }

    // ==========================================================
    // ✅ REQUIRED: Transaction Details (fixes your controller error)
    // Fetches Product, User, Supplier objects for details page
    // ==========================================================
    public TransactionDetailsDto getTransactionDetails(String transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Product product = null;
        if (transaction.getProductId() != null) {
            product = productRepository.findById(transaction.getProductId()).orElse(null);
        }

        User user = null;
        if (transaction.getUserId() != null) {
            user = userRepository.findById(transaction.getUserId()).orElse(null);
        }

        Supplier supplier = null;
        if (transaction.getSupplierId() != null && !transaction.getSupplierId().isBlank()) {
            supplier = supplierRepository.findById(transaction.getSupplierId()).orElse(null);
        }

        return new TransactionDetailsDto(transaction, product, user, supplier);
    }

    // ==========================================================
    // ✅ Pagination + Search for transaction list UI
    // ==========================================================
    public List<Transaction> getPaginatedTransactions(int page, int size, String search) {

        List<Transaction> all = transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        if (all == null) return Collections.emptyList();

        // ✅ Search filter
        if (search != null && !search.trim().isEmpty()) {
            String s = search.trim().toLowerCase();
            all = all.stream()
                    .filter(t ->
                            (t.getTransactionType() != null && t.getTransactionType().toLowerCase().contains(s)) ||
                                    (t.getStatus() != null && t.getStatus().toLowerCase().contains(s)) ||
                                    (t.getTotalPrice() != null && t.getTotalPrice().toString().toLowerCase().contains(s))
                    )
                    .collect(Collectors.toList());
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, all.size());

        if (start >= all.size() || start < 0) return Collections.emptyList();
        return all.subList(start, end);
    }

    public long getTransactionCount(String search) {
        if (search == null || search.trim().isEmpty()) {
            return transactionRepository.count();
        }
        return getPaginatedTransactions(1, Integer.MAX_VALUE, search).size();
    }

    // ==========================================================
    // ✅ Mapping Logic: Transaction -> TransactionDto
    // NOTE: This requires TransactionDto contains these fields.
    // ==========================================================
    private TransactionDto mapToDto(Transaction t) {

        TransactionDto dto = new TransactionDto();

        dto.setId(t.getId());
        dto.setProductId(t.getProductId());
        dto.setUserId(t.getUserId());
        dto.setSupplierId(t.getSupplierId());

        dto.setTransactionType(t.getTransactionType());
        dto.setTotalProducts(t.getTotalProducts());
        dto.setTotalPrice(t.getTotalPrice());

        dto.setDiscount(t.getDiscount());
        dto.setPromotion(t.getPromotion());

        dto.setSaleDate(t.getSaleDate());
        dto.setStatus(t.getStatus());
        dto.setCreatedAt(t.getCreatedAt());

        return dto;
    }
}
