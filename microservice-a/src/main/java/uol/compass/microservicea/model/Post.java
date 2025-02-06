package uol.compass.microservicea.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter @RequiredArgsConstructor @NoArgsConstructor
public class Post {
    private String id;
    private String title;
    private String body;
}