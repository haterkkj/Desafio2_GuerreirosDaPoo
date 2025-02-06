package uol.compass.microserviceb.web.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Posts", description = "Endpoints for managing posts")
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
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity - Invalid Arguments",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @PostMapping
    public ResponseEntity<PostResponseDTO> create(
            @Parameter(description = "Body with the data of the post that will be created.", required = true)
            @RequestBody @Valid PostCreateDTO post
    ) {
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
                                    array = @ArraySchema(schema = @Schema(implementation = PostResponseDTO.class))
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
                                    schema = @Schema(implementation = PostResponseDTO.class)
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
    public ResponseEntity<PostResponseDTO> getById(
            @Parameter(description = "Id of the post.", required = true)
            @PathVariable String id
    ) {
        Post post = service.findById(id);
        PostResponseDTO postResponse = PostMapper.fromPostToDto(post);

        return ResponseEntity.ok().body(postResponse);
    }

    @Operation(summary = "Delete a post by its ID", description = "Deletes a post from the system based on the provided ID. If the post is not found, an exception is thrown.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Post deleted successfully"
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input - Malformed ID format",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - Post not found",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id of the post.", required = true)
            @PathVariable String id
    ) {
        service.deletePostById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a post by ID", description = "Resource to partially update an existing post.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Post data to be updated.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCreateDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Post not found",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid data for update",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity - Invalid Arguments",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @Parameter(description = "Id of the post.", required = true)
            @PathVariable String id,
            @Parameter(description = "Body with the data of the post that will be updated.", required = true)
            @RequestBody UpdatePostDTO dto
    ) {
        Post post = service.updatePost(id, dto);
        PostResponseDTO response = PostMapper.fromPostToDto(post);

        return ResponseEntity.ok().body(response);
    }
}
