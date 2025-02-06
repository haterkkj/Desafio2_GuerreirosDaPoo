package uol.compass.microserviceb;

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
        post.setId("123456");
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

    @Test
    void shouldThrowExceptionWhenCommentNotFound() {
        when(commentRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.findById("999"));
        verify(commentRepository, times(1)).findById("999");
    }

    @Test
    void shouldDeleteCommentById() {
        when(commentRepository.existsById("123")).thenReturn(true);
        doNothing().when(commentRepository).deleteById("123");

        commentService.deleteById("123");

        verify(commentRepository, times(1)).deleteById("123");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentComment() {
        when(commentRepository.existsById("999")).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> commentService.deleteById("999"));
        verify(commentRepository, never()).deleteById("999");
    }






























}
