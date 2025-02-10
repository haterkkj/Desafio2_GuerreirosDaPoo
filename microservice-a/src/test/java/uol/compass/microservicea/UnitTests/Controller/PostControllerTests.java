package uol.compass.microservicea.UnitTests.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.services.PostService;
import uol.compass.microservicea.web.controller.PostController;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;
import uol.compass.microservicea.web.exception.ApiExceptionHandler;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostControllerTests {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private ObjectMapper objectMapper;
    private Post mockPost;

    private static final String POST_ID = "123";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        mockPost = new Post();
        mockPost.setId(POST_ID);
        mockPost.setTitle("Test Title");
        mockPost.setBody("Test Body");
    }

    @Test
    void postService_shouldGetAllPosts() throws Exception {
        when(postService.getPosts()).thenReturn(List.of(mockPost));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(POST_ID));
    }

    @Test
    void postService_shouldReturnEmptyListWhenNoPostsExist() throws Exception {
        when(postService.getPosts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void postService_shouldGetPostById() throws Exception {
        when(postService.getPostById(POST_ID)).thenReturn(mockPost);

        mockMvc.perform(get("/api/posts/{id}", POST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(POST_ID));
    }

    @Test
    void postService_shouldReturnNotFoundWhenPostDoesNotExist() throws Exception {
        when(postService.getPostById(POST_ID)).thenThrow(new EntityNotFoundException("Post not found"));

        mockMvc.perform(get("/api/posts/{id}", POST_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void postService_shouldCreatePostSuccessfully() throws Exception {
        PostCreateDTO createDTO = new PostCreateDTO("Test Title", "Test Body");
        when(postService.createPost(any(PostCreateDTO.class))).thenReturn(mockPost);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(POST_ID));
    }

    @Test
    void postService_shouldUpdatePostSuccessfully() throws Exception {
        PostUpdateDTO updateDTO = new PostUpdateDTO("Updated Title", "Updated Body");
        when(postService.updatePost(eq(POST_ID), any(PostUpdateDTO.class))).thenReturn(mockPost);

        mockMvc.perform(put("/api/posts/{id}", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(POST_ID));
    }

    @Test
    void postService_shouldReturnNotFoundWhenUpdatingNonExistentPost() throws Exception {
        PostUpdateDTO updateDTO = new PostUpdateDTO("Updated Title", "Updated Body");
        when(postService.updatePost(eq(POST_ID), any(PostUpdateDTO.class)))
                .thenThrow(new EntityNotFoundException("Post not found"));

        mockMvc.perform(put("/api/posts/{id}", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void postService_shouldDeletePostSuccessfully() throws Exception {
        doNothing().when(postService).deletePost(POST_ID);

        mockMvc.perform(delete("/api/posts/{id}", POST_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void postService_shouldReturnNotFoundWhenDeletingNonExistentPost() throws Exception {
        doThrow(new EntityNotFoundException("Post not found"))
                .when(postService).deletePost(POST_ID);

        mockMvc.perform(delete("/api/posts/{id}", POST_ID))
                .andExpect(status().isNotFound());
    }
}