package uol.compass.microserviceb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
