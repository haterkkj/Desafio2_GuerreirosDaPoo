package uol.compass.microservicea.web.dto.mapper;

import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.CommentResponseDTO;
import uol.compass.microservicea.web.dto.PostResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {
    public static PostResponseDTO fromPostToDto(Post post) {
        List<CommentResponseDTO> commentList = CommentMapper.fromListCommentToListDto(post.getComments());
        return new PostResponseDTO(post.getId(), post.getTitle(), post.getBody(), commentList);
    }

    public static List<PostResponseDTO> fromListPostToListDto(List<Post> posts) {
        return posts.stream().map(PostMapper::fromPostToDto).collect(Collectors.toList());
    }
}

