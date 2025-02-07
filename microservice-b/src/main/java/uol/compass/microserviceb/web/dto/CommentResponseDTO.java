package uol.compass.microserviceb.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uol.compass.microserviceb.model.Comment;

@AllArgsConstructor
@Getter @Setter
public class CommentResponseDTO {
    private String id;
    private String email;
    private String name;
    private String body;

    public static CommentResponseDTO toDto(Comment comment) {
        return new CommentResponseDTO(comment.getId(), comment.getEmail(), comment.getName(), comment.getBody());
    }
}
