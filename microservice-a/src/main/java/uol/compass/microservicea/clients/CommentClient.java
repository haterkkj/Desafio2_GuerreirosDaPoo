package uol.compass.microservicea.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.web.dto.CommentCreateDTO;

import java.util.List;

@FeignClient(value = "comment-client", url = "localhost:8081/api/posts")
public interface CommentClient {

    @PostMapping("/{postId}/comments")
    Comment createCommentInPost(@PathVariable("postId") String postId, @RequestBody CommentCreateDTO comment);

    @GetMapping("/{postId}/comments")
    List<Comment> getCommentsByPostId(@PathVariable("postId") String postId);

    @PutMapping("/{postId}/comments/{commentId}")
    Comment updateCommentInPost(@PathVariable("postId") String postId, @PathVariable("commentId") String commentId, @RequestBody CommentCreateDTO comment);

    @DeleteMapping("/{postId}/comments/{commentId}")
    Void deleteCommentInPost(@PathVariable("postId") String postId, @PathVariable("commentId") String commentId);
}
