package uol.compass.microserviceb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Post post;
    @NonNull
    private String email;
    @NonNull
    private String name;
    @NonNull
    private String body;

    public Comment(String id, Post post, @NonNull String email, @NonNull String name, @NonNull String body) {
        this.id = id;
        this.post = post;
        this.email = email;
        this.name = name;
        this.body = body;
    }
}
