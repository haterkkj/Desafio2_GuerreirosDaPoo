package uol.compass.microserviceb.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uol.compass.microserviceb.model.Comment;

@Getter @Setter @AllArgsConstructor
public class CommentCreateDTO {
    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must be valid")
    private String email;

    @NotBlank
    @Size(min = 2, max = 180)
    private String name;

    @NotBlank
    @Size(min = 3, max = 1080)
    private String body;

    public Comment toComment() {
        return new Comment(email, name, body);
    }
}
