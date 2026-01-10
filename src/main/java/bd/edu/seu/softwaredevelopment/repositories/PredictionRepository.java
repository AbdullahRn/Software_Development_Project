package bd.edu.seu.softwaredevelopment.repositories;

import bd.edu.seu.softwaredevelopment.models.Prediction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PredictionRepository extends MongoRepository<Prediction, String> {
    Prediction findFirstByProductIdOrderByPredictionDateDesc(String productId);
    List<Prediction> findByOwnerIdAndProductIdOrderByPredictionDateAsc(String ownerId, String productId);
}


