package uol.compass.microserviceb.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @RequiredArgsConstructor @NoArgsConstructor
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;
    @DBRef
    private String postId;
    @NonNull
    private String email;
    @NonNull
    private String name;
    @NonNull
    private String body;
}
