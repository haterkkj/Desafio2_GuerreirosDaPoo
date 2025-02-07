package uol.compass.microservicea.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Post {
    private String id;
    private String title;
    private String body;
    private List<Comment> comments = new ArrayList<>();
}