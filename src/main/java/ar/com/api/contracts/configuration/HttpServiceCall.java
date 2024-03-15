package ar.com.api.contracts.configuration;

import ar.com.api.contracts.enums.ErrorTypeEnum;
import ar.com.api.contracts.exception.ApiServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class HttpServiceCall {

    private final WebClient webClient;

    public HttpServiceCall(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> Mono<T> getMonoObject(String urlEndPoint, Class<T> responseType) {
        return configureResponseSpec(urlEndPoint)
                .bodyToMono(responseType)
                .doOnSubscribe(subscription -> log.info("Fetch data from CoinGecko service: {}", urlEndPoint))
                .onErrorResume(this::handleError);
    }

    public <T> Flux<T> geFluxObject(String urlEndPoint, Class<T> responseType) {
        return configureResponseSpec(urlEndPoint)
                .bodyToFlux(responseType)
                .doOnSubscribe(subscription -> log.info("Fetch data from CoinGecko service: {}", urlEndPoint))
                .onErrorResume(this::handleError);
    }

    private <T> Mono<T> handleError(Throwable e) {
        return Mono.error(e instanceof ApiServerErrorException ? e : new Exception("General Error", e));
    }

    private Mono<ApiServerErrorException> handleResponseError(Map<String, Object> errorMessage,
                                                              HttpStatus status,
                                                              ErrorTypeEnum errorTypeEnum) {
        String errorBody = (String) errorMessage.getOrDefault("error", "Unknown error");
        return Mono.error(new ApiServerErrorException("Error Occurred", errorBody, errorTypeEnum, status));
    }

    private WebClient.ResponseSpec configureResponseSpec(String urlEndPoint) {
        return webClient.get()
                .uri(urlEndPoint)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response ->
                                response.bodyToMono(Map.class)
                                        .flatMap(errorMessage ->
                                                handleResponseError(errorMessage,
                                                        (HttpStatus) response.statusCode(),
                                                        ErrorTypeEnum.GECKO_CLIENT_ERROR))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(Map.class)
                                .flatMap(errorResponse ->
                                        handleResponseError(errorResponse,
                                                (HttpStatus) response.statusCode(),
                                                ErrorTypeEnum.GECKO_SERVER_ERROR))
                );
    }

}
