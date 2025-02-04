package uol.compass.microserviceb.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.PostService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable String id){
        Post post = service.findById(id);
        return ResponseEntity.ok().body(post);
    }


}
