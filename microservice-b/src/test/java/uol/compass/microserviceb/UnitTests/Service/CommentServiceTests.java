package uol.compass.microserviceb.UnitTests.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uol.compass.microserviceb.exceptions.EntityNotFoundException;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.repositories.CommentRepository;
import uol.compass.microserviceb.repositories.PostRepository;
import uol.compass.microserviceb.services.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    private Comment comment;

    @BeforeEach
    void setUp() {
        Post post = new Post();
        post.setId("123456");
        post.setTitle("Title test");
        post.setBody("Body Test");

        comment = new Comment("email@test.com", "Test Name", "Test Body");
        comment.setId("123");
        comment.setPost(post);
    }

    @Test
    void should_Save_Comment_Successfully() {
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment savedComment = commentService.save(comment);

        assertNotNull(savedComment);
        assertEquals("123", savedComment.getId());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void should_ThrowException_When_Saving_Fails() {
        when(commentRepository.save(comment)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> commentService.save(comment));

        assertEquals("Unexpected error occurred while saving the comment.", exception.getMessage());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void should_Find_CommentById() {
        when(commentRepository.findById("123")).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.findById("123");

        assertNotNull(foundComment);
        assertEquals("123", foundComment.getId());
        verify(commentRepository, times(1)).findById("123");
    }

    @Test
    void should_ThrowException_When_Comment_Not_Found() {
        when(commentRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.findById("999"));
        verify(commentRepository, times(1)).findById("999");
    }

    @Test
    void should_Delete_Comment_ById() {
        String postId = "123456";
        String commentId = "123";

        when(commentRepository.existsById(commentId)).thenReturn(true);
        when(postRepository.findById(postId)).thenReturn(Optional.of(comment.getPost()));

        commentService.deleteById(postId, commentId);

        verify(commentRepository, times(1)).existsById(commentId);
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(any(Post.class));
        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void should_ThrowException_When_Deleting_No_Existent_Comment() {
        String postId = "123456";
        String commentId = "999";

        when(commentRepository.existsById(commentId)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () ->
                commentService.deleteById(postId, commentId)
        );

        assertEquals("Comment with ID 999 not found.",
                exception.getMessage());

        verify(commentRepository, times(1)).existsById(commentId);
        verify(postRepository, never()).findById(anyString());
        verify(postRepository, never()).save(any(Post.class));
        verify(commentRepository, never()).deleteById(anyString());
    }

    @Test
    void should_ThrowException_When_Post_Not_Found_On_Delete() {
        String postId = "999";
        String commentId = "123";

        when(commentRepository.existsById(commentId)).thenReturn(true);
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                commentService.deleteById(postId, commentId)
        );

        assertEquals("Post with ID 999 not found.",
                exception.getMessage());

        verify(commentRepository, times(1)).existsById(commentId);
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
        verify(commentRepository, never()).deleteById(anyString());
    }

    @Test
    void should_Update_Comment_Successfully() {
        comment.setId("123");

        when(commentRepository.existsById("123")).thenReturn(true);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment updatedComment = commentService.update(comment);

        assertNotNull(updatedComment);
        assertEquals("123", updatedComment.getId());

        verify(commentRepository, times(1)).existsById("123");
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    public void commentService_Should_GetCommentById_Return_Success() {
        when(commentRepository.findById("1")).thenReturn(Optional.of(comment));

        Comment foundCommentById = commentService.findById("1");

        assertNotNull(foundCommentById);
        assertEquals("email@test.com", foundCommentById.getEmail());
        assertEquals("Test Name", foundCommentById.getName());
        assertEquals("Test Body", foundCommentById.getBody());

        verify(commentRepository, times(1)).findById("1");
    }

    //Não sei se seguiu o padrão pedido
    @Test
    public void commentService_ShouldGetCommentsByPostId_ReturnSuccess() {
        Post post = new Post();
        post.setId("post123");
        post.setTitle("Test Post");
        post.setBody("Post Body");

        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment("email1@test.com", "User 1", "Comment body 1"));
        mockComments.add(new Comment("email2@test.com", "User 2", "Comment body 2"));

        mockComments.forEach(comment -> comment.setPost(post));

        when(commentRepository.findByPostId("post123")).thenReturn(mockComments);

        List<Comment> comments = commentService.findByPostId("post123");

        assertNotNull(comments);
        assertEquals(2, comments.size());

        assertEquals("email1@test.com", comments.get(0).getEmail());
        assertEquals("User 1", comments.get(0).getName());
        assertEquals("Comment body 1", comments.get(0).getBody());

        assertEquals("email2@test.com", comments.get(1).getEmail());
        assertEquals("User 2", comments.get(1).getName());
        assertEquals("Comment body 2", comments.get(1).getBody());

        verify(commentRepository, times(1)).findByPostId("post123");
    }

}
