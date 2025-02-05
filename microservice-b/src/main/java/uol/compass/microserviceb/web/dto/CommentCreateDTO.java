package uol.compass.microserviceb.web.dto;

import lombok.Getter;
import lombok.Setter;
import uol.compass.microserviceb.model.Comment;

@Getter @Setter
public class CommentCreateDTO {
    private String email;
    private String name;
    private String body;

    public Comment toComment() {
        return new Comment(email, name, body);
    }
}
