package uol.compass.microservicea.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.services.PostService;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostResponseDTO;
import uol.compass.microservicea.web.dto.UpdatePostDTO;
import uol.compass.microservicea.web.dto.mapper.PostMapper;

import java.net.URI;
import java.util.List;

@Tag(name = "Posts", description = "Endpoints for managing posts.")
@AllArgsConstructor
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
                            description = "Posts successfully retrieved.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostResponseDTO.class))
                            )
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
                            description = "Post successfully retrieved.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PostResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Object.class) // Temporário, alterar para ErrorMessage depois.
                            )
                    ),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable String id) {
        Post post = postService.getPostById(id);
        PostResponseDTO postDto = PostMapper.fromPostToDto(post);

        return ResponseEntity.ok().body(postDto);
    }

    @Operation(
            summary = "Create a new post",
            description = "Endpoint to create a new post in the database consuming Micro Service B.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Post data to be created.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCreateDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Post successfully created.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PostResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class)) // Temporário, alterar para ErrorMessage depois.
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity - Invalid Arguments",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class)) // Temporário, alterar para ErrorMessage depois.
                    )
            }
    )
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostCreateDTO postCreateDTO) {
        Post newPost = postService.createPost(postCreateDTO);
        PostResponseDTO newPostDto = PostMapper.fromPostToDto(newPost);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newPost.getId())
                .toUri();

        return ResponseEntity.created(location).body(newPostDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id ) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable String id, @RequestBody UpdatePostDTO updatePostDTO) {
        Post updatedPost = postService.updatePost(id, updatePostDTO);
        PostResponseDTO updatedPostDto = PostMapper.fromPostToDto(updatedPost);

        return ResponseEntity.ok().body(updatedPostDto);
    }

}
