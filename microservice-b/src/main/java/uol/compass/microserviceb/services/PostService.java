package uol.compass.microserviceb.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.repositories.PostRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class PostService {
    private final PostRepository repository;

    @Transactional
    public Post save(Post post) {
        return repository.save(post);
    }
  
    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Post findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Postagem não encontrada.")
        );
    }
}
