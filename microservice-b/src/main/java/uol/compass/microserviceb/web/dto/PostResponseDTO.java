package uol.compass.microserviceb.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private String id;
    private String title;
    private String body;
    private List<CommentResponseDTO> comments;
}
