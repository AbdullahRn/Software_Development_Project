package bd.edu.seu.softwaredevelopment.repositories;

import bd.edu.seu.softwaredevelopment.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByProductIdAndTransactionTypeOrderByCreatedAtAsc(String productId, String transactionType);

    List<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Transaction> findByUserId(String userId);
    List<Transaction> findByProductIdAndSaleDateBetweenOrderBySaleDateAsc(String productId, LocalDate start, LocalDate end);
}

