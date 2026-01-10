package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dto.TransactionTrainingRowDto;
import bd.edu.seu.softwaredevelopment.models.Transaction;
import bd.edu.seu.softwaredevelopment.repositories.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ml/training-data")
public class MlTrainingDataController {

    private final TransactionRepository transactionRepository;

    public MlTrainingDataController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/transactions")
    public List<TransactionTrainingRowDto> getTransactionsTrainingData() {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream()
                .filter(t -> t.getSaleDate() != null)
                .map(t -> new TransactionTrainingRowDto(
                        t.getProductId(),
                        t.getSaleDate(),
                        t.getTotalProducts(),
                        t.getTotalPrice()
                ))
                .collect(Collectors.toList());
    }
}
