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
import uol.compass.microservicea.clients.PostClient;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.exceptions.MethodArgumentNotValidException;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostResponseDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;
import uol.compass.microservicea.web.exception.ErrorMessage;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static uol.compass.microservicea.utils.IntegrationTestUtils.PRE_SAVED_POSTS;
import static uol.compass.microservicea.utils.IntegrationTestUtils.mockBindingResultWithErrors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostIntegrationTests {

    @Autowired
    private WebTestClient testClient;

    @MockitoBean
    private PostClient postClient;

    @Mock
    private BindingResult bindingResult;

    private final String BASE_URI = "api/posts";

    @Test
    public void createPost_WithValidData_ReturnPostResponseDTOWithStatus201() {
        Post post = PRE_SAVED_POSTS.get(0);
        when(postClient.createPost(any())).thenReturn(post);

        PostResponseDTO responseBody = testClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PostCreateDTO(post.getTitle(), post.getBody()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PostResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getTitle()).isEqualTo(post.getTitle());
        org.assertj.core.api.Assertions.assertThat(responseBody.getBody()).isEqualTo(post.getBody());
        org.assertj.core.api.Assertions.assertThat(responseBody.getComments()).isEmpty();
    }

    @Test
    public void createPost_WithTitleNull_ReturnErrorMessageWithStatus422() {
        mockBindingResultWithErrors(bindingResult);

        when(postClient.createPost(any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.createPost(any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.createPost(any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.createPost(any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.createPost(any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.createPost(any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.createPost(any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.createPost(any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.createPost(any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.createPost(any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        Mockito.when(postClient.getPosts()).thenReturn(PRE_SAVED_POSTS);

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
        int postId = 1;
        Post postData = PRE_SAVED_POSTS.get(postId);
        Mockito.when(postClient.getPostById(any())).thenReturn(postData);

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
        when(postClient.getPostById(any())).thenThrow(
                new EntityNotFoundException("Not Found")
        );
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
        int postId = 1;
        Post postData = PRE_SAVED_POSTS.get(postId);
        String newTitle = "A Normal Title";
        String newBody = "A Normal Body";

        postData.setTitle(newTitle);
        postData.setBody(newBody);

        Mockito.when(postClient.updatePost(any(), any())).thenReturn(postData);

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
        when(postClient.updatePost(any(), any())).thenThrow(
                new EntityNotFoundException("Not Found")
        );
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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.updatePost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.updatePost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.updatePost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.updatePost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.updatePost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.updatePost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.updatePost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        mockBindingResultWithErrors(bindingResult);

        when(postClient.updatePost(any(), any())).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                )
        );

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
        doNothing().when(postClient).deletePost(any());
        testClient
                .delete()
                .uri(BASE_URI + "/1")
                .exchange()
                .expectStatus().isEqualTo(204);
    }

    @Test
    public void deletePostById_WithInvalidId_ReturnErrorMessageWithStatus404(){
        doThrow(new EntityNotFoundException("Post not found"))
                .when(postClient).deletePost(any());

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
