package bd.edu.seu.softwaredevelopment.interfaces;

import bd.edu.seu.softwaredevelopment.dtos.TransactionDto;
import bd.edu.seu.softwaredevelopment.dtos.TransactionRequest;

import java.util.List;

public interface TransactionServiceInterface {
    TransactionDto sell(TransactionRequest transactionRequest);

    TransactionDto purchase(TransactionRequest transactionRequest);

    List<TransactionDto> getAllTransactions();

    TransactionDto getTransactionById(String id);

    List<TransactionDto> getSalesHistoryForProduct(String productId);
    List<TransactionDto> getTransactionsByMonthAndYear(int month, int year);
    TransactionDto updateTransactionStatus(String transactionId, String status);
}
