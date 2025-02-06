package uol.compass.microserviceb.web.dto.mapper;

import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.web.dto.CommentResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentResponseDTO fromCommentToDto(Comment comment) {
        return new CommentResponseDTO(comment.getId(), comment.getEmail(), comment.getName(), comment.getBody());
    }

    public static List<CommentResponseDTO> fromListCommentToListDto(List<Comment> posts) {
        return posts.stream().map(CommentMapper::fromCommentToDto).collect(Collectors.toList());
    }
}

