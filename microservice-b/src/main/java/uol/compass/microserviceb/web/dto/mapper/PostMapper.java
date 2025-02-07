package uol.compass.microserviceb.web.dto.mapper;

import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.web.dto.CommentResponseDTO;
import uol.compass.microserviceb.web.dto.PostResponseDTO;

import java.util.List;
import java.util.stream.Collectors;


public class PostMapper {
    public static PostResponseDTO fromPostToDto(Post post) {
        return new PostResponseDTO(post.getId(), post.getTitle(), post.getBody(), fromCommentToDto(post.getComments()));
    }

    public static List<PostResponseDTO> fromListPostToListDto(List<Post> posts) {
        return posts.stream().map(PostMapper::fromPostToDto).collect(Collectors.toList());
    }

    private static List<CommentResponseDTO> fromCommentToDto(List<Comment> comments) {
        return comments.stream().map(CommentResponseDTO::toDto).collect(Collectors.toList());
    }
}
