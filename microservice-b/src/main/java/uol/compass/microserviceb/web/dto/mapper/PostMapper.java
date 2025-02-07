package uol.compass.microserviceb.web.dto.mapper;

import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.web.dto.CommentResponseDTO;
import uol.compass.microserviceb.web.dto.PostResponseDTO;

import java.util.List;
import java.util.stream.Collectors;


public class PostMapper {
    public static PostResponseDTO fromPostToDto(Post post) {
        return new PostResponseDTO(post.getId(), post.getTitle(), post.getBody(), CommentMapper.fromListCommentToListDto(post.getComments()));
    }

    public static List<PostResponseDTO> fromListPostToListDto(List<Post> posts) {
        return posts.stream().map(PostMapper::fromPostToDto).collect(Collectors.toList());
    }
}
