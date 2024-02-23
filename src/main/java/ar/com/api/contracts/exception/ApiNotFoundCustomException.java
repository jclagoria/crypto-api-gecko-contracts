package ar.com.api.contracts.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApiNotFoundCustomException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public ApiNotFoundCustomException(String message, HttpStatus hStatus) {
        super(message);
        this.status = hStatus;
    }


}
