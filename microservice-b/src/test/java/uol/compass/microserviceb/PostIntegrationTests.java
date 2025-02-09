package uol.compass.microserviceb;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.web.dto.PostCreateDTO;
import uol.compass.microserviceb.web.dto.PostResponseDTO;
import uol.compass.microserviceb.web.dto.PostUpdateDTO;
import uol.compass.microserviceb.web.exception.ErrorMessage;

import java.util.*;

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
    public void createPost_WithTitleWithBlankSpaces_ReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("        ", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void createPost_WithBodyWithBlankSpaces_ReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("A Normal Title", "          "))
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
    public void createPost_WithBodyMoreThan2080CharsReturnErrorMessageWithStatus422() {
        String longString = "LongString".repeat(208 + 1); // LongString has 10 Chars; 10*209 = 2090

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

    @Test
    public void getAllPosts_ReturnListOfPostResponseDTOWithStatus200(){
        List<PostResponseDTO> responseBody = testClient
                .get()
                .uri(BASE_URI)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PostResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(PRE_SAVED_POSTS.size());
    }

    @Test
    public void getPostById_WithValidId_ReturnPostResponseDTOWithStatus200(){
        Integer postId = 1;
        Post postData = PRE_SAVED_POSTS.get(postId);

        PostResponseDTO responseBody = testClient
                .get()
                .uri(BASE_URI + "/" + postId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PostResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(postData.getId());
        org.assertj.core.api.Assertions.assertThat(responseBody.getTitle()).isEqualTo(postData.getTitle());
        org.assertj.core.api.Assertions.assertThat(responseBody.getBody()).isEqualTo(postData.getBody());
        org.assertj.core.api.Assertions.assertThat(responseBody.getComments()).isEmpty();
    }

    @Test
    public void getPostById_WithInvalidId_ReturnErrorMessageWithStatus404(){
        ErrorMessage responseBody = testClient
                .get()
                .uri(BASE_URI + "/-1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updatePostById_WithValidIdAndValidData_ReturnPostResponseDTOWithStatus200(){
        Integer postId = 1;
        Post postData = PRE_SAVED_POSTS.get(postId);

        String newTitle = "A Normal Title";
        String newBody = "A Normal Body";

        PostResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostUpdateDTO(newTitle, newBody))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PostResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(postData.getId());
        org.assertj.core.api.Assertions.assertThat(responseBody.getTitle()).isEqualTo(newTitle);
        org.assertj.core.api.Assertions.assertThat(responseBody.getBody()).isEqualTo(newBody);
        org.assertj.core.api.Assertions.assertThat(responseBody.getComments()).isEmpty();
    }

    @Test
    public void updatePostById_WithInvalidIdAndValidData_ReturnErrorMessageStatus404(){
        ErrorMessage responseBody = testClient
                .put()
                .uri(BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostUpdateDTO("A Normal Title", "A Normal Body"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updatePostById_WithValidIdAndTitleNull_ReturnErrorMessageStatus422(){
        ErrorMessage responseBody = testClient
                .put()
                .uri(BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostUpdateDTO(null, "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updatePostById_WithValidIdAndBodyNull_ReturnErrorMessageStatus422(){
        ErrorMessage responseBody = testClient
                .put()
                .uri(BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostUpdateDTO("A Normal Title", null))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updatePostById_WithValidIdAndTitleBlank_ReturnErrorMessageStatus422(){
        ErrorMessage responseBody = testClient
                .put()
                .uri(BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostUpdateDTO("", null))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updatePostById_WithValidIdAndBodyBlank_ReturnErrorMessageStatus422(){
        ErrorMessage responseBody = testClient
                .put()
                .uri(BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostUpdateDTO("A Normal Title", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updatePostById_WithValidIdAndTitleWithLessThan3Chars_ReturnErrorMessageStatus422(){
        ErrorMessage responseBody = testClient
                .put()
                .uri(BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostUpdateDTO("ti", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updatePostById_WithValidIdAndBodyWithLessThan3Chars_ReturnErrorMessageStatus422(){
        ErrorMessage responseBody = testClient
                .put()
                .uri(BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostUpdateDTO("A Normal Title", "bo"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updatePostById_WithValidIdAndTitleWithMoreThan80Chars_ReturnErrorMessageStatus422(){
        String longString = "LongString".repeat(8 + 1); // LongString has 10 Chars; 10*9 = 90

        ErrorMessage responseBody = testClient
                .put()
                .uri(BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostUpdateDTO(longString, "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updatePostById_WithValidIdAndBodyWithMoreThan1080Chars_ReturnErrorMessageStatus422(){
        String longString = "LongString".repeat(208 + 1); // LongString has 10 Chars; 10*209 = 2090

        ErrorMessage responseBody = testClient
                .put()
                .uri(BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO("A Normal Title", longString))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void deletePostById_WithValidId_ReturnNothingWithStatus204(){
        testClient
                .delete()
                .uri(BASE_URI + "/1")
                .exchange()
                .expectStatus().isEqualTo(204);
    }

    @Test
    public void deletePostById_WithInvalidId_ReturnErrorMessageWithStatus404(){
        ErrorMessage responseBody = testClient
                .delete()
                .uri(BASE_URI + "/-1")
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

}
