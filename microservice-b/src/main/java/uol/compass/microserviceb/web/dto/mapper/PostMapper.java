package uol.compass.microserviceb.web.dto.mapper;

import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.web.dto.CommentResponseDTO;
import uol.compass.microserviceb.web.dto.PostResponseDTO;

import java.util.List;
import java.util.stream.Collectors;


public class PostMapper {
    public static List<PostResponseDTO> fromListPostToListDto(List<Post> posts) {
        return posts.stream().map(PostResponseDTO::toDTO).collect(Collectors.toList());
    }
}
