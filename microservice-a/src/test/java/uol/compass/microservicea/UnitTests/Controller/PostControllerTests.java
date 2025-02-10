package uol.compass.microservicea.UnitTests.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.exceptions.MethodArgumentNotValidException;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.services.PostService;
import uol.compass.microservicea.web.controller.PostController;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;
import uol.compass.microservicea.web.exception.ApiExceptionHandler;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostControllerTests {

    private MockMvc mockMvc;

    @Mock
    private BindingResult bindingResult;

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

        when(postService.createPost(any(PostCreateDTO.class))).thenReturn(mockPost);

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

        when(postService.createPost(any(PostCreateDTO.class))).thenThrow(
                new MethodArgumentNotValidException(
                        "UNPROCESSABLE_ENTITY",
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        bindingResult
                ));

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateDTO)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void postController_ShouldGetAllPosts_ReturnSuccess() throws Exception {
        when(postService.getPosts()).thenReturn(List.of(mockPost));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Post to title 1"));
    }

    @Test
    public void postController_ShouldGetList_ReturnEmpty_Code200() throws Exception {
        when(postService.getPosts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void postController_ShouldReturnGetPostById_ReturnSuccess() throws Exception {
        when(postService.getPostById("1")).thenReturn(mockPost);
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
        doNothing().when(postService).deletePost("1");

        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void postController_ShouldDeletePost_ReturnError() throws Exception {
        doThrow(new EntityNotFoundException("Post not found with ID: " + mockPost.getId()))
                .when(postService).deletePost(mockPost.getId());

        mockMvc.perform(delete("/api/posts/" + mockPost.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Post not found with ID: " + mockPost.getId()));

        verify(postService, times(1)).deletePost(mockPost.getId());
    }
}
