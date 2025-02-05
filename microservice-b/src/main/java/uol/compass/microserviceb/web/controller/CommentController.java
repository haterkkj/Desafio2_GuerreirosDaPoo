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

import java.net.URI;

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
}
