package uol.compass.microserviceb.UnitTests.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import uol.compass.microserviceb.web.dto.PostUpdateDTO;

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
        mockPost.setTitle("Test for title");
        mockPost.setBody("Test for body");
    }

    @Test
    public void postService_Shouldcreate_ReturnSuccess() {
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);
        Post savedPost = postService.save(mockPost);
        assertNotNull(savedPost);
        assertEquals("Test for title", savedPost.getTitle());
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

    public void postService_ShouldNotCreate_ReturnsNullWhenPostIsNull() {
        Post savedPost = postService.save(null);

        assertNull(savedPost);

        verify(postRepository, times(0)).save(any(Post.class));
    }

    @Test
    public void postService_ShouldGetAllPosts_ReturnSuccess() {
        List<Post> mockPosts = new ArrayList<>();

        mockPosts.add(new Post("Title number 1", "Body number 1"));
        mockPosts.add(new Post("Title number 2", "Body number 2"));

        when(postRepository.findAll()).thenReturn(mockPosts);

        List<Post> posts = postService.findAll();

        assertNotNull(posts);
        assertEquals(2, posts.size());

        assertEquals("Title number 1", posts.get(0).getTitle());
        assertEquals("Title number 2", posts.get(1).getTitle());

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

        assertEquals("Test for title", foundPostById.getTitle());
        assertEquals("Test for body", foundPostById.getBody());
    }

    @Test
    public void postService_ShouldNotGetPostById_ReturnEmpty() {
        when(postRepository.findById("99")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> postService.findById("99"));
    }

    @Test
    public void postService_ShouldUpdatePostWithDTO_WhenValidDTO() {
        PostUpdateDTO dto = new PostUpdateDTO("New title to test", "New body to test");
        Post existingPost = new Post("Old title to test", "Old body to test");

        when(postRepository.findById("1")).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        Post result = postService.updatePost("1", dto);

        assertNotNull(result);
        assertEquals("New title to test", result.getTitle());
        assertEquals("New body to test", result.getBody());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void postService_ShouldThrowException_WhenPostIdIsBlank() {
        PostUpdateDTO dto = new PostUpdateDTO("New title to test", "New body to test");

        assertThrows(IllegalArgumentException.class, () -> postService.updatePost("", dto));
        verify(postRepository, times(0)).save(any(Post.class));
    }

    @Test
    public void postService_ShouldThrowException_WhenNoFieldsToUpdate() {
        PostUpdateDTO dto = new PostUpdateDTO(null, null);

        assertThrows(IllegalArgumentException.class, () -> postService.updatePost("1", dto));
        verify(postRepository, times(0)).save(any(Post.class));
    }

    @Test
    public void postService_ShouldThrowException_WhenPostNotFoundById() {
        PostUpdateDTO dto = new PostUpdateDTO("New title to test", "New body to test");

        when(postRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> postService.updatePost("1", dto));
        verify(postRepository, times(0)).save(any(Post.class));
    }

    @Test
    void postService_ShouldDeletePost_ReturnSuccess() {
        when(postRepository.existsById("1")).thenReturn(true);
        doNothing().when(postRepository).deleteById("1");

        postService.deletePostById("1");

        verify(postRepository, times(1)).deleteById("1");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPost() {
        when(postRepository.existsById("99")).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> postService.deletePostById("99"));
    }

}