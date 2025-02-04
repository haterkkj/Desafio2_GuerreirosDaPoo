package uol.compass.microserviceb.web.dto;

import lombok.*;
import uol.compass.microserviceb.model.Post;

@Getter @Setter
public class UpdatePostDTO {
    private String title;
    private String body;

    public Post toPost() {
        return new Post(this.title, this.body);
    }
}
