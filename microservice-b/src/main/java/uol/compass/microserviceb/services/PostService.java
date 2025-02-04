package uol.compass.microserviceb.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uol.compass.microserviceb.clients.PostClient;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.repositories.PostRepository;
import uol.compass.microserviceb.web.dto.FetchedPostDTO;

import java.util.List;

@AllArgsConstructor
@Service
public class PostService {
    private final PostRepository repository;
    private final PostClient postClient;

    public List<Post> syncData() {
        try {
            List<Post> posts = postClient.getPosts().stream().map(FetchedPostDTO::toPost).toList();
            return repository.saveAll(posts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return repository.findAll();
    }
}
