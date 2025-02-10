package uol.compass.microserviceb.IntegrationTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.web.dto.PostResponseDTO;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SyncIntegrationTests {

    @Autowired
    private WebTestClient testClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void cleanUpBefore() {
        mongoTemplate.dropCollection(Post.class);
    }

    @AfterEach
    public void cleanUpAfter() {
        mongoTemplate.dropCollection(Post.class);
    }

    @Test
    public void createPost_WithValidData_ReturnPostResponseDTOWithStatus201() {
        List<PostResponseDTO> responseBody = testClient
                .post()
                .uri("api/sync")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PostResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(100);
    }
}