package uol.compass.microservicea.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Getter @Setter @RequiredArgsConstructor
public class MethodArgumentNotValidException extends RuntimeException {
    private final HttpStatus status;
    private final BindingResult bindingResult;

    public MethodArgumentNotValidException(String message, HttpStatus status, BindingResult bindingResult) {
        super(message);
        this.status = status;
        this.bindingResult = bindingResult;
    }
}