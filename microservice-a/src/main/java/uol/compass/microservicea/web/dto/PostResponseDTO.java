package uol.compass.microservicea.web.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.mapper.CommentMapper;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PostResponseDTO {
    private String id;
    private String title;
    private String body;
    private List<CommentResponseDTO> comments;

    public static PostResponseDTO toDto(Post post) {
        List<CommentResponseDTO> comments = post.getComments().stream().map(CommentResponseDTO::toDto).toList();
        return new PostResponseDTO(post.getId(), post.getTitle(), post.getBody(), comments);
    }
}
