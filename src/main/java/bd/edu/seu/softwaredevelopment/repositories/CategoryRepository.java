package bd.edu.seu.softwaredevelopment.repositories;

import bd.edu.seu.softwaredevelopment.models.Category;
import org.springframework.data.mongodb.core.convert.ReferenceResolver;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findByName(String name);
    Optional<Category> findById(String id);

}


