package uol.compass.microserviceb.web.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import uol.compass.microserviceb.model.Comment;

@Getter @Setter
public class CommentCreateDTO {
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email Invalid")
    private String email;
    private String name;
    private String body;

    public Comment toComment() {
        return new Comment(email, name, body);
    }
}
