package uol.compass.microservicea.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.services.PostService;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostResponseDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;
import uol.compass.microservicea.web.dto.mapper.PostMapper;
import uol.compass.microservicea.web.exception.ErrorMessage;

import java.net.URI;
import java.util.List;

@Tag(name = "Posts", description = "Endpoints for managing posts consuming Micro Service B.")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/posts")
public class PostController {
    private final PostService postService;

    @Operation(
            summary = "List all posts",
            description = "Endpoint to retrieve all posts from the database consuming Micro Service B.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok - Posts successfully retrieved.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostResponseDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getPosts() {
        List<Post> posts = postService.getPosts();
        List<PostResponseDTO> postsDto = PostMapper.fromListPostToListDto(posts);
        return ResponseEntity.ok().body(postsDto);
    }

    @Operation(
            summary = "Retrieve a post by ID",
            description = "Endpoint to retrieve a specific post from the database by its unique identifier consuming Micro Service B.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok - Post successfully retrieved.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found - Post not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(
            @PathVariable String id
    ) {
        Post post = postService.getPostById(id);
        PostResponseDTO postDto = PostResponseDTO.toDto(post);

        return ResponseEntity.ok().body(postDto);
    }

    @Operation(
            summary = "Create a new post",
            description = "Endpoint to create a new post in the database consuming Micro Service B.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Post data to be created.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created - Post successfully created.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity - Invalid Arguments",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestBody PostCreateDTO postCreateDTO
    ) {
        Post newPost = postService.createPost(postCreateDTO);
        PostResponseDTO newPostDto = PostResponseDTO.toDto(newPost);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newPost.getId())
                .toUri();

        return ResponseEntity.created(location).body(newPostDto);
    }

    @Operation(summary = "Delete a post by its ID",
            description = "Deletes a post from the system based on the provided ID consuming Micro Service B. If the post is not found, an exception is thrown.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No content - Post deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - Post not found",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable String id
    ) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a post by ID", description = "Resource to partially update an existing post consuming Micro Service B.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Post data to be updated.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok - Post updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found - Post not found",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity - Invalid Arguments",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable String id,
            @RequestBody PostUpdateDTO postUpdateDTO
    ) {
        Post updatedPost = postService.updatePost(id, postUpdateDTO);
        PostResponseDTO updatedPostDto = PostResponseDTO.toDto(updatedPost);
        return ResponseEntity.ok().body(updatedPostDto);

    }
}
