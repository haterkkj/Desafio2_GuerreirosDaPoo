package uol.compass.microservicea.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.services.PostService;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;

import java.util.List;

@Tag(name = "Posts", description = "Endpoints for managing posts.")
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.getPosts();
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok().body(post);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostCreateDTO postCreateDTO) {
        Post newPost = postService.createPost(postCreateDTO);
        return ResponseEntity.ok().body(newPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id ) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody PostUpdateDTO postUpdateDTO) {
        Post updatedPost = postService.updatePost(id, postUpdateDTO);
        return ResponseEntity.ok().body(updatedPost);
    }

}
