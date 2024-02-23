package ar.com.api.contracts.exception;

import ar.com.api.contracts.model.ApiErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler {

    @ExceptionHandler(ClientErrorApiException.class)
    public Mono<Void> handler4XXRequestException(ServerWebExchange exchange, ClientErrorApiException cApiException)
            throws JsonProcessingException {

        log.error("A ClientError occurred.", cApiException);

        ServerHttpResponse response = exchange.getResponse();
        HttpStatus hStatus = cApiException.getHttpStatus();
        response.setStatusCode(hStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(hStatus.value())
                .message(cApiException.getMessage())
                .build();

        return response.writeWith(Mono
                .just(response.bufferFactory()
                        .wrap(new ObjectMapper().writeValueAsBytes(apiErrorResponse))));
    }

    @ExceptionHandler(ServerErrorApiException.class)
    public Mono<Void> handler5XXRequestException(ServerWebExchange exchange, ServerErrorApiException sApiException)
            throws JsonProcessingException {

        log.error("A ServerException occurred.", sApiException);

        ServerHttpResponse response = exchange.getResponse();
        HttpStatus hStatus = sApiException.getStatus();

        response.setStatusCode(hStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(hStatus.value())
                .message(sApiException.getMessage())
                .build();

        return response.writeWith(Mono
                .just(response.bufferFactory()
                        .wrap(new ObjectMapper().writeValueAsBytes(apiErrorResponse))));
    }

    @ExceptionHandler(ApiNotFoundCustomException.class)
    public Mono<ServerResponse> handleApiNotFoundException(
            ApiNotFoundCustomException aNotFoundCustomException,
            ServerWebExchange exchange) {

        log.error("An ApiCustomException occurred.", aNotFoundCustomException.getMessage());

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(aNotFoundCustomException.getStatus().value())
                .message(aNotFoundCustomException.getMessage())
                .build();

        return ServerResponse
                .status(aNotFoundCustomException.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(apiErrorResponse);
    }

    @ExceptionHandler(ApiValidatorException.class)
    public Mono<ServerResponse> handleApiValidationError(ApiValidatorException validatorException,
                                                         ServerWebExchange webExchange) {
        log.error("An ApiValidationError occurred {}", validatorException.getErrorMessage());

        ApiErrorResponse apiErrorResponse = ApiErrorResponse
                .builder()
                .code(validatorException.getHttpStatus().value())
                .message(validatorException.getMessage())
                .errorMessage(validatorException.getErrorMessage())
                .build();

        return ServerResponse
                .status(validatorException.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(apiErrorResponse);
    }

    @ExceptionHandler(ApiCustomException.class)
    public Mono<ServerResponse> handleApiCustomException(
            ApiCustomException aCustomException,
            ServerWebExchange exchange) {

        log.error("An ApiCustomException occurred.", aCustomException.getCause());

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(aCustomException.getStatus().value())
                .message(aCustomException.getMessage())
                .build();

        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(apiErrorResponse);
    }


    @ExceptionHandler(WebClientResponseException.class)
    public Mono<ServerResponse> handleWebClientRequestException(WebClientResponseException ex, ServerWebExchange exchange) {
        log.error("A WebClientRequestException occurred", ex);

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(HttpStatus.BAD_GATEWAY.value())
                .message("Failed to communicate with external service.")
                .errorMessage(ex.getMessage())
                .build();

        return ServerResponse
                .status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(apiErrorResponse);
    }
    @ExceptionHandler(Exception.class)
    public Mono<ServerResponse> handleGeneralException(Exception ex, ServerWebExchange sWebExchange) {
        log.error("An unexpected Exception occurred", ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Unexpected Internal Server Error.");
        body.put("message", "An unexpected error has occurred.");
        body.put("pathError", sWebExchange.getRequest().getPath());
        body.put("stackTrace", ex.getStackTrace());

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(body);
    }

}
