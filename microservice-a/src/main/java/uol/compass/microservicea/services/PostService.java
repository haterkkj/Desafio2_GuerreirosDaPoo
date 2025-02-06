package uol.compass.microservicea.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uol.compass.microservicea.clients.PostClient;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.UpdatePostDTO;

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
        return postClient.createPost(postCreateDTO);
    }

    public void deletePost(String id) {
        postClient.deletePost(id);
    }

    public Post updatePost(String id, UpdatePostDTO updatePostDTO) {
        return postClient.updatePost(id, updatePostDTO);
    }

}
