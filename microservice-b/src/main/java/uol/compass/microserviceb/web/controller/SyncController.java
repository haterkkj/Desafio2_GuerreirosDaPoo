package uol.compass.microserviceb.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uol.compass.microserviceb.model.Post;
import uol.compass.microserviceb.services.PostService;
import uol.compass.microserviceb.web.dto.PostCreateDTO;
import uol.compass.microserviceb.web.dto.PostResponseDTO;
import uol.compass.microserviceb.web.dto.mapper.PostMapper;
import uol.compass.microserviceb.web.exception.ErrorMessage;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class SyncController {
    private final PostService service;

    @PostMapping("/sync")
    public ResponseEntity<List<PostResponseDTO>> syncData() {
        List<Post> listPost = service.syncData();
        List<PostResponseDTO> listDto = PostMapper.fromListPostToListDto(listPost);

        return ResponseEntity.ok().body(listDto);
    }
}
