package ar.com.api.contracts.services;

import ar.com.api.contracts.configuration.ExternalServerConfig;
import ar.com.api.contracts.exception.ManageExceptionCoinGeckoServiceApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.com.api.contracts.model.Ping;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoServiceStatus extends CoinGeckoServiceApi {

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
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        getClientResponseMonoServerException()
                )
                .bodyToMono(Ping.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

}
