package uol.compass.microserviceb.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import uol.compass.microserviceb.model.Post;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePostDTO {
    private String title;
    private String body;

}
