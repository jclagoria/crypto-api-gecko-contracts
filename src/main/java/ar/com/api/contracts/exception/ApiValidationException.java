package ar.com.api.contracts.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiValidationException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorMessage;

    public ApiValidationException(String message, String errorMessage, HttpStatus status) {
        super(message);
        this.errorMessage = errorMessage;
        this.httpStatus = status;
    }

}
