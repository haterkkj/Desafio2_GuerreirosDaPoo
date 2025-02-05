package uol.compass.microserviceb.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.CommentService;
import uol.compass.microserviceb.services.PostService;
import uol.compass.microserviceb.web.dto.CommentCreateDTO;
import uol.compass.microserviceb.web.dto.CommentUpdateDTO;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    @PostMapping(value = "/posts/{postId}/comments")
    public ResponseEntity<Comment> create(@PathVariable String postId, @RequestBody CommentCreateDTO createdComment) {
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
        return ResponseEntity.created(location).body(comment);
    }

    @GetMapping(value = "/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable String postId) {
        Post relatedPost = postService.findById(postId);
        List<Comment> commentsFromPost = relatedPost.getComments();
        return ResponseEntity.ok(commentsFromPost);
    }

    @GetMapping(value = "/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable String postId, @PathVariable String commentId) {
        Post relatedPost = postService.findById(postId);
        Comment comment = findCommentInPost(relatedPost.getComments(), commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping(value = "/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteById(@PathVariable String postId, @PathVariable String commentId) {
        List<Comment> commentsInPost = postService.findById(postId).getComments();
        Comment comment = findCommentInPost(commentsInPost, commentId);
        if (comment != null) {
            commentService.deleteById(commentId);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Comment> update(@PathVariable String postId, @PathVariable String commentId, @RequestBody CommentUpdateDTO updatedComment) {
        List<Comment> commentsInPost = postService.findById(postId).getComments();
        Comment comment = findCommentInPost(commentsInPost, commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        comment.setName(updatedComment.getName());
        comment.setBody(updatedComment.getBody());
        comment = commentService.update(comment);
        return ResponseEntity.ok().body(comment);
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
