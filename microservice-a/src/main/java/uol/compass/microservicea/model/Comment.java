package uol.compass.microservicea.model;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Comment {
    private String id;
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
