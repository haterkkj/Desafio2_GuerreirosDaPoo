package uol.compass.microservicea.web.dto.mapper;

import uol.compass.microservicea.model.Comment;
import uol.compass.microservicea.web.dto.CommentResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static List<CommentResponseDTO> fromListCommentToListDto(List<Comment> posts) {
        return posts.stream().map(CommentResponseDTO::toDto).collect(Collectors.toList());
    }
}

