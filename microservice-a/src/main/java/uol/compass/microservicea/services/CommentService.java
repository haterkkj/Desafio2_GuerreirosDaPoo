package uol.compass.microservicea.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uol.compass.microservicea.clients.CommentClient;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.web.dto.CommentCreateDTO;
import uol.compass.microservicea.web.dto.CommentUpdateDTO;

import java.util.List;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentClient client;

    public Comment createCommentInPost(String postId, CommentCreateDTO comment) {
        return client.createCommentInPost(postId, comment);
    }

    public List<Comment> getCommentsByPostId(String postId) {
        return client.getCommentsByPostId(postId);
    }

    public Comment getCommentById(String postId, String commentId) {
        return client.getCommentById(postId, commentId);
    }

    public Comment updateCommentInPost(String postId, String commentId, CommentUpdateDTO comment) {
        return client.updateCommentInPost(postId, commentId, comment);
    }

    public void deleteCommentInPost(String postId, String commentId) {
        client.deleteCommentInPost(postId, commentId);
    }
}
