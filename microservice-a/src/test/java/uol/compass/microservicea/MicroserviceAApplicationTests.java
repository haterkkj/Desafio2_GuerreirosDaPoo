package uol.compass.microservicea;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import uol.compass.microservicea.clients.CommentClient;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.services.CommentService;
import uol.compass.microservicea.web.dto.CommentUpdateDTO;

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
    private CommentService commentService;

    @Mock
    private CommentClient commentClient;

    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment("123", "email@test.com", "Test Name", "Test Body");
    }

    @Test
    void should_Update_Comment_Successfully() {
        String postId = "post123";
        String commentId = "comment123";

        CommentUpdateDTO updateDTO = new CommentUpdateDTO("post123", "post123");
        updateDTO.setBody("Updated body");

        Comment updatedComment = new Comment();
        updatedComment.setId(commentId);
        updatedComment.setBody("Updated body");

        when(commentClient.updateCommentInPost(postId, commentId, updateDTO)).thenReturn(updatedComment);

        Comment result = commentService.updateCommentInPost(postId, commentId, updateDTO);

        assertNotNull(result, "O comentário atualizado não deveria ser nulo.");
        assertEquals("Updated body", result.getBody(), "O corpo do comentário não foi atualizado corretamente.");

        verify(commentClient, times(1)).updateCommentInPost(postId, commentId, updateDTO);
    }


    @Test
    void should_ThrowException_When_Update_Fails() {
        CommentUpdateDTO updateDTO = new CommentUpdateDTO("Updated Name", "Updated Body");

        when(commentClient.updateCommentInPost("post123", "999", updateDTO)).thenThrow(new EntityNotFoundException("Comment not found"));

        assertThrows(EntityNotFoundException.class, () -> commentService.updateCommentInPost("post123", "999", updateDTO));
        verify(commentClient, times(1)).updateCommentInPost("post123", "999", updateDTO);
    }

    @Test
    void should_Delete_Comment_ById() {
        doNothing().when(commentClient).deleteCommentInPost("post123", "123");

        commentService.deleteCommentInPost("post123", "123");

        verify(commentClient, times(1)).deleteCommentInPost("post123", "123");
    }

    @Test
    void should_ThrowException_When_Deleting_NonExistent_Comment() {
        doThrow(new EntityNotFoundException("Comment not found"))
                .when(commentClient).deleteCommentInPost("post123", "999");

        assertThrows(EntityNotFoundException.class, () -> commentService.deleteCommentInPost("post123", "999"));
        verify(commentClient, times(1)).deleteCommentInPost("post123", "999");
    }

    @Test
    void should_Get_Comments_By_PostId() {
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment("1", "email1@test.com", "User 1", "Comment body 1"));
        mockComments.add(new Comment("2", "email2@test.com", "User 2", "Comment body 2"));

        when(commentClient.getCommentsByPostId("post123")).thenReturn(mockComments);

        List<Comment> comments = commentService.getCommentsByPostId("post123");

        assertNotNull(comments);
        assertEquals(2, comments.size());
        assertEquals("User 1", comments.get(0).getName());
        assertEquals("User 2", comments.get(1).getName());
        verify(commentClient, times(1)).getCommentsByPostId("post123");
    }
}
