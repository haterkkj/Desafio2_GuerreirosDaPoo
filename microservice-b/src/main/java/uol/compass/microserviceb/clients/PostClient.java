package uol.compass.microserviceb.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uol.compass.microserviceb.web.dto.FetchedPostDTO;

import java.util.List;

@FeignClient(value = "post-client", url = "https://jsonplaceholder.typicode.com/")
public interface PostClient {

    @GetMapping("/posts")
    List<FetchedPostDTO> getPosts();

}
