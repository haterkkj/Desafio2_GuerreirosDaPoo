package uol.compass.microserviceb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import uol.compass.microserviceb.web.dto.PostCreateDTO;
import uol.compass.microserviceb.web.dto.PostResponseDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql(scripts = "/sql/posts/posts-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = "/sql/posts/posts-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PostIntegrationTests {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createPost_WithValidData_ReturnPostResponseDTOWithStatus201() {
        PostResponseDTO responseBody = testClient
                .post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("NewPost", "Hello World!"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PostResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getTitle()).isEqualTo("NewPost");
        org.assertj.core.api.Assertions.assertThat(responseBody.getBody()).isEqualTo("Hello World!");
        org.assertj.core.api.Assertions.assertThat(responseBody.getComments()).isEmpty();
    }

}
