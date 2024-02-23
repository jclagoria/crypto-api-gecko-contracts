package ar.com.api.contracts.handler;

import ar.com.api.contracts.exception.ApiCustomException;
import ar.com.api.contracts.services.CoinGeckoServiceStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class HealthApiHandler {

    private CoinGeckoServiceStatus serviceStatus;

    public Mono<ServerResponse> getStatusServiceCoinGecko(ServerRequest serverRequest) {

        log.info("In getStatusServiceCoinGecko");

        return serviceStatus
                .getStatusCoinGeckoService()
                .flatMap(ping -> ServerResponse.ok().bodyValue(ping))
                .onErrorResume(Exception.class,
                        error -> Mono
                                .error(new ApiCustomException("An expected error occurred in getStatusServiceCoinGecko",
                                        HttpStatus.INTERNAL_SERVER_ERROR))
                );
    }
}
