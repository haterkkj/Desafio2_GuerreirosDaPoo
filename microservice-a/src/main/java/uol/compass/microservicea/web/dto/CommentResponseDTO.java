package uol.compass.microservicea.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.microservicea.model.Comment;

@NoArgsConstructor
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
