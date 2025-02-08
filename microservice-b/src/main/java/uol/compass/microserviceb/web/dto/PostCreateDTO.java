package uol.compass.microserviceb.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.microserviceb.model.Post;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PostCreateDTO {
    @NotBlank
    @Size(min = 3, max = 80)
    private String title;
    @NotBlank
    @Size(min = 3, max = 1080)
    private String body;

    public Post toPost() {
        return new Post(this.title, this.body);
    }
}
