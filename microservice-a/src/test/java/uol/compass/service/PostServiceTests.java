package uol.compass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import uol.compass.microservicea.clients.PostClient;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.services.PostService;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostClient postClient;

    private Post mockPost;

    @BeforeEach
    void setUp() {
        mockPost = new Post();
        mockPost.setId("1");
        mockPost.setTitle("Test Title");
        mockPost.setBody("Test Body");
    }

    @Test
    public void postService_ShouldCreatePost_ReturnSuccess() {
        PostCreateDTO createDTO = new PostCreateDTO("Test Title", "Test Body");
        when(postClient.createPost(any(PostCreateDTO.class))).thenReturn(mockPost);

        Post createdPost = postService.createPost(createDTO);

        assertNotNull(createdPost);
        assertEquals("Test Title", createdPost.getTitle());
        verify(postClient, times(1)).createPost(any(PostCreateDTO.class));
    }

    @Test
    public void postService_ShouldThrowEntityNotFoundException_WhenPostClientReturnsNull() {
        PostCreateDTO createDTO = new PostCreateDTO("Valid Title", "Valid Body");

        when(postClient.createPost(any(PostCreateDTO.class))).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            postService.createPost(createDTO);
        });

        assertEquals("Failed to create post", exception.getMessage());
        verify(postClient, times(1)).createPost(any(PostCreateDTO.class));
    }

    @Test
    public void postService_ShouldPropagateException_WhenPostClientThrowsBadRequest() {
        PostCreateDTO createDTO = new PostCreateDTO("", "");

        when(postClient.createPost(any(PostCreateDTO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            postService.createPost(createDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(postClient, times(1)).createPost(any(PostCreateDTO.class));
    }

    @Test
    public void postService_ShouldGetPostById_ReturnSuccess() {
        when(postClient.getPostById("1")).thenReturn(mockPost);

        Post post = postService.getPostById("1");

        assertNotNull(post);
        assertEquals("Test Title", post.getTitle());
        verify(postClient, times(1)).getPostById("1");
    }

    @Test
    public void postService_ShouldThrowEntityNotFoundException_WhenPostNotFound() {
        when(postClient.getPostById("1"))
                .thenThrow(new EntityNotFoundException("Post not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            postService.getPostById("1");
        });

        assertEquals("Post not found", exception.getMessage());
        verify(postClient, times(1)).getPostById("1");
    }

    @Test
    public void postService_ShouldUpdatePost_ReturnSuccess() {
        PostUpdateDTO updateDTO = new PostUpdateDTO("Updated Title", "Updated Body");
        when(postClient.updatePost(eq("1"), any(PostUpdateDTO.class))).thenReturn(mockPost);

        Post updatedPost = postService.updatePost("1", updateDTO);

        assertNotNull(updatedPost);
        assertEquals("Test Title", updatedPost.getTitle());
        verify(postClient, times(1)).updatePost(eq("1"), any(PostUpdateDTO.class));
    }

    @Test
    public void postServiceUpdate_ShouldThrowEntityNotFoundException_WhenPostNotFound() {
        PostUpdateDTO updateDTO = new PostUpdateDTO("Updated Title", "Updated Body");

        when(postClient.updatePost(eq("1"), any(PostUpdateDTO.class)))
                .thenThrow(new EntityNotFoundException("Post not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            postService.updatePost("1", updateDTO);
        });

        assertEquals("Post not found", exception.getMessage());
        verify(postClient, times(1)).updatePost(eq("1"), any(PostUpdateDTO.class));
    }

    @Test
    public void postService_ShouldThrowHttpClientErrorException_WhenBadRequest() {
        PostUpdateDTO updateDTO = new PostUpdateDTO("", "");

        when(postClient.updatePost(eq("1"), any(PostUpdateDTO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            postService.updatePost("1", updateDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(postClient, times(1)).updatePost(eq("1"), any(PostUpdateDTO.class));
    }

    @Test
    public void postService_ShouldThrowHttpClientErrorException_WhenUnprocessableEntity() {
        PostUpdateDTO updateDTO = new PostUpdateDTO("Updated Title", null); // Corpo nulo pode ser inválido

        when(postClient.updatePost(eq("1"), any(PostUpdateDTO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            postService.updatePost("1", updateDTO);
        });

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
        verify(postClient, times(1)).updatePost(eq("1"), any(PostUpdateDTO.class));
    }

    @Test
    public void postService_ShouldDeletePost_ReturnSuccess() {
        doNothing().when(postClient).deletePost("1");

        postService.deletePost("1");

        verify(postClient, times(1)).deletePost("1");
    }

    @Test
    public void postServiceDelete_ShouldThrowEntityNotFoundException_WhenPostNotFound() {
        doThrow(new EntityNotFoundException("Post not found")).when(postClient).deletePost("1");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            postService.deletePost("1");
        });

        assertEquals("Post not found", exception.getMessage());
        verify(postClient, times(1)).deletePost("1");
    }

    @Test
    public void postServiceDelete_ShouldThrowHttpClientErrorException_WhenBadRequest() {
        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(postClient).deletePost("invalid-id");

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            postService.deletePost("invalid-id");
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(postClient, times(1)).deletePost("invalid-id");
    }

}
