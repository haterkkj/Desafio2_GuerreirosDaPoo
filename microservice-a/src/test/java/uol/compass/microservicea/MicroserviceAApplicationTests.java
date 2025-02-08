package uol.compass.microservicea;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.services.CommentService;
import uol.compass.microservicea.web.controller.CommentController;

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

}
