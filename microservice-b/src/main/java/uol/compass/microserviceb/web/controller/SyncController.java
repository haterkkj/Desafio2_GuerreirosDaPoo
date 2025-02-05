package uol.compass.microserviceb.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.PostService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class SyncController {
    private final PostService service;

    @PostMapping("/sync")
    public ResponseEntity<List<Post>> syncData() {
        List<Post> listPost = service.syncData();
        return ResponseEntity.ok().body(listPost);
    }
}
