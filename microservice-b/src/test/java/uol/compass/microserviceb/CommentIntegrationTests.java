package uol.compass.microserviceb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.web.dto.*;
import uol.compass.microserviceb.web.exception.ErrorMessage;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentIntegrationTests {

    @Autowired
    private WebTestClient testClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final String BASE_URI = "api/posts";

    // The key value must be the same of the Post ID.
    private final Map<Integer, Post> PRE_SAVED_POSTS = Map.of(
            1, new Post("1", "Título do Post 1", "Conteúdo do Post 1"),
            2, new Post("2", "Título do Post 2", "Conteúdo do Post 2")
    );

    // The key value must be the same of the Comment ID.
    private final Map<Integer, Comment> PRE_SAVED_COMMENTS = Map.of(
            1, new Comment("1", PRE_SAVED_POSTS.get(1), "autor1@email.com", "Autor1", "Conteúdo do Comentário 1"),
            2, new Comment("2", PRE_SAVED_POSTS.get(1), "autor2@email.com", "Autor2", "Conteúdo do Comentário 2"),
            3, new Comment("3", PRE_SAVED_POSTS.get(2), "autor3@email.com", "Autor3", "Conteúdo do Comentário 3")
    );


    @BeforeEach
    public void insertTestPosts() {
        PRE_SAVED_POSTS.forEach(
                (key, post) -> mongoTemplate.save(post)
        );

        PRE_SAVED_COMMENTS.forEach(
                (key, comment) -> mongoTemplate.save(comment)
        );

        Collections.addAll(PRE_SAVED_POSTS.get(1).getComments(),
                PRE_SAVED_COMMENTS.get(1),
                PRE_SAVED_COMMENTS.get(2)
        );

        Collections.addAll(PRE_SAVED_POSTS.get(2).getComments(),
                PRE_SAVED_COMMENTS.get(3)
        );
    }

    @AfterEach
    public void cleanUp() {
        mongoTemplate.dropCollection(Post.class);
        mongoTemplate.dropCollection(Comment.class);
    }

    @Test
    public void createComment_WithValidData_ReturnCommentResponseDTOWithStatus201() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        CommentResponseDTO responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getEmail()).isEqualTo("valid@email.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("John Doe");
        org.assertj.core.api.Assertions.assertThat(responseBody.getBody()).isEqualTo("A Normal Body");

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments + 1);

    }

    @Test
    public void createComment_WithEmailNull_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO(null, "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithNameNull_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", null, "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithBodyNull_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", "John Doe", null))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }
}
