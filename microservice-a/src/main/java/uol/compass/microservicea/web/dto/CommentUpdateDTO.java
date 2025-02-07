package uol.compass.microservicea.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uol.compass.microservicea.model.Comment;

@AllArgsConstructor
@Getter @Setter
public class CommentUpdateDTO {
    private String name;
    private String body;

}
