package uol.compass.microserviceb.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uol.compass.microserviceb.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
}
