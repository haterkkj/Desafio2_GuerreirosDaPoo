package uol.compass.microservicea.model;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Comment {
    private String id;
    private String email;
    private String name;
    private String body;

}
