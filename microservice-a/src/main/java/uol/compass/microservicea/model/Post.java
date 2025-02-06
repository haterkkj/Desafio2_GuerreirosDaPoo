package uol.compass.microservicea.model;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Post {
    private String id;
    private String title;
    private String body;
}