package uol.compass.microserviceb.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.microserviceb.model.Post;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PostResponseDTO {
    private String id;
    private String title;
    private String body;
    private List<CommentResponseDTO> comments;

    public static PostResponseDTO toDTO(Post post) {
        List<CommentResponseDTO> commentResponseDTO = post.getComments().stream().map(CommentResponseDTO::toDto).toList();
        return new PostResponseDTO(post.getId(), post.getTitle(), post.getBody(), commentResponseDTO);
    }
}
