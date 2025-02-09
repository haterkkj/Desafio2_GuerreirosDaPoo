package uol.compass.microservicea.model;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Comment {
    private String id;
    private Post post;
    private String email;
    private String name;
    private String body;
}
