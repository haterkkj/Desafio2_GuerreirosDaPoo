package uol.compass.microservicea.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import uol.compass.microservicea.exceptions.EntityNotFoundException;
import uol.compass.microservicea.exceptions.FeignClientException;
import uol.compass.microservicea.exceptions.MethodArgumentNotValidException;
import uol.compass.microservicea.web.dto.CommentCreateDTO;
import uol.compass.microservicea.web.dto.CommentUpdateDTO;
import uol.compass.microservicea.web.dto.PostCreateDTO;
import uol.compass.microservicea.web.dto.PostUpdateDTO;

import java.io.IOException;

@Slf4j
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        try {
            JsonNode errorNode = objectMapper.readTree(response.body().asInputStream());

            String message = errorNode.has("message") ? errorNode.get("message").asText() : "Unknown error";
            String method = errorNode.has("method") ? errorNode.get("method").asText() : "unknown";
            String path = errorNode.has("path") ? errorNode.get("path").asText() : "N/A";
            int status = response.status();

            return switch (HttpStatus.valueOf(status)) {
                case NOT_FOUND -> new EntityNotFoundException(message);
                case INTERNAL_SERVER_ERROR -> new RuntimeException(message);
                case BAD_REQUEST -> new IllegalArgumentException(message);
                case UNPROCESSABLE_ENTITY -> buildMethodArgumentNotValidException(errorNode, message, path, method);
                default -> new FeignClientException(status, message, path);
            };

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MethodArgumentNotValidException buildMethodArgumentNotValidException(JsonNode errorNode, String message, String path, String method) {
        JsonNode errorsNode = errorNode.get("errors");

        BindingResult bindingResult = getBindingResult(path, method);

        errorsNode.fields().forEachRemaining(field -> {
            String fieldKey = field.getKey();
            String errorMessage = field.getValue().asText();
            bindingResult.addError(new FieldError("requestBody", fieldKey, errorMessage));
        });

        return new MethodArgumentNotValidException(message, HttpStatus.UNPROCESSABLE_ENTITY, bindingResult);
    }

    private static BindingResult getBindingResult(String path, String method) {
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "requestBody");

        if (method.equals("POST")) {
            if(path.contains("/comments")) {
                bindingResult = new BeanPropertyBindingResult(new CommentCreateDTO(), "requestBody");
            } else {
                bindingResult = new BeanPropertyBindingResult(new PostCreateDTO(), "requestBody");
            }
        }

        if (method.equals("PUT")) {
            if(path.contains("/comments")) {
                bindingResult = new BeanPropertyBindingResult(new CommentUpdateDTO(), "requestBody");
            } else {
                bindingResult = new BeanPropertyBindingResult(new PostUpdateDTO(), "requestBody");
            }
        }
        return bindingResult;
    }
}
