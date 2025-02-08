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
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.services.CommentService;
import uol.compass.microservicea.web.dto.*;
import uol.compass.microservicea.web.dto.mapper.CommentMapper;
import uol.compass.microservicea.web.exception.ErrorMessage;

import java.net.URI;
import java.util.List;

@Tag(name = "Comment", description = "Endpoints for managing posts consuming Micro Service B.")
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/posts")
public class CommentController {
    private final CommentService service;

    @Operation(
            summary = "Create a new comment",
            description = "Creates a new comment in a post, consuming Micro Service B.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Comment data to be created.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentCreateDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Comment successfully created.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDTO.class)
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
                    )
            }
    )
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDTO> createComment(
            @PathVariable String postId,
            @RequestBody CommentCreateDTO comment
    ) {
        Comment createdComment = service.createCommentInPost(postId, comment);
        CommentResponseDTO response = CommentResponseDTO.toDto(createdComment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdComment.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(
            summary = "List all commento from a posts",
            description = "Endpoint to retrieve all posts from the database consuming Micro Service B.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Posts successfully retrieved.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CommentResponseDTO.class))
                            )
                    )
            }
    )
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getPosts(
            @PathVariable String postId
    ) {
        List<Comment> comments = service.getCommentsByPostId(postId);
        List<CommentResponseDTO> response = CommentMapper.fromListCommentToListDto(comments);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Update a comment by ID",
            description = "Resource to partially update an existing comment consuming Micro Service B.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Comment data to be updated.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentUpdateDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comment updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDTO.class)) // Correção
                    ),
                    @ApiResponse(responseCode = "404", description = "Comment not found",
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
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable String postId,
            @PathVariable String commentId,
            @RequestBody CommentUpdateDTO comment
    ) {
        Comment updatedComment = service.updateCommentInPost(postId, commentId, comment);
        CommentResponseDTO response = CommentResponseDTO.toDto(updatedComment);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Delete a Comment by ID",
            description = "Deletes a comment from the system based on the provided ID consuming Micro Service B. If the post is not found, an exception is thrown.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Comment deleted successfully"
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input - Malformed ID format",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - Comment not found",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            })
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable String postId,
            @PathVariable String commentId
    ) {
        service.deleteCommentInPost(postId, commentId);

        return ResponseEntity.noContent().build();
    }

}
