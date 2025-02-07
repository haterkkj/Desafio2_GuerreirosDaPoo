package uol.compass.microserviceb.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Synchronization", description = "Synchronize data from JSONPlaceholder to the local database.")
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class SyncController {
    private final PostService service;

    @Operation(
            summary = "Sync JsonPlaceholder data with MongoDB",
            description = "Endpoint to sync JsonPlaceHolder data with system data base.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successfully Synced.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostResponseDTO.class))
                            )
                    )
            }
    )
    @PostMapping("/sync")
    public ResponseEntity<List<PostResponseDTO>> syncData() {
        List<Post> listPost = service.syncData();
        List<PostResponseDTO> listDto = PostMapper.fromListPostToListDto(listPost);

        return ResponseEntity.ok().body(listDto);
    }
}
