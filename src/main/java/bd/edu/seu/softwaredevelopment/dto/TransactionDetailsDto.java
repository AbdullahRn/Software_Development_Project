package bd.edu.seu.softwaredevelopment.dto;

import bd.edu.seu.softwaredevelopment.models.Product;
import bd.edu.seu.softwaredevelopment.models.Supplier;
import bd.edu.seu.softwaredevelopment.models.Transaction;
import bd.edu.seu.softwaredevelopment.models.User;

public class TransactionDetailsDto {

    private final Transaction transaction;
    private final Product product;
    private final User user;
    private final Supplier supplier;

    public TransactionDetailsDto(Transaction transaction, Product product, User user, Supplier supplier) {
        this.transaction = transaction;
        this.product = product;
        this.user = user;
        this.supplier = supplier;
    }

    public Transaction getTransaction() { return transaction; }
    public Product getProduct() { return product; }
    public User getUser() { return user; }
    public Supplier getSupplier() { return supplier; }
}
