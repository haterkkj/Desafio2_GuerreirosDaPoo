package uol.compass.microservicea.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uol.compass.microservicea.clients.PostClient;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;

@AllArgsConstructor
@Service
public class PostService {
    private final PostClient postClient;

    public Page<Post> getPosts(Pageable pageable) {
        return postClient.getPosts(pageable);
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

    public Post updatePost(String id, PostUpdateDTO postUpdateDTO) {
        return postClient.updatePost(id, postUpdateDTO);
    }

}
