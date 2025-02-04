package uol.compass.microserviceb.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.PostService;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService service;

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        Post createdPost = service.save(post);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(createdPost.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdPost);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAll() {
        List<Post> listPost = service.findAll();
        return ResponseEntity.ok().body(listPost);
    }

    @PostMapping("/sync")
    public ResponseEntity<List<Post>> syncData() {
        List<Post> listPost = service.syncData();
        return ResponseEntity.ok().body(listPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable String id) {
        Post post = service.findById(id);
        return ResponseEntity.ok().body(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        service.deletePostById(id);
        return ResponseEntity.noContent().build();
    }
}
