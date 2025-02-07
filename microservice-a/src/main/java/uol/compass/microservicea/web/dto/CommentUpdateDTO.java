package uol.compass.microservicea.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class CommentUpdateDTO {
    private String name;
    private String body;
}
