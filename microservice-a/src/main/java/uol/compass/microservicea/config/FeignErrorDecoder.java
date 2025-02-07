package uol.compass.microservicea.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.exceptions.FeignClientException;

import java.io.IOException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        try {
            JsonNode errorNode = objectMapper.readTree(response.body().asInputStream());

            String message = errorNode.has("message") ? errorNode.get("message").asText() : "Unknown error";
            String path = errorNode.has("path") ? errorNode.get("path").asText() : "N/A";
            int status = response.status();

            switch (HttpStatus.valueOf(status)) {
                case NOT_FOUND:
                    return new EntityNotFoundException(message);
                case INTERNAL_SERVER_ERROR:
                    return new RuntimeException(message);
                case BAD_REQUEST:
                    return new IllegalArgumentException(message);
                default:
                    return new FeignClientException(status, message, path);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
