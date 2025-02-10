package uol.compass.microserviceb.utils;

import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.model.Post;

import java.util.Map;

public class IntegrationTestsUtils {
    // The key value must be the same of the Post ID.
    public static final Map<Integer, Post> PRE_SAVED_POSTS = Map.of(
            1, new Post("1", "Título do Post 1", "Conteúdo do Post 1"),
            2, new Post("2", "Título do Post 2", "Conteúdo do Post 2")
    );

    // The key value must be the same of the Comment ID.
    public static final Map<Integer, Comment> PRE_SAVED_COMMENTS = Map.of(
            1, new Comment("1", PRE_SAVED_POSTS.get(1), "autor1@email.com", "Autor1", "Conteúdo do Comentário 1"),
            2, new Comment("2", PRE_SAVED_POSTS.get(1), "autor2@email.com", "Autor2", "Conteúdo do Comentário 2"),
            3, new Comment("3", PRE_SAVED_POSTS.get(2), "autor3@email.com", "Autor3", "Conteúdo do Comentário 3")
    );
}
