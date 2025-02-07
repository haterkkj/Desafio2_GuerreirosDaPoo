package uol.compass.microserviceb.web.dto.mapper;

import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.web.dto.CommentResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static List<CommentResponseDTO> fromListCommentToListDto(List<Comment> comments) {
        return comments.stream().map(CommentResponseDTO::toDto).collect(Collectors.toList());
    }
}

