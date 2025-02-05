package uol.compass.microserviceb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uol.compass.microserviceb.exceptions.EntityNotFoundException;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment save(Comment comment) {
        try {
            return commentRepository.save(comment);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while saving the comment.", e);
        }
    }

    @Transactional(readOnly = true)
    public void deleteById(String id) {
        try {
            if(!commentRepository.existsById(id)) {
                throw new EntityNotFoundException("Comment with ID " + id + " not found.");
            }
            commentRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Comment with ID " + id + " not found.");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while deleting the comment.", e);
        }
    }

    @Transactional
    public Comment update(Comment updatedComment) {
        try {
            Comment foundComment = findById(updatedComment.getId());
            foundComment = updatedComment;
            return commentRepository.save(foundComment);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Comment with ID " + updatedComment.getId() + " not found.");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while updating the comment.", e);
        }
    }

    public Comment findById(String id) {
        return commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment with ID " + id + " not found."));
    }

}
