package uol.compass.microservicea.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.services.PostService;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostResponseDTO;
import uol.compass.microservicea.web.dto.UpdatePostDTO;
import uol.compass.microservicea.web.dto.mapper.PostMapper;

import java.util.List;

@Tag(name = "Posts", description = "Endpoints for managing posts.")
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getPosts() {
        List<Post> posts = postService.getPosts();
        List<PostResponseDTO> postsDto = PostMapper.fromListPostToListDto(posts);

        return ResponseEntity.ok().body(postsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable String id) {
        Post post = postService.getPostById(id);
        PostResponseDTO postDto = PostMapper.fromPostToDto(post);

        return ResponseEntity.ok().body(postDto);
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostCreateDTO postCreateDTO) {
        Post newPost = postService.createPost(postCreateDTO);
        PostResponseDTO newPostDto = PostMapper.fromPostToDto(newPost);

        return ResponseEntity.ok().body(newPostDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id ) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody UpdatePostDTO updatePostDTO) {
        Post updatedPost = postService.updatePost(id, updatePostDTO);
        return ResponseEntity.ok().body(updatedPost);
    }

}
