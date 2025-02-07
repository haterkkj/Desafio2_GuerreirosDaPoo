package uol.compass.microservicea.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uol.compass.microservicea.config.FeignConfig;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;

import java.util.List;

@FeignClient(value = "post-client", url = "http://localhost:8081/", configuration = FeignConfig.class)
public interface PostClient {

    @GetMapping("/posts")
    List<Post> getPosts();

    @GetMapping("/posts/{id}")
    Post getPostById(@PathVariable("id") String id);

    @PostMapping("/posts")
    Post createPost(@RequestBody PostCreateDTO postCreateDTO);

    @PutMapping("/posts/{id}")
    Post updatePost(@PathVariable("id") String id, @RequestBody PostUpdateDTO postUpdateDTO);

    @DeleteMapping("/posts/{id}")
    void deletePost(@PathVariable("id") String id);

}
