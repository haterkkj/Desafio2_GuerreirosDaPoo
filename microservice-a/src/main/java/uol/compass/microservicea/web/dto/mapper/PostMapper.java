package uol.compass.microservicea.web.dto.mapper;

import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.PostResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {
    public static PostResponseDTO fromPostToDto(Post post) {
        return new PostResponseDTO(post.getId(), post.getTitle(), post.getBody());
    }

    public static List<PostResponseDTO> fromListPostToListDto(List<Post> posts) {
        return posts.stream().map(PostMapper::fromPostToDto).collect(Collectors.toList());
    }
}

