package uol.compass.microserviceb.web.dto;

import lombok.Getter;
import lombok.Setter;
import uol.compass.microserviceb.model.Post;

@Getter @Setter
public class FetchedPostDTO {
    private long id;
    private long userId;
    private String title;
    private String body;

    public Post toPost() {
        return new Post(this.title, this.body);
    }
}
