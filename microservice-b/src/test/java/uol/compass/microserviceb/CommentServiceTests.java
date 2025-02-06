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

































}
