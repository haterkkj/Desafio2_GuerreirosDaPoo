package uol.compass.microservicea.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter @RequiredArgsConstructor
public class FeignClientException extends RuntimeException {
    private final int status;
    private final String path;

    public FeignClientException(int status, String message, String path) {
        super(message);
        this.status = status;
        this.path = path;
    }
}
