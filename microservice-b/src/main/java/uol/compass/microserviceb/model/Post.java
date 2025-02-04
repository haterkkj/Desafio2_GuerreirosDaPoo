package uol.compass.microserviceb.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @RequiredArgsConstructor @NoArgsConstructor

@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String body;
}