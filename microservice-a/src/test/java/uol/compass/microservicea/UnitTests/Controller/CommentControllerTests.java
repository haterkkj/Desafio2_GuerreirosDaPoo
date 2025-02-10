package uol.compass.microservicea.UnitTests.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.services.CommentService;
import uol.compass.microservicea.web.controller.CommentController;
import uol.compass.microservicea.web.dto.CommentCreateDTO;
import uol.compass.microservicea.web.exception.ApiExceptionHandler;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTests {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private ObjectMapper objectMapper;
    private Comment mockComment;

    private static final String POST_ID = "123";
    private static final String COMMENT_ID = "456";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        Post mockPost = new Post();
        mockPost.setId(POST_ID);

        mockComment = new Comment();
        mockComment.setId(COMMENT_ID);
        mockComment.setEmail("email@test.com");
        mockComment.setName("Test Name");
        mockComment.setBody("Test Body");
    }

    @Test
    void commentService_shouldCreateCommentSuccessfully() throws Exception {
        CommentCreateDTO createDTO = new CommentCreateDTO("email@test.com", "Test Name", "Test Body");
        when(commentService.createCommentInPost(eq(POST_ID), any(CommentCreateDTO.class))).thenReturn(mockComment);

        mockMvc.perform(post("/api/posts/{postId}/comments", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(COMMENT_ID));
    }

    @Test
    void commentService_shouldReturnNotFoundWhenPostDoesNotExist() throws Exception {
        when(commentService.createCommentInPost(eq(POST_ID), any(CommentCreateDTO.class)))
                .thenThrow(new EntityNotFoundException("Post not found"));

        mockMvc.perform(post("/api/posts/{postId}/comments", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentCreateDTO("email@test.com", "Test Name", "Test Body"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void commentService_shouldGetAllCommentsForPost() throws Exception {
        when(commentService.getCommentsByPostId(POST_ID)).thenReturn(List.of(mockComment));

        mockMvc.perform(get("/api/posts/{postId}/comments", POST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(COMMENT_ID));
    }

    @Test
    void commentService_shouldReturnEmptyListWhenNoCommentsExist() throws Exception {
        when(commentService.getCommentsByPostId(POST_ID)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/posts/{postId}/comments", POST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void commentService_shouldReturnNotFoundWhenCommentDoesNotExist() throws Exception {
        when(commentService.getCommentById(POST_ID, COMMENT_ID)).thenThrow(new EntityNotFoundException("Comment not found"));

        mockMvc.perform(get("/api/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID))
                .andExpect(status().isNotFound());
    }
}
