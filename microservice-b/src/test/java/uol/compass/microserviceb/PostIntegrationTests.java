package uol.compass.microserviceb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import uol.compass.microserviceb.web.dto.PostCreateDTO;
import uol.compass.microserviceb.web.dto.PostResponseDTO;
import uol.compass.microserviceb.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql(scripts = "/sql/posts/posts-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = "/sql/posts/posts-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PostIntegrationTests {

    @Autowired
    WebTestClient testClient;

    private final String BASE_URI = "api/posts";

    @Test
    public void createPost_WithValidData_ReturnPostResponseDTOWithStatus201() {
        PostResponseDTO responseBody = testClient
                .post()
                .uri(BASE_URI)
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

    @Test
    public void createPost_WithEveryDataNull_ReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO(null, null))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void createPost_WithTitleNull_ReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO(null, "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void createPost_WithBodyNull_ReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("A Normal Title", null))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void createPost_WithEveryDataBlank_ReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void createPost_WithTitleBlank_ReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void createPost_WithBodyBlank_ReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("A Normal Title", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void createPost_WithTitleLessThan3CharsReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("ti", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void createPost_WithBodyLessThan3CharsReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("A Normal Title", "bo"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void createPost_WithTitleMoreThan80CharsReturnErrorMessageWithStatus422() {
        String longString = "LongString".repeat(8 + 1); // LongString has 10 Chars; 10*9 = 90

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO(longString, "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void createPost_WithBodyMoreThan1080CharsReturnErrorMessageWithStatus422() {
        String longString = "LongString".repeat(108 + 1); // LongString has 10 Chars; 10*109 = 1090

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("A Normal Title", longString))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

}
