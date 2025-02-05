package uol.compass.microserviceb.web.controller;


import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.PostService;

import uol.compass.microserviceb.web.dto.*;
import uol.compass.microserviceb.web.dto.mapper.PostMapper;
import uol.compass.microserviceb.web.exception.ErrorMessage;


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
    public ResponseEntity<PostResponseDTO> create(@RequestBody @Valid PostCreateDTO post) {
        Post createdPost = service.save(post.toPost());
        PostResponseDTO postResponse = PostMapper.fromPostToDto(createdPost);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(createdPost.getId())
                .toUri();
        return ResponseEntity.created(location).body(postResponse);
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
    public ResponseEntity<List<PostResponseDTO>> getAll() {
        List<Post> listPost = service.findAll();
        List<PostResponseDTO> listPostResponse = PostMapper.fromListPostToListDto(listPost);

        return ResponseEntity.ok().body(listPostResponse);
    }

    @Operation(
            summary = "Retrieve a post by ID",
            description = "Endpoint to retrieve a specific post from the database by its unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post successfully retrieved.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Post.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getById(@PathVariable String id) {
        Post post = service.findById(id);
        PostResponseDTO postResponse = PostMapper.fromPostToDto(post);

        return ResponseEntity.ok().body(postResponse);
    }

    @Operation(summary = "Delete a post by its ID", description = "Deletes a post from the system based on the provided ID. If the post is not found, an exception is thrown.", parameters = {
            @Parameter(name = "id", description = "ID of the post to be deleted", required = true, in = ParameterIn.PATH)
    }, responses = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found with the provided ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input, malformed ID format", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
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
        return ResponseEntity.noContent().build(); // 204
    }

    @PatchMapping("/{id}/title")
    public ResponseEntity<Void> updateTitle(@PathVariable String id, @RequestBody UpdateTitleDTO dto) {
        service.updateTitle(id, dto.getTitle());
        return ResponseEntity.noContent().build(); // 204
    }

    @PatchMapping("/{id}/body")
    public ResponseEntity<Void> updateBody(@PathVariable String id, @RequestBody UpdateBodyDTO dto) {
        service.updateBody(id, dto.getBody());
        return ResponseEntity.noContent().build(); // 204
    }
}
