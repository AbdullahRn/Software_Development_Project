package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dto.TransactionTrainingRowDto;
import bd.edu.seu.softwaredevelopment.models.Transaction;
import bd.edu.seu.softwaredevelopment.repositories.TransactionRepository;
import bd.edu.seu.softwaredevelopment.dtos.ProductDto;
import bd.edu.seu.softwaredevelopment.models.Product;
import bd.edu.seu.softwaredevelopment.repositories.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ml/training-data")
public class MlTrainingDataController {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;


    public MlTrainingDataController(TransactionRepository transactionRepository, ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
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

    @GetMapping("/products")
    public List<ProductDto> getProductsTrainingData() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(p -> {
            ProductDto dto = new ProductDto();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setStockQuantity(p.getStockQuantity());
            return dto;
        }).collect(Collectors.toList());
    }

}
