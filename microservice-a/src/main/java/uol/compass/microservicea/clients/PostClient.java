package uol.compass.microservicea.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.UpdatePostDTO;

import java.util.List;

@FeignClient(value = "post-client", url = "http://localhost:8081/")
public interface PostClient {

    @GetMapping("/posts")
    List<Post> getPosts();

    @GetMapping("/posts/{id}")
    Post getPostById(@PathVariable("id") Long id);

    @PostMapping("/posts")
    Post createPost(@RequestBody PostCreateDTO postCreateDTO);

    @PutMapping("/posts/{id}")
    Post updatePost(@PathVariable("id") Long id, @RequestBody UpdatePostDTO updatePostDTO);

    @DeleteMapping("/posts/{id}")
    void deletePost(@PathVariable("id") Long id);

}
