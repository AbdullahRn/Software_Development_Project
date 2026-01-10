package bd.edu.seu.softwaredevelopment.repositories;

import bd.edu.seu.softwaredevelopment.models.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRepository extends MongoRepository<Supplier, String> {
}
