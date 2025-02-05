package uol.compass.microserviceb.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @RequiredArgsConstructor @NoArgsConstructor

@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String body;
    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList<>();
}