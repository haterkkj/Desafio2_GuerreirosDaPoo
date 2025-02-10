package uol.compass.microserviceb.UnitTests.Controller;

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
import uol.compass.microserviceb.exceptions.EntityNotFoundException;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.CommentService;
import uol.compass.microserviceb.services.PostService;
import uol.compass.microserviceb.web.controller.CommentController;
import uol.compass.microserviceb.web.dto.CommentCreateDTO;
import uol.compass.microserviceb.web.dto.CommentResponseDTO;
import uol.compass.microserviceb.web.exception.ApiExceptionHandler;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTests {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentController commentController;

    private ObjectMapper objectMapper;

    private Comment mockComment;
    private Post mockPost;

    private static final String POST_ID = "123";
    private static final String COMMENT_ID = "1";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        mockPost = mock(Post.class);
        mockComment = new Comment();
        mockComment.setId(COMMENT_ID);
        mockComment.setEmail("email@test.com");
        mockComment.setName("Test Name");
        mockComment.setBody("Test Body");
    }


    @Test
    void shouldCreateCommentSuccessfully() throws Exception {
        CommentCreateDTO createDTO = new CommentCreateDTO("email@test.com", "Test Name", "Test Body");
        when(postService.findById(POST_ID)).thenReturn(mockPost);
        when(commentService.save(any(Comment.class))).thenReturn(mockComment);

        mockMvc.perform(post("/api/posts/{postId}/comments", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(COMMENT_ID));
    }

    @Test
    void shouldReturnNotFoundWhenPostDoesNotExist() throws Exception {
        when(postService.findById(POST_ID)).thenThrow(new EntityNotFoundException("Post not found"));

        mockMvc.perform(post("/api/posts/{postId}/comments", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentCreateDTO("email@test.com", "Test Name", "Test Body"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Post not found"));
    }

    @Test
    void shouldGetAllCommentsForPost() throws Exception {
        CommentResponseDTO commentResponseDTO = CommentResponseDTO.toDto(mockComment);

        when(postService.findById(POST_ID)).thenReturn(mockPost);

        when(mockPost.getComments()).thenReturn(List.of(mockComment));

        mockMvc.perform(get("/api/posts/{postId}/comments", POST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(commentResponseDTO.getId()))
                .andExpect(jsonPath("$[0].email").value(commentResponseDTO.getEmail()))
                .andExpect(jsonPath("$[0].name").value(commentResponseDTO.getName()))
                .andExpect(jsonPath("$[0].body").value(commentResponseDTO.getBody()));
    }

    @Test
    void shouldReturnNotFoundWhenPostHasNoComments() throws Exception {
        when(postService.findById(POST_ID)).thenReturn(mockPost);

        when(mockPost.getComments()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/posts/{postId}/comments", POST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldDeleteCommentSuccessfully() throws Exception {
        doNothing().when(commentService).deleteById(POST_ID, COMMENT_ID);

        mockMvc.perform(delete("/api/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentComment() throws Exception {
        doThrow(new EntityNotFoundException("Comment not found"))
                .when(commentService).deleteById(POST_ID, COMMENT_ID);

        mockMvc.perform(delete("/api/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Comment not found"));
    }
}