package uol.compass.microservicea;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.services.CommentService;
import uol.compass.microservicea.web.controller.CommentController;
import uol.compass.microservicea.web.dto.CommentResponseDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class MicroserviceAApplicationTests {

    @Test
    void contextLoads() {
    }

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment("123", "email@test.com", "Test Name", "Test Body");
    }

    @Test
    void should_GetCommentsByPostId_Successfully() {
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentService.getCommentsByPostId("post123")).thenReturn(comments);

        ResponseEntity<List<CommentResponseDTO>> response = commentController.getPosts("post123");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(commentService, times(1)).getCommentsByPostId("post123");
    }
}
