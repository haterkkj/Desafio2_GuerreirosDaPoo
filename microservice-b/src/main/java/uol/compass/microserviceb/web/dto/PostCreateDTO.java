package uol.compass.microserviceb.web.dto;

import lombok.Getter;
import lombok.Setter;
import uol.compass.microserviceb.model.Post;

@Getter @Setter
public class PostCreateDTO {
    private String title;
    private String body;
}
