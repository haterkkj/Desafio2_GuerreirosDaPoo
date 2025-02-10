package uol.compass.microserviceb.UnitTests.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import uol.compass.microserviceb.exceptions.EntityNotFoundException;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.PostService;
import uol.compass.microserviceb.web.controller.PostController;
import uol.compass.microserviceb.web.dto.PostCreateDTO;
import uol.compass.microserviceb.web.dto.PostUpdateDTO;
import uol.compass.microserviceb.web.exception.ApiExceptionHandler;

@ExtendWith(MockitoExtension.class)
public class PostControllerTests {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private ObjectMapper objectMapper;

    private Post mockPost;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(postController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        mockPost = new Post();
        mockPost.setId("1");
        mockPost.setTitle("Test Post to title 1");
        mockPost.setBody("This is a test.");
    }


    @Test
    void postController_ShouldCreatePost_ReturnSuccess() throws Exception {
        PostCreateDTO postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("Test Post to title 1");
        postCreateDTO.setBody("This is a test to body 1.");

        when(postService.save(any(Post.class))).thenReturn(mockPost);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Post to title 1"));
    }

    @Test

    public void postController_ShouldReturnUnprocessableEntity_ReturnCode422() throws Exception {
        PostCreateDTO postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("");
        postCreateDTO.setBody("Test body");

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postCreateDTO)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void postController_ShouldGetAllPosts_ReturnSuccess() throws Exception {
        when(postService.findAll()).thenReturn(List.of(mockPost));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Post to title 1"));
    }

    @Test
    public void postController_ShouldGetList_ReturnEmpty_Code200() throws Exception {
        when(postService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void postController_ShouldReturnGetPostById_ReturnSuccess() throws Exception {
        when(postService.findById("1")).thenReturn(mockPost);
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post to title 1"));
    }

    @Test
    public void postController_ShouldUpdatePost_ReturnSuccess() throws Exception {
        PostUpdateDTO updatePostDTO = new PostUpdateDTO();
        updatePostDTO.setTitle("Updated Title");
        updatePostDTO.setBody("Updated Body");

        Post updatedPost = new Post();
        updatedPost.setId("1");
        updatedPost.setTitle("Updated Title");
        updatedPost.setBody("Updated Body");

        when(postService.updatePost(eq("1"), any(PostUpdateDTO.class))).thenReturn(updatedPost);

        mockMvc.perform(put("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePostDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.body").value("Updated Body"));
    }

    @Test
    public void postController_ShouldDeletePost_ReturnSuccess() throws Exception {
        doNothing().when(postService).deletePostById("1");

        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void postController_ShouldDeletePost_ReturnError() throws Exception {
        doThrow(new EntityNotFoundException("Post not found with ID: " + mockPost.getId()))
                .when(postService).deletePostById(mockPost.getId());

        mockMvc.perform(delete("/api/posts/" + mockPost.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Post not found with ID: " + mockPost.getId())); // Garante que o JSON de erro foi retornado corretamente

        verify(postService, times(1)).deletePostById(mockPost.getId());
    }
}
