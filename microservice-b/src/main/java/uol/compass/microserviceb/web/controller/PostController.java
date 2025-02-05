package uol.compass.microserviceb.web.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.PostService;
import uol.compass.microserviceb.web.dto.*;
import uol.compass.microserviceb.web.dto.mapper.PostMapper;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService service;

    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@RequestBody @Valid PostCreateDTO post) {
        Post createdPost = service.save(post.toPost());
        PostResponseDTO postResponse = PostMapper.fromPostToDto(createdPost);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(createdPost.getId())
                .toUri();
        return ResponseEntity.created(location).body(postResponse);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAll() {
        List<Post> listPost = service.findAll();
        List<PostResponseDTO> listPostResponse = PostMapper.fromListPostToListDto(listPost);

        return ResponseEntity.ok().body(listPostResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getById(@PathVariable String id) {
        Post post = service.findById(id);
        PostResponseDTO postResponse = PostMapper.fromPostToDto(post);

        return ResponseEntity.ok().body(postResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deletePostById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/all")
    public ResponseEntity<Void> updateAll(@RequestBody UpdatePostDTO dto, @PathVariable String id) {
        Post post = dto.toPost();
        post.setId(id);
        service.update(post);
        return ResponseEntity.noContent().build(); //204
    }

    @PatchMapping("/{id}/title")
    public ResponseEntity<Void> updateTitle(@PathVariable String id, @RequestBody UpdateTitleDTO dto) {
        service.updateTitle(id, dto.getTitle());
        return ResponseEntity.noContent().build(); //204
    }

    @PatchMapping("/{id}/body")
    public ResponseEntity<Void> updateBody(@PathVariable String id, @RequestBody UpdateBodyDTO dto){
        service.updateBody(id, dto.getBody());
        return ResponseEntity.noContent().build(); //204
    }
}
