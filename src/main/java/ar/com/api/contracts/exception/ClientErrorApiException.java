package ar.com.api.contracts.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientErrorApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ClientErrorApiException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

}
