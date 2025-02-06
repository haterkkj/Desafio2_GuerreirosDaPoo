package uol.compass.microserviceb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.repositories.CommentRepository;
import uol.compass.microserviceb.services.CommentService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;

    private Comment comment;

    @BeforeEach
    void setUp() {
        Post post = new Post();
        post.setId("post123");
        post.setTitle("Title test");
        post.setBody("Body Test");

        comment = new Comment("email@test.com", "Test Name", "Test Body");
        comment.setId("123");
        comment.setPost(post);
    }
    @Test
    void shouldSaveCommentSuccessfully() {
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment savedComment = commentService.save(comment);

        assertNotNull(savedComment);
        assertEquals("123", savedComment.getId());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void shouldThrowExceptionWhenSavingFails() {
        when(commentRepository.save(comment)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> commentService.save(comment));

        assertEquals("Unexpected error occurred while saving the comment.", exception.getMessage());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void shouldFindCommentById() {
        when(commentRepository.findById("123")).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.findById("123");

        assertNotNull(foundComment);
        assertEquals("123", foundComment.getId());
        verify(commentRepository, times(1)).findById("123");
    }
































}
