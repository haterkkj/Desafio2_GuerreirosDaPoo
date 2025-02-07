package uol.compass.microserviceb.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostUpdateDTO {
    @NotBlank
    @Size(min = 3, max = 80)
    private String title;
    @NotBlank
    @Size(min = 3, max = 2080)
    private String body;
}
