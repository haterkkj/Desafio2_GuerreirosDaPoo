package uol.compass.microserviceb.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CommentUpdateDTO {
    @NotBlank
    @Size(min = 2, max = 180)
    private String name;

    @NotBlank
    @Size(min = 3, max = 1080)
    private String body;
}
