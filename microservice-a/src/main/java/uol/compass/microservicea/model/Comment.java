package uol.compass.microservicea.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Comment {
    private String id;
    @DBRef
    @JsonIgnore
    private Post post;
    private String email;
    private String name;
    private String body;

    public Comment(String id, String email, String name, String body) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.body = body;
    }
}
