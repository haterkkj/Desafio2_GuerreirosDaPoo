package uol.compass.microservicea.web.dto.mapper;

import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.PostResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {
    public static List<PostResponseDTO> fromListPostToListDto(List<Post> posts) {
        return posts.stream().map(PostResponseDTO::toDto).collect(Collectors.toList());
    }
}

