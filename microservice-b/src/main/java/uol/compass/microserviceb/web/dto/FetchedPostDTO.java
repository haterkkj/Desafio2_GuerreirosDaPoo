package uol.compass.microserviceb.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uol.compass.microserviceb.model.Post;

@NoArgsConstructor
@AllArgsConstructor
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
