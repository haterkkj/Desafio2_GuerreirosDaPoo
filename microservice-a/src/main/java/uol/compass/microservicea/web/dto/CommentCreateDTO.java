package uol.compass.microservicea.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uol.compass.microservicea.model.Comment;

@AllArgsConstructor
@Getter @Setter
public class CommentCreateDTO {
    private String id;
    private String email;
    private String name;
    private String body;

    public Comment toComment(){return new Comment(this.getId(), this.getEmail(), this.getName(), this.getBody());}
}
