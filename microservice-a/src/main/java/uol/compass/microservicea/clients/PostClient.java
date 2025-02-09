package uol.compass.microservicea.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uol.compass.microservicea.config.FeignConfig;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;

@FeignClient(value = "post-client", url = "http://localhost:8081/api/posts", configuration = FeignConfig.class)
public interface PostClient {

    @GetMapping
    Page<Post> getPosts(@SpringQueryMap Pageable pageable);

    @GetMapping("/{id}")
    Post getPostById(@PathVariable("id") String id);

    @PostMapping
    Post createPost(@RequestBody PostCreateDTO postCreateDTO);

    @PutMapping("/{id}")
    Post updatePost(@PathVariable("id") String id, @RequestBody PostUpdateDTO postUpdateDTO);

    @DeleteMapping("/{id}")
    void deletePost(@PathVariable("id") String id);

}
