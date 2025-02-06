package uol.compass.microservicea.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostCreateDTO {
    @NotBlank
    @Size(min = 3, max = 80)
    private String title;
    @NotBlank
    @Size(min = 3, max = 1080)
    private String body;
}
