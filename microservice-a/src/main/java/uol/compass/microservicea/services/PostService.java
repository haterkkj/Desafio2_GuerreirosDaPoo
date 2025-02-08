package uol.compass.microservicea.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uol.compass.microservicea.clients.PostClient;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;

import java.util.List;

@AllArgsConstructor
@Service
public class PostService {
    private final PostClient postClient;

    public List<Post> getPosts() {
        return postClient.getPosts();
    }

    public Post getPostById(String id) {
        return postClient.getPostById(id);
    }

    public Post createPost(PostCreateDTO postCreateDTO) {
        Post createdPost = postClient.createPost(postCreateDTO);

        if (createdPost == null) {
            throw new EntityNotFoundException("Failed to create post");
        }

        return createdPost;
    }

    public void deletePost(String id) {
        postClient.deletePost(id);
    }

    public Post updatePost(String id, PostUpdateDTO postUpdateDTO) {
        return postClient.updatePost(id, postUpdateDTO);
    }

}
