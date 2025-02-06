package uol.compass.microserviceb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.repositories.CommentRepository;
import uol.compass.microserviceb.services.CommentService;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;

    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment("email@test.com", "Test Name", "Test Body");
        comment.setId("123");
    }

































}
