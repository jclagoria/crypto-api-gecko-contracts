package ar.com.api.contracts.services;

import ar.com.api.contracts.configuration.ExternalServerConfig;
import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartByRangeDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import ar.com.api.contracts.exception.ManageExceptionCoinGeckoServiceApi;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.model.MarketChart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ContractsApiService extends CoinGeckoServiceApi {

    private final WebClient wClient;
    private final ExternalServerConfig externalServerConfig;

    @Value("${api.contractAddressByIdMarketChartByRange}")
    private String URL_CONTRACT_ADDRESS_MARKET_CHART_RANGE_API;

    public ContractsApiService(WebClient webClient, ExternalServerConfig eServerConfig) {
        this.wClient = webClient;
        this.externalServerConfig = eServerConfig;
    }

    public Mono<AssertPlatformAddressById> getAssertPlatformAddressById(ContractAddressByIdFilterDTO filterDto) {

        String urlServiceApi = String.format(
                externalServerConfig.getContractAddressById(),
                filterDto.getIdCoin(),
                filterDto.getContractAddress());

        log.info("Calling method: {}", urlServiceApi);

        return wClient
                .get()
                .uri(urlServiceApi)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        getClientResponseMonoServerException()
                )
                .bodyToMono(AssertPlatformAddressById.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );

    }

    public Mono<MarketChart> getContractAddressMarketChartById(MarketChartDTO filterDto) {

        String urlService = String.format(
                externalServerConfig.getContractAddressByIdMarketChart(),
                filterDto.getId(),
                filterDto.getContractAddress());

        log.info("Calling method: {}",  urlService + filterDto.getUrlFilterService());

        return wClient
                .get()
                .uri(urlService + filterDto.getUrlFilterService())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        getClientResponseMonoServerException()
                )
                .bodyToMono(MarketChart.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

    public Flux<MarketChart> getContractAddressMarketChartByIdAndRange(MarketChartByRangeDTO filterDto) {

        String urlService = String.format(
                URL_CONTRACT_ADDRESS_MARKET_CHART_RANGE_API,
                filterDto.getId(),
                filterDto.getContractAddress());

        return wClient
                .get()
                .uri(urlService + filterDto.getUrlFilterService())
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        getClientResponseMonoServerException()
                )
                .bodyToFlux(MarketChart.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );

    }

}
