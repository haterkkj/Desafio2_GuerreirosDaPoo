package uol.compass.microservicea.IntegrationTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import uol.compass.microservicea.model.Post;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostIntegrationTests {

    @Autowired
    private WebTestClient testClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final String BASE_URI = "api/posts";

    // The key value need to be the same that the Post ID.
    private final Map<Integer, Post> PRE_SAVED_POSTS = Map.of(
            1, new Post("1", "Título do Post 1", "Conteúdo do Post 1"),
            2, new Post("2", "Título do Post 2", "Conteúdo do Post 2")
    );


    @BeforeEach
    public void insertTestPosts() {
        PRE_SAVED_POSTS.forEach(
                (key, post) -> mongoTemplate.save(post)
        );
    }

    @AfterEach
    public void cleanUp() {
        mongoTemplate.dropCollection(Post.class);
    }

    // TODO: Testes de Integração de Posts do MicroService A.
    // Vai ser basicamente só copiar o outro rsrs

}
