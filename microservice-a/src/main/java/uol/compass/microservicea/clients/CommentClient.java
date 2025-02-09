package uol.compass.microservicea.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uol.compass.microservicea.config.FeignConfig;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.web.dto.CommentCreateDTO;
import uol.compass.microservicea.web.dto.CommentUpdateDTO;

import java.util.List;

@FeignClient(value = "comment-client", url = "localhost:8081/api/posts", configuration = FeignConfig.class)
public interface CommentClient {

    @PostMapping("/{postId}/comments")
    Comment createCommentInPost(
            @PathVariable("postId") String postId,
            @RequestBody CommentCreateDTO comment
    );

    @GetMapping("/{postId}/comments")
    List<Comment> getCommentsByPostId(
            @PathVariable("postId") String postId
    );

    @GetMapping("/{postId}/comments/{commentId}")
    Comment getCommentById(
            @PathVariable("postId") String postId,
            @PathVariable("commentId") String commentId
    );

    @PutMapping("/{postId}/comments/{commentId}")
    Comment updateCommentInPost(
            @PathVariable("postId") String postId,
            @PathVariable("commentId") String commentId,
            @RequestBody CommentUpdateDTO comment
    );

    @DeleteMapping("/{postId}/comments/{commentId}")
    Void deleteCommentInPost(
            @PathVariable("postId") String postId,
            @PathVariable("commentId") String commentId
    );
}
