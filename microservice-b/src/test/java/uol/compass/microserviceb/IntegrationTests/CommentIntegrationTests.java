package uol.compass.microserviceb.IntegrationTests;

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

import static uol.compass.microserviceb.utils.IntegrationTestsUtils.PRE_SAVED_COMMENTS;
import static uol.compass.microserviceb.utils.IntegrationTestsUtils.PRE_SAVED_POSTS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentIntegrationTests {

    @Autowired
    private WebTestClient testClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final String BASE_URI = "api/posts";

    @BeforeEach
    public void insertTestPostsAndComments() {
        Collections.addAll(PRE_SAVED_POSTS.get(1).getComments(),
                PRE_SAVED_COMMENTS.get(1),
                PRE_SAVED_COMMENTS.get(2)
        );

        Collections.addAll(PRE_SAVED_POSTS.get(2).getComments(),
                PRE_SAVED_COMMENTS.get(3)
        );

        PRE_SAVED_POSTS.forEach(
                (key, post) -> mongoTemplate.save(post)
        );

        PRE_SAVED_COMMENTS.forEach(
                (key, comment) -> mongoTemplate.save(comment)
        );
    }

    @AfterEach
    public void cleanUp() {
        mongoTemplate.dropCollection(Post.class);
        mongoTemplate.dropCollection(Comment.class);
    }

    @Test
    public void createComment_WithValidData_ReturnCommentResponseDTOWithStatus201() {
        Integer postId = 1;
        int oldNumberOfComments = PRE_SAVED_POSTS.get(postId).getComments().size();

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

        Integer newNumberOfComments = mongoTemplate.findById(String.valueOf(postId), Post.class).getComments().size();
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

    @Test
    public void createComment_WithEmailWithBlankSpaces_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("          ", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithNameWithBlankSpaces_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", "           ", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithBodyWithBlankSpaces_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", "John Doe", "        "))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithEmailBlank_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithNameBlank_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", "", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithBodyBlank_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", "John Doe", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithEmailWithoutAt_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("thereisntanyat.com", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithEmailWithoutTopLevelDomain_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("invalid@email", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithEmailWithTopLevelDomainWithLessThan2Chars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("invalid@email.x", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithEmailWithInvalidChar_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("invalid!@email.com", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithEmailWithDotAtStart_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO(".invalid@email.com", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithEmailWithDotAtEnd_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("invalid@email.com.", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithEmailWithMoreThanOneAt_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("invalid@@email.com", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithEmailWithBlankChars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("invalid @email.com", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithEmailDotsOnARow_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("in..valid@email.com", "John Doe", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithNameWithLessThan2Chars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", "n", "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithBodyWithLessThan3Chars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", "John Doe", "bo"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithNameWithMoreThan180Chars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        String longString = "LongString".repeat(18 + 1); // LongString has 10 Chars; 10*19 = 190
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", longString, "A Normal Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void createComment_WithBodyWithMoreThan1080Chars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        String longString = "LongString".repeat(108 + 1); // LongString has 10 Chars; 10*109 = 1090
        Integer oldNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();

        ErrorMessage responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO("valid@email.com", "John Doe", longString))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        Integer newNumberOfComments = mongoTemplate.findById(postId, Post.class).getComments().size();
        org.assertj.core.api.Assertions.assertThat(newNumberOfComments).isEqualTo(oldNumberOfComments);
    }

    @Test
    public void getAllComments_ReturnListOfCommentsResponseDTOWithStatus200(){
        Integer postId = 1;
        Integer postCommentsSize = PRE_SAVED_POSTS.get(postId).getComments().size();

        List<CommentResponseDTO> responseBody = testClient
                .get()
                .uri(BASE_URI + "/" + postId + "/comments")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(postCommentsSize);
    }

    @Test
    public void getCommentById_WithValidId_ReturnCommentResponseDTOWithStatus200(){
        int postId = 1;
        Integer commentId = 1;
        Comment commentData = PRE_SAVED_COMMENTS.get(commentId);

        CommentResponseDTO responseBody = testClient
                .get()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(commentData.getId());
        org.assertj.core.api.Assertions.assertThat(responseBody.getEmail()).isEqualTo(commentData.getEmail());
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo(commentData.getName());
        org.assertj.core.api.Assertions.assertThat(responseBody.getBody()).isEqualTo(commentData.getBody());
    }

    @Test
    public void getCommentById_WithInvalidPostId_ReturnErrorMessageWithStatus404(){
        int postId = -1;
        int commentId = 1;

        ErrorMessage responseBody = testClient
                .get()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void getCommentById_WithInvalidCommentId_ReturnErrorMessageWithStatus404(){
        int postId = 1;
        int commentId = -1;

        ErrorMessage responseBody = testClient
                .get()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updateCommentById_WithValidIdAndNameNull_ReturnErrorMessageWithStatus422(){
        int postId = 1;
        int commentId = 1;

        CommentResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentUpdateDTO(null, "A Normal New Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updateCommentById_WithValidIdAndBodyNull_ReturnErrorMessageWithStatus422(){
        int postId = 1;
        int commentId = 1;

        CommentResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentUpdateDTO("Mary Doe", null))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updateCommentById_WithValidIdAndNameBlank_ReturnErrorMessageWithStatus422(){
        int postId = 1;
        int commentId = 1;

        CommentResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentUpdateDTO("", "A Normal New Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updateCommentById_WithValidIdAndBodyBlank_ReturnErrorMessageWithStatus422(){
        int postId = 1;
        int commentId = 1;

        CommentResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentUpdateDTO("Mary Doe", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updateCommentById_WithValidIdAndNameWithBlankSpaces_ReturnErrorMessageWithStatus422(){
        int postId = 1;
        int commentId = 1;

        CommentResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentUpdateDTO("           ", "A Normal New Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updateCommentById_WithValidIdAndBodyWithBlankSpaces_ReturnErrorMessageWithStatus422(){
        int postId = 1;
        int commentId = 1;

        CommentResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentUpdateDTO("Mary Doe", "       "))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updateCommentById_WithNameWithLessThan2Chars_ReturnErrorMessageWithStatus422(){
        int postId = 1;
        int commentId = 1;

        CommentResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentUpdateDTO("n", "A Normal New Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updateCommentById_WithBodyWithLessThan3Chars_ReturnErrorMessageWithStatus422(){
        int postId = 1;
        int commentId = 1;

        CommentResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentUpdateDTO("Mary Doe", "bo"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updateCommentById_WithNameWithMoreThan180Chars_ReturnErrorMessageWithStatus422(){
        int postId = 1;
        int commentId = 1;
        String longString = "LongString".repeat(18 + 1); // LongString has 10 Chars; 10*19 = 190

        CommentResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentUpdateDTO(longString, "A Normal New Body"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void updateCommentById_WithBodyWithMoreThan1080Chars_ReturnErrorMessageWithStatus422(){
        int postId = 1;
        int commentId = 1;
        String longString = "LongString".repeat(108 + 1); // LongString has 10 Chars; 10*109 = 1090

        CommentResponseDTO responseBody = testClient
                .put()
                .uri(BASE_URI + "/" + postId + "/comments" + "/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentUpdateDTO("Mary Doe", longString))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void deleteCommentById_WithValidId_ReturnNothingWithStatus204(){
        int postId = 1;
        testClient
                .delete()
                .uri(BASE_URI + "/" + postId + "/comments/1")
                .exchange()
                .expectStatus().isEqualTo(204);
    }

    @Test
    public void deleteCommentById_WithInvalidPostId_ReturnErrorMessageWithStatus404(){
        ErrorMessage responseBody = testClient
                .delete()
                .uri(BASE_URI + "/-1/comments/1")
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void deleteCommentById_WithInvalidCommentId_ReturnErrorMessageWithStatus404(){
        ErrorMessage responseBody = testClient
                .delete()
                .uri(BASE_URI + "/1/comments/-1")
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

}
