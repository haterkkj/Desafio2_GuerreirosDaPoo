package uol.compass.microservicea.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.model.Post;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class IntegrationTestUtils {
    public static final List<Post> PRE_SAVED_POSTS = List.of(
            new Post("1", "Título do Post 1", "Conteúdo do Post 1"),
            new Post("2", "Título do Post 2", "Conteúdo do Post 2")
    );

    public static final List<Comment> PRE_SAVED_COMMENTS = List.of(
            new Comment("1", PRE_SAVED_POSTS.get(0), "autor1@email.com", "Autor1", "Conteúdo do Comentário 1"),
            new Comment("2", PRE_SAVED_POSTS.get(0), "autor2@email.com", "Autor2", "Conteúdo do Comentário 2"),
            new Comment("3", PRE_SAVED_POSTS.get(1), "autor3@email.com", "Autor3", "Conteúdo do Comentário 3")
    );

    public static void mockBindingResultWithErrors(BindingResult bindingResult) {
        FieldError fieldError = new FieldError("myRequest", "invalidField", "Field is invalid");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
    }
}
