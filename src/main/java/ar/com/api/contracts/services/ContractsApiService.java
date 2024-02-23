package ar.com.api.contracts.services;

import ar.com.api.contracts.configuration.ExternalServerConfig;
import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartByRangeDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import ar.com.api.contracts.enums.ErrorTypeEnum;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.model.MarketChart;
import ar.com.api.contracts.services.utils.ErrorHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ContractsApiService {

    private final WebClient wClient;
    private final ExternalServerConfig externalServerConfig;

    public ContractsApiService(WebClient webClient, ExternalServerConfig eServerConfig) {
        this.wClient = webClient;
        this.externalServerConfig = eServerConfig;
    }

    public Mono<AssertPlatformAddressById> getAssertPlatformAddressById(ContractAddressByIdFilterDTO filterDto) {

        String urlServiceApi = String.format(
                externalServerConfig.getContractAddressById(),
                filterDto.getId(),
                filterDto.getContractAddress());

        log.info("Calling method: {}", urlServiceApi);

        return wClient
                .get()
                .uri(urlServiceApi)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> ErrorHandlerUtils.handleError(response, ErrorTypeEnum.GECKO_CLIENT_ERROR)
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> ErrorHandlerUtils.handleError(response, ErrorTypeEnum.GECKO_SERVER_ERROR)
                )
                .bodyToMono(AssertPlatformAddressById.class);


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
                        response -> ErrorHandlerUtils.handleError(response, ErrorTypeEnum.GECKO_CLIENT_ERROR)
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> ErrorHandlerUtils.handleError(response, ErrorTypeEnum.GECKO_SERVER_ERROR)
                )
                .bodyToMono(MarketChart.class);
    }

    public Mono<MarketChart> getContractAddressMarketChartByIdAndRange(MarketChartByRangeDTO filterDto) {

        String urlService = String.format(
                externalServerConfig.getContractAddressByIdMarketChartByRange() + filterDto.getUrlFilterService(),
                filterDto.getId(),
                filterDto.getContractAddress());

        log.info("Calling method: {}",  urlService + filterDto.getUrlFilterService());

        return wClient
                .get()
                .uri(urlService + filterDto.getUrlFilterService())
                .retrieve()
                .onStatus(
                        stats -> stats.is4xxClientError(),
                        response -> ErrorHandlerUtils.handleError(response, ErrorTypeEnum.GECKO_CLIENT_ERROR)
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> ErrorHandlerUtils.handleError(response, ErrorTypeEnum.GECKO_SERVER_ERROR)
                )
                .bodyToMono(MarketChart.class);

    }

}
