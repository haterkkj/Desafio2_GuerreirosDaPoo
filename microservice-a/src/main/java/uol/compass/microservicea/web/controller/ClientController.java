package uol.compass.microservicea.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.services.CommentService;
import uol.compass.microservicea.web.dto.CommentCreateDTO;
import uol.compass.microservicea.web.dto.CommentResponseDTO;
import uol.compass.microservicea.web.dto.mapper.CommentMapper;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/posts")
public class ClientController {
    private final CommentService service;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable String postId, @RequestBody CommentCreateDTO comment) {
        Comment createdComment = service.createCommentInPost(postId, comment);
        CommentResponseDTO response = CommentMapper.fromCommentToDto(createdComment);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getPosts(@PathVariable String postId) {
        List<Comment> comments = service.getCommentsByPostId(postId);
        List<CommentResponseDTO> response = CommentMapper.fromListCommentToListDto(comments);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> createComment(
            @PathVariable String postId,
            @PathVariable String commentId,
            @RequestBody CommentCreateDTO comment) {
        Comment updatedComment = service.updateCommentInPost(postId, commentId, comment);
        CommentResponseDTO response = CommentMapper.fromCommentToDto(updatedComment);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> createComment(
            @PathVariable String postId,
            @PathVariable String commentId) {
        service.deleteCommentInPost(postId, commentId);

        return ResponseEntity.noContent().build();
    }

}
