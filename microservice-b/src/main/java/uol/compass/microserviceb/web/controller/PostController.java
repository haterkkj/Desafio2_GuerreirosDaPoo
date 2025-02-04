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

    @GetMapping
    public ResponseEntity<List<Post>> getAll(){
        List<Post> listPost = service.findAll();
        return ResponseEntity.ok().body(listPost);
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post){
        Post createdPost = service.save(post);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(createdPost.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdPost);
    }

}
