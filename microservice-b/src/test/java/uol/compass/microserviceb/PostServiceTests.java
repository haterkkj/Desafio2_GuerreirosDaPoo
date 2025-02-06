package uol.compass.microserviceb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uol.compass.microserviceb.clients.PostClient;
import uol.compass.microserviceb.exceptions.EntityNotFoundException;
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
    public void postService_Shouldcreate_ReturnSuccess() {
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);
        Post savedPost = postService.save(mockPost);
        assertNotNull(savedPost);
        assertEquals("Test for tittle", savedPost.getTitle());
        assertEquals("Test for body", savedPost.getBody());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void postService_ShouldNotSave_WhenRepositoryFails() {
        when(postRepository.save(any(Post.class))).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> postService.save(mockPost));
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void postService_ShouldThrowException_WhenPostIsNull() {
        assertThrows(IllegalArgumentException.class, () -> postService.save(null));

        verify(postRepository, times(0)).save(any(Post.class));
    }

    @Test
    public void postService_ShouldGetAllPosts_ReturnSuccess() {
        List<Post> mockPosts = new ArrayList<>();

        mockPosts.add(new Post("Tittle number 1", "Body number 1"));
        mockPosts.add(new Post("Tittle number 2", "Body number 2"));

        when(postRepository.findAll()).thenReturn(mockPosts);

        List<Post> posts = postService.findAll();

        assertNotNull(posts);
        assertEquals(2, posts.size());

        assertEquals("Tittle number 1", posts.get(0).getTitle());
        assertEquals("Tittle number 2", posts.get(1).getTitle());

        assertEquals("Body number 1", posts.get(0).getBody());
        assertEquals("Body number 2", posts.get(1).getBody());

        verify(postRepository, times(1)).findAll();
    }

    @Test
    public void postService_ShouldGetAllPosts_ReturnEmpty() {

        when(postRepository.findAll()).thenReturn(Collections.emptyList());

        List<Post> posts = postService.findAll();

        assertNotNull(posts);
        assertTrue(posts.isEmpty());

        verify(postRepository, times(1)).findAll();

    }

    @Test
    public void postService_ShouldGetPostById_ReturnSuccess() {
        when(postRepository.findById("1")).thenReturn(Optional.of(mockPost));
        Post foundPostById = postService.findById("1");
        assertNotNull(foundPostById);

        assertEquals("Test for tittle", foundPostById.getTitle());
        assertEquals("Test for body", foundPostById.getBody());
    }

    @Test
    public void postService_ShouldNotGetPostById_ReturnEmpty() {
        when(postRepository.findById("99")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> postService.findById("99"));
    }

}
