package uol.compass.microserviceb.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uol.compass.microserviceb.clients.PostClient;
import uol.compass.microserviceb.exceptions.EntityNotFoundException;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.repositories.PostRepository;
import uol.compass.microserviceb.web.dto.FetchedPostDTO;
import uol.compass.microserviceb.web.dto.UpdatePostDTO;

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
        Post newObj = findById(obj.getId());
        updateData(newObj, obj);
        return repository.save(newObj);
    }

    @Transactional
    public Post updateTitle(String id, String title) {
        Post post = findById(id);
        post.setTitle(title);
        return repository.save(post);
    }

    @Transactional
    public Post updateBody(String id, String body) {
        Post post = findById(id);
        post.setBody(body);
        return repository.save(post);
    }

    private void updateData(Post newObj, Post obj) {
        newObj.setTitle(obj.getTitle());
        newObj.setBody(obj.getBody());
    }

    public void updatePost(String id, UpdatePostDTO dto) {
        Post post = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post não encontrado"));

        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }
        if (dto.getBody() != null) {
            post.setBody(dto.getBody());
        }

        repository.save(post);
    }
}
