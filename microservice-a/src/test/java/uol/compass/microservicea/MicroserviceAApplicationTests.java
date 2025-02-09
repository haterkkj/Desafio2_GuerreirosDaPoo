package uol.compass.microservicea;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import uol.compass.microservicea.clients.CommentClient;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.services.CommentService;
import uol.compass.microservicea.web.dto.CommentCreateDTO;
import uol.compass.microservicea.web.dto.CommentResponseDTO;
import uol.compass.microservicea.web.dto.CommentUpdateDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest Esse import quebra os testes.
class MicroserviceAApplicationTests {

    @Mock
    private CommentClient commentClient;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private CommentResponseDTO responseDTO;
    private CommentCreateDTO createDTO;
    private CommentUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        comment = new Comment("1", "test@example.com", "Test User", "This is a test comment.");
        responseDTO = new CommentResponseDTO("1", "test@example.com", "Test User", "This is a test comment.");
        createDTO = new CommentCreateDTO("test@example.com", "Test User", "This is a test comment.");
        updateDTO = new CommentUpdateDTO("Updated User", "This is an updated comment.");
    }

    @Test
    void should_Create_Comment_In_Post() {
        when(commentClient.createCommentInPost("1", createDTO)).thenReturn(comment);

        Comment createdComment = commentService.createCommentInPost("1", createDTO);

        assertNotNull(createdComment);
        assertEquals(comment.getId(), createdComment.getId());
        verify(commentClient, times(1)).createCommentInPost("1", createDTO);
    }

    @Test
    void should_Get_Comments_By_Post_Id() {
        when(commentClient.getCommentsByPostId("1")).thenReturn(List.of(comment));

        List<Comment> comments = commentService.getCommentsByPostId("1");

        assertNotNull(comments);
        assertFalse(comments.isEmpty());
        assertEquals(1, comments.size());
        verify(commentClient, times(1)).getCommentsByPostId("1");
    }

    @Test
    void should_Update_Comment_In_Post() {
        when(commentClient.updateCommentInPost("1", "1", updateDTO)).thenReturn(comment);

        Comment updatedComment = commentService.updateCommentInPost("1", "1", updateDTO);

        assertNotNull(updatedComment);
        verify(commentClient, times(1)).updateCommentInPost("1", "1", updateDTO);
    }

    @Test
    void should_Delete_Comment_In_Post() {
        doNothing().when(commentClient).deleteCommentInPost("1", "1");

        assertDoesNotThrow(() -> commentService.deleteCommentInPost("1", "1"));
        verify(commentClient, times(1)).deleteCommentInPost("1", "1");
    }
}
