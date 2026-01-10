package bd.edu.seu.softwaredevelopment.repositories;

import bd.edu.seu.softwaredevelopment.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String > {
    List<Product> findByCategoryId(String categoryId);
    List<Product> findBySupplierId(String supplierId);

    List<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku);

    boolean existsBySku(String sku);
}


