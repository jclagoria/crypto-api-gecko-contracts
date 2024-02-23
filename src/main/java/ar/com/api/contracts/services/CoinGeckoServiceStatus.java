package ar.com.api.contracts.services;

import ar.com.api.contracts.configuration.ExternalServerConfig;
import ar.com.api.contracts.enums.ErrorTypeEnum;
import ar.com.api.contracts.model.Ping;
import ar.com.api.contracts.services.utils.ErrorHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoServiceStatus {

    private final ExternalServerConfig externalServerConfig;

    private final WebClient webClient;

    public CoinGeckoServiceStatus(WebClient webClient, ExternalServerConfig eServerConfig) {
        this.webClient = webClient;
        this.externalServerConfig = eServerConfig;
    }

    public Mono<Ping> getStatusCoinGeckoService() {

        log.info("Calling CoinGecko method {} ", externalServerConfig.getPing());

        return webClient
                .get()
                .uri(externalServerConfig.getPing())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> ErrorHandlerUtils.handleError(response, ErrorTypeEnum.GECKO_CLIENT_ERROR)
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> ErrorHandlerUtils.handleError(response, ErrorTypeEnum.GECKO_SERVER_ERROR)
                )
                .bodyToMono(Ping.class);
    }

}
