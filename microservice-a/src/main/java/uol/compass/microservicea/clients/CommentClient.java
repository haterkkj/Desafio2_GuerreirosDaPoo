package uol.compass.microservicea.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.web.dto.FetchedCommentDTO;

import java.util.List;

@FeignClient(value = "comment-client", url = "localhost:8081/api/posts")
public interface CommentClient {

    @GetMapping("/{postId}/comments")
    List<Comment> getComments(@PathVariable("postId") String postId);

}
