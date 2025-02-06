package uol.compass.microservicea.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uol.compass.microservicea.clients.CommentClient;
import uol.compass.microservicea.model.Comment;

import java.util.List;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentClient client;

    public List<Comment> getCommentsByPostId(String postId) {
        return client.getCommentsByPostId(postId);
    }

}
