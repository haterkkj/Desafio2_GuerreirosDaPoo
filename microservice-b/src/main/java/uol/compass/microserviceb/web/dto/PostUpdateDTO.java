package uol.compass.microserviceb.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostUpdateDTO {
    private String title;
    private String body;
}
