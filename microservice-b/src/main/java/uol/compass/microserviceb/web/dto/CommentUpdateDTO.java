package uol.compass.microserviceb.web.dto;

import lombok.Getter;
import lombok.Setter;
import uol.compass.microserviceb.model.Comment;

@Getter @Setter
public class CommentUpdateDTO {
    private String name;
    private String body;
}
