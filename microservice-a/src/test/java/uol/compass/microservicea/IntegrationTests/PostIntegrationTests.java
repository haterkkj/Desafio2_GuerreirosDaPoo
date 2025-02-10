package uol.compass.microservicea.IntegrationTests;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uol.compass.microservicea.clients.PostClient;
import uol.compass.microservicea.model.Post;
import uol.compass.microservicea.web.dto.PostCreateDTO;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostIntegrationTest {

    @Autowired
    private PostClient postClient;

    private List<Post> preSavedPosts = new ArrayList<>();

    @BeforeEach
    void setup() {
        PostCreateDTO post = new PostCreateDTO("Post Teste", "Conteúdo de teste");
        preSavedPosts.add(postClient.createPost(post));
    }

    @Test
    void testGetPostById_success() {
        String postId = preSavedPosts.get(0).getId();

        Post post = postClient.getPostById(postId);
        assertNotNull(post);
        assertEquals("Post Teste", post.getTitle());
    }

    @Test
    void testGetPostById_notFound() {
        try {
            postClient.getPostById("999");
            fail("Expected FeignException.NotFound");
        } catch (FeignException.NotFound e) {
            assertEquals(404, e.status());
        }
    }
}
