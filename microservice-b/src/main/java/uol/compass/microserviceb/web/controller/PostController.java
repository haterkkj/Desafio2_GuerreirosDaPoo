package uol.compass.microserviceb.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.PostService;
import uol.compass.microserviceb.web.dto.UpdateBodyDTO;
import uol.compass.microserviceb.web.dto.UpdatePostDTO;
import uol.compass.microserviceb.web.dto.UpdateTitleDTO;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService service;

    @Operation(
            summary = "Create a new post",
            description = "Endpoint to create a new post in the database.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Post data to be created.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Post.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Post successfully created.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Post.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        Post createdPost = service.save(post);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(createdPost.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdPost);
    }

    @Operation(
            summary = "List all posts",
            description = "Endpoint to retrieve all posts from the database.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Posts successfully retrieved.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Post.class))
                            )
                    )
            }
    )

    @GetMapping
    public ResponseEntity<List<Post>> getAll() {
        List<Post> listPost = service.findAll();
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
