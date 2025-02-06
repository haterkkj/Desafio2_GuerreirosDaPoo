package uol.compass.microservicea.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uol.compass.microservicea.services.CommentService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/posts")
public class ClientController {
    private final CommentService service;


}
