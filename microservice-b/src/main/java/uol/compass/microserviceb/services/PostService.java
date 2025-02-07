package uol.compass.microserviceb.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uol.compass.microserviceb.clients.PostClient;
import uol.compass.microserviceb.exceptions.EntityNotFoundException;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.repositories.CommentRepository;
import uol.compass.microserviceb.repositories.PostRepository;
import uol.compass.microserviceb.web.dto.FetchedPostDTO;
import uol.compass.microserviceb.web.dto.UpdatePostDTO;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PostService {
    private final PostRepository repository;
    private final PostClient postClient;
    private final CommentRepository commentRepository;

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
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Post not found with ID: " + id));
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
    public Post update(Post obj) {
        if (obj == null || obj.getId() == null) {
            throw new IllegalArgumentException("Object or ID cannot be null");
        }

        Post newObj = findById(obj.getId());
        updateData(newObj, obj);

        try {
            return repository.save(newObj);
        } catch (Exception e) {
            throw new RuntimeException("Error updating post: " + e.getMessage());
        }
    }

    public Post updatePost(String id, UpdatePostDTO dto) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Post ID cannot be null or empty");
        }
        if ((dto.getTitle() == null || dto.getTitle().isBlank()) &&
                (dto.getBody() == null || dto.getBody().isBlank())) {
            throw new IllegalArgumentException("At least one field must be updated");
        }
        Post post = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            post.setTitle(dto.getTitle());
        }
        if (dto.getBody() != null && !dto.getBody().isBlank()) {
            post.setBody(dto.getBody());
        }

        try {
            return repository.save(post);
        } catch (Exception e) {
            throw new RuntimeException("Error updating post: " + e.getMessage());
        }

    }
    @Transactional
    public Optional<Post> getPostWithComments(String postId) {
        Optional<Post> post = repository.findById(postId);
        post.ifPresent(p -> {
            List<Comment> comments = commentRepository.findByPostId(postId);
            p.setComments(comments);
        });
        return post;
    }

    private void updateData(Post newObj, Post obj) {
        newObj.setTitle(obj.getTitle());
        newObj.setBody(obj.getBody());
    }

}
