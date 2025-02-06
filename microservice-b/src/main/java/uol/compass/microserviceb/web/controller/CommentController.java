package uol.compass.microserviceb.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.CommentService;
import uol.compass.microserviceb.services.PostService;
import uol.compass.microserviceb.web.dto.CommentCreateDTO;
import uol.compass.microserviceb.web.dto.CommentResponseDTO;
import uol.compass.microserviceb.web.dto.CommentUpdateDTO;
import uol.compass.microserviceb.web.dto.PostCreateDTO;
import uol.compass.microserviceb.web.dto.mapper.CommentMapper;
import uol.compass.microserviceb.web.exception.ErrorMessage;

import java.net.URI;
import java.util.List;

@Tag(name = "Comments", description = "Endpoints for managing comments on posts")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    @Operation(
            summary = "Create a new comment",
            description = "Adds a comment to an existing post identified by `postId`. The created comment is returned in the response.",
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
                            description = "Comment successfully created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - Post ID does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
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
    @PostMapping(value = "/posts/{postId}/comments")
    public ResponseEntity<CommentResponseDTO> create(
            @Parameter(description = "ID of the post to which the comment will be added", required = true)
            @PathVariable String postId,
            @Parameter(description = "Comment data to be created", required = true)
            @Valid @RequestBody CommentCreateDTO createdComment
    ) {
        Post relatedPost = postService.findById(postId);
        Comment comment = createdComment.toComment();
        comment.setPost(relatedPost);
        comment = commentService.save(comment);
        relatedPost.getComments().add(comment);
        postService.save(relatedPost);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(comment.getId())
                .toUri();

        CommentResponseDTO commentResponse = CommentMapper.fromCommentToDto(comment);
        return ResponseEntity.created(location).body(commentResponse);
    }

    @Operation(
            summary = "Retrieve all comments for a post",
            description = "Fetches a list of comments associated with the given `postId`.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of comments successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - The post with the given ID does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @GetMapping(value = "/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getAllComments(
            @Parameter(description = "ID of the post whose comments should be retrieved", required = true)
            @PathVariable String postId
    ) {
        Post relatedPost = postService.findById(postId);
        List<Comment> commentsFromPost = relatedPost.getComments();
        List<CommentResponseDTO> commentsFromPostDto = CommentMapper.fromListCommentToListDto(commentsFromPost);

        return ResponseEntity.ok(commentsFromPostDto);
    }

    @Operation(
            summary = "Retrieve a specific comment by ID",
            description = "Fetches a single comment associated with the given `postId` and `commentId`.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comment retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - The post or comment does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @GetMapping(value = "/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> getCommentById(
            @Parameter(description = "ID of the post containing the comment", required = true)
            @PathVariable String postId,
            @Parameter(description = "ID of the comment to retrieve", required = true)
            @PathVariable String commentId
    ) {
        Post relatedPost = postService.findById(postId);
        Comment comment = findCommentInPost(relatedPost.getComments(), commentId);
        CommentResponseDTO commentResponse = CommentMapper.fromCommentToDto(comment);

        return ResponseEntity.ok(commentResponse);
    }

    @Operation(
            summary = "Delete a comment by ID",
            description = "Deletes a specific comment from a post using `postId` and `commentId`. Returns `204 No Content` if successful.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Comment deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - The post or comment does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @DeleteMapping(value = "/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID of the post containing the comment", required = true)
            @PathVariable String postId,
            @Parameter(description = "ID of the comment to delete", required = true)
            @PathVariable String commentId
    ) {
        List<Comment> commentsInPost = postService.findById(postId).getComments();
        Comment comment = findCommentInPost(commentsInPost, commentId);
        commentService.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update a comment by ID",
            description = "Updates the details of an existing comment identified by `postId` and `commentId`. Returns the updated comment.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comment updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - The post or comment does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
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
    @PutMapping(value = "/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> update(
            @Parameter(description = "ID of the post containing the comment", required = true)
            @PathVariable String postId,
            @Parameter(description = "ID of the comment to update", required = true)
            @PathVariable String commentId,
            @Parameter(description = "Updated comment data", required = true)
            @RequestBody CommentUpdateDTO updatedComment
    ) {
        List<Comment> commentsInPost = postService.findById(postId).getComments();
        Comment comment = findCommentInPost(commentsInPost, commentId);
        comment.setName(updatedComment.getName());
        comment.setBody(updatedComment.getBody());
        comment = commentService.update(comment);

        CommentResponseDTO commentResponse = CommentMapper.fromCommentToDto(comment);
        return ResponseEntity.ok().body(commentResponse);
    }

    private Comment findCommentInPost(List<Comment> commentsInPost, String commentId) {
        for (Comment comment : commentsInPost) {
            if (comment.getId().equals(commentId)) {
                return comment;
            }
        }
        return null;
    }
}
