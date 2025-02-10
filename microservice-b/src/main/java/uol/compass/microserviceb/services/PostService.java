package uol.compass.microserviceb.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uol.compass.microserviceb.clients.PostClient;
import uol.compass.microserviceb.exceptions.EntityNotFoundException;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.repositories.PostRepository;
import uol.compass.microserviceb.web.dto.FetchedPostDTO;
import uol.compass.microserviceb.web.dto.PostUpdateDTO;

import java.util.List;

@AllArgsConstructor
@Service
public class PostService {
    private final PostRepository repository;
    private final PostClient postClient;

    @Transactional
    public List<Post> syncData() {
        try {
            List<Post> posts = postClient.getPosts().stream().map(FetchedPostDTO::toPost).toList();
            return repository.saveAll(posts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Post save(Post post) {
        try {
            return repository.save(post);
        } catch (Exception e) {
            throw new RuntimeException("Error saving post: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving posts: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Post findById(String id) {
        try {
            return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + id));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving post: " + e.getMessage());
        }
    }

    @Transactional
    public void deletePostById(String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Post not found with ID: " + id);
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting post: " + e.getMessage());
        }
    }

    @Transactional
    public Post updatePost(String id, PostUpdateDTO dto) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Post ID cannot be null or empty");
        }

        Post post = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Post not found with ID: " + id)
        );

        post.setTitle(dto.getTitle());
        post.setBody(dto.getBody());

        try {
            return repository.save(post);
        } catch (Exception e) {
            throw new RuntimeException("Error updating post: " + e.getMessage());
        }
    }
}
