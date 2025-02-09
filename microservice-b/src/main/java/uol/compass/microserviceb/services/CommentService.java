package uol.compass.microserviceb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uol.compass.microserviceb.exceptions.EntityNotFoundException;
import uol.compass.microserviceb.model.Comment;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.repositories.CommentRepository;
import uol.compass.microserviceb.repositories.PostRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment save(Comment comment) {
        try {
            return commentRepository.save(comment);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while saving the comment.", e);
        }
    }

    @Transactional(readOnly = true)
    public void deleteById(String postId, String id) {
        try {
            if (!commentRepository.existsById(id)) {
                throw new EntityNotFoundException("Comment with ID " + id + " not found.");
            }

            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new EntityNotFoundException("Post with ID " + postId + " not found.")
            );

            post.getComments().removeIf(comment -> comment.getId().equals(id));

            postRepository.save(post);

            commentRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Comment update(Comment updatedComment) {
        try {
            if (!commentRepository.existsById(updatedComment.getId())) {
                throw new EntityNotFoundException("Comment with ID " + updatedComment.getId() + " not found.");
            }
            return commentRepository.save(updatedComment);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Comment with ID " + updatedComment.getId() + " not found.");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while updating the comment.", e);
        }
    }

    public Comment findById(String id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with ID " + id + " not found.")
        );
    }

    public List<Comment> findByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }
}
