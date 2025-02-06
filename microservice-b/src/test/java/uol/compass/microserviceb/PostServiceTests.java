package uol.compass.microserviceb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uol.compass.microserviceb.clients.PostClient;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.repositories.PostRepository;
import uol.compass.microserviceb.services.PostService;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostClient postClient;

    private Post mockPost;

    @BeforeEach
    void setUp() {
        mockPost = new Post();
        mockPost.setId("1");
        mockPost.setTitle("Test for tittle");
        mockPost.setBody("Test for body");
    }

    @Test
    public void postService_ShouldcreateSuccessfully() {
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);
        Post savedPost = postService.save(mockPost);
        assertNotNull(savedPost);
        assertEquals("Test for tittle", savedPost.getTitle());
        assertEquals("Test for body", savedPost.getBody());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void postService_ShoultNotSave_WhenRepositoryFails() {
        when(postRepository.save(any(Post.class))).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> postService.save(mockPost));
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void postService_ShouldNotCreateSuccessfully() {
        Post savedPost = postService.save(null);
        assertNull(savedPost);
        verify(postRepository, times(0)).save(any(Post.class));
    }

}
