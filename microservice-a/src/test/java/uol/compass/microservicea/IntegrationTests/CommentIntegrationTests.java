package uol.compass.microservicea.IntegrationTests;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.BindingResult;
import uol.compass.microservicea.clients.CommentClient;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.exceptions.MethodArgumentNotValidException;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.web.dto.CommentCreateDTO;
import uol.compass.microservicea.web.dto.CommentResponseDTO;
import uol.compass.microservicea.web.dto.CommentUpdateDTO;
import uol.compass.microservicea.web.exception.ErrorMessage;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static uol.compass.microservicea.utils.IntegrationTestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentIntegrationTests {

    @Autowired
    private WebTestClient testClient;

    @MockitoBean
    private CommentClient commentClient;

    @Mock
    private BindingResult bindingResult;

    private final String BASE_URI = "api/posts";

    @Test
    public void createComment_WithValidData_ReturnCommentResponseDTOWithStatus201() {
        int postId = 1;
        Comment comment = PRE_SAVED_COMMENTS.get(0);
        when(commentClient.createCommentInPost(any(), any())).thenReturn(comment);

        CommentResponseDTO responseBody = testClient
                .post()
                .uri(BASE_URI + "/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommentCreateDTO(comment.getEmail(), comment.getName(), comment.getBody()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getEmail()).isEqualTo(comment.getEmail());
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo(comment.getName());
        org.assertj.core.api.Assertions.assertThat(responseBody.getBody()).isEqualTo(comment.getBody());
    }

    @Test
    public void createComment_WithEmailNull_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
    }

    @Test
    public void createComment_WithNameNull_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithBodyNull_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailWithBlankSpaces_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithNameWithBlankSpaces_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithBodyWithBlankSpaces_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailBlank_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithNameBlank_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithBodyBlank_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailWithoutAt_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailWithoutTopLevelDomain_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailWithTopLevelDomainWithLessThan2Chars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailWithInvalidChar_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailWithDotAtStart_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailWithDotAtEnd_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailWithMoreThanOneAt_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailWithBlankChars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithEmailDotsOnARow_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithNameWithLessThan2Chars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithBodyWithLessThan3Chars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithNameWithMoreThan180Chars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        String longString = "LongString".repeat(18 + 1); // LongString has 10 Chars; 10*19 = 190
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void createComment_WithBodyWithMoreThan1080Chars_ReturnErrorMessageWithStatus422() {
        String postId = "1";
        String longString = "LongString".repeat(108 + 1); // LongString has 10 Chars; 10*109 = 1090
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.createCommentInPost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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


    }

    @Test
    public void getAllComments_ReturnListOfCommentsResponseDTOWithStatus200(){
        int postId = 1;
        List<Comment> postComments = PRE_SAVED_POSTS.get(postId).getComments();
        Mockito.when(commentClient.getCommentsByPostId(any())).thenReturn(postComments);

        List<CommentResponseDTO> responseBody = testClient
                .get()
                .uri(BASE_URI + "/" + postId + "/comments")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(postComments.size());
    }

    @Test
    public void getCommentById_WithValidId_ReturnCommentResponseDTOWithStatus200(){
        int postId = 1;
        int commentId = 1;
        Comment commentData = PRE_SAVED_COMMENTS.get(commentId);
        Mockito.when(commentClient.getCommentById(any(), any())).thenReturn(commentData);

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
        when(commentClient.getCommentById(any(), any())).thenThrow(
                new EntityNotFoundException("Not Found")
        );

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
        when(commentClient.getCommentById(any(), any())).thenThrow(
                new EntityNotFoundException("Not Found")
        );

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
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.updateCommentInPost(any(), any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.updateCommentInPost(any(), any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.updateCommentInPost(any(), any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.updateCommentInPost(any(), any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.updateCommentInPost(any(), any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.updateCommentInPost(any(), any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.updateCommentInPost(any(), any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.updateCommentInPost(any(), any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.updateCommentInPost(any(), any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);
        when(commentClient.updateCommentInPost(any(), any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        doNothing().when(commentClient).deleteCommentInPost(any(), any());
        testClient
                .delete()
                .uri(BASE_URI + "/" + postId + "/comments/1")
                .exchange()
                .expectStatus().isEqualTo(204);
    }

    @Test
    public void deleteCommentById_WithInvalidPostId_ReturnErrorMessageWithStatus404(){
        doThrow(new EntityNotFoundException("Post not found"))
                .when(commentClient).deleteCommentInPost(any(), any());

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
        doThrow(new EntityNotFoundException("Post not found"))
                .when(commentClient).deleteCommentInPost(any(), any());

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
