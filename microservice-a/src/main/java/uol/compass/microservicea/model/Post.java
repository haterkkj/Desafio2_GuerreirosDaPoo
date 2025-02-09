package uol.compass.microservicea.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Post {
    private String id;
    private String title;
    private String body;
    private List<Comment> comments = new ArrayList<>();

    public Post(String id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }
}