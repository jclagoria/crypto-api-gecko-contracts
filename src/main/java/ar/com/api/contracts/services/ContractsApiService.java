package ar.com.api.contracts.services;

import ar.com.api.contracts.configuration.ExternalServerConfig;
import ar.com.api.contracts.configuration.HttpServiceCall;
import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartByRangeDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.model.MarketChart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ContractsApiService {

    private final HttpServiceCall httpServiceCall;
    private final ExternalServerConfig externalServerConfig;

    public ContractsApiService(HttpServiceCall serviceCall, ExternalServerConfig eServerConfig) {
        this.httpServiceCall = serviceCall;
        this.externalServerConfig = eServerConfig;
    }

    public Mono<AssertPlatformAddressById> getAssertPlatformAddressById(ContractAddressByIdFilterDTO filterDto) {

        String urlServiceApi = String.format(
                externalServerConfig.getContractAddressById(),
                filterDto.getId(),
                filterDto.getContractAddress());

        log.info("Calling method: {}", urlServiceApi);

        return null;
    }

    public Mono<MarketChart> getContractAddressMarketChartById(MarketChartDTO filterDto) {

        String urlService = String.format(
                externalServerConfig.getContractAddressByIdMarketChart(),
                filterDto.getId(),
                filterDto.getContractAddress());

        log.info("Calling method: {}", urlService + filterDto.getUrlFilterService());

        return null;
    }

    public Mono<MarketChart> getContractAddressMarketChartByIdAndRange(MarketChartByRangeDTO filterDto) {

        String urlService = String.format(
                externalServerConfig.getContractAddressByIdMarketChartByRange() + filterDto.getUrlFilterService(),
                filterDto.getId(),
                filterDto.getContractAddress());

        log.info("Calling method: {}", urlService + filterDto.getUrlFilterService());

        return null;
    }

}
