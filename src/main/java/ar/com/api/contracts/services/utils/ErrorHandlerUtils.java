package ar.com.api.contracts.services.utils;

import ar.com.api.contracts.enums.ErrorTypeEnum;
import ar.com.api.contracts.exception.ClientErrorApiException;
import ar.com.api.contracts.exception.ServerErrorApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
public class ErrorHandlerUtils {

    public static Mono<Throwable> handleError(ClientResponse clientResponse, ErrorTypeEnum errorType) {
        return clientResponse.bodyToMono(Map.class)
                .flatMap(errorDetails -> {
                    String errorBody = errorDetails.getOrDefault("error", "Unknown error").toString();
                    HttpStatus status = (HttpStatus) clientResponse.statusCode();
                    String errorMessage = String.format("%s: %s", getClientErrorTypeDescription(errorType), errorBody);

                    return Mono.error(
                            errorType == ErrorTypeEnum.GECKO_CLIENT_ERROR ?
                                    new ClientErrorApiException(errorMessage, status) :
                                    new ServerErrorApiException(errorMessage, status)
                    );
                });
    }

    private static String getClientErrorTypeDescription(ErrorTypeEnum errorType) {
        return errorType == ErrorTypeEnum.GECKO_CLIENT_ERROR ?
                ErrorTypeEnum.GECKO_CLIENT_ERROR.getDescription() :
                ErrorTypeEnum.GECKO_SERVER_ERROR.getDescription();
    }


}
