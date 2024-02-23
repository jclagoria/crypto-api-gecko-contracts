package ar.com.api.contracts.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServerErrorApiException extends RuntimeException {

    private final HttpStatus status;

    public ServerErrorApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }
}
