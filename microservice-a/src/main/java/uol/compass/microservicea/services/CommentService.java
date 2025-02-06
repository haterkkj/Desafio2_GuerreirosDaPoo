package uol.compass.microservicea.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uol.compass.microservicea.clients.CommentClient;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentClient client;



}
