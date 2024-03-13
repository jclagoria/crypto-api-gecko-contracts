package ar.com.api.contracts.handler;

import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartByRangeDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import ar.com.api.contracts.enums.ErrorTypeEnum;
import ar.com.api.contracts.exception.ApiClientErrorException;
import ar.com.api.contracts.services.ContractsApiService;
import ar.com.api.contracts.validators.ValidatorOfDTOComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ContractApiHandler {

    private final ContractsApiService serviceContract;
    private final ValidatorOfDTOComponent validatorComponent;

    public ContractApiHandler(ContractsApiService sContract, ValidatorOfDTOComponent validator) {
        this.serviceContract = sContract;
        this.validatorComponent = validator;
    }

    public Mono<ServerResponse> getContractAddressById(ServerRequest sRequest) {
        log.info("Fetching Contract Address by Id from CoinGecko API");

        return Mono.just(sRequest)
                .map(req -> ContractAddressByIdFilterDTO
                        .builder()
                        .id(req.pathVariable("id"))
                        .contractAddress(req.pathVariable("contractAddress"))
                        .build())
                .flatMap(validatorComponent::validation)
                .flatMap(serviceContract::getAssertPlatformAddressById)
                .flatMap(result -> ServerResponse.ok().bodyValue(result))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error ->
                        Mono.error(
                                new ApiClientErrorException("An expected error occurred in getContractAddressById",
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        ErrorTypeEnum.API_SERVER_ERROR)
                        )
                );
    }

    public Mono<ServerResponse> getContractAddressMarketChartById(ServerRequest sRequest) {
        log.info("Fetching Contract Address Market Chart by Id from CoinGecko API");

        return Mono.just(sRequest)
                .map(req -> MarketChartDTO
                        .builder()
                        .id(req.pathVariable("id"))
                        .contractAddress(req.pathVariable("contractAddress"))
                        .vsCurrency(req.queryParam("vsCurrency").get())
                        .days(req.queryParam("days").get())
                        .precision(req.queryParam("precision"))
                        .build())
                .flatMap(validatorComponent::validation)
                .flatMap(serviceContract::getContractAddressMarketChartById)
                .flatMap(result -> ServerResponse.ok().bodyValue(result))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error ->
                        Mono.error(
                                new ApiClientErrorException("An expected error occurred in getContractAddressMarketChartById",
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        ErrorTypeEnum.API_SERVER_ERROR)
                        )
                );
    }

    public Mono<ServerResponse> getContractAddressMarketChartByIdAndRange(ServerRequest sRequest) {
        log.info("Fetching Contract Address Market Chart by Id and Range from CoinGecko API");

        return Mono.just(sRequest)
                .map(req -> MarketChartByRangeDTO
                        .builder()
                        .id(req.pathVariable("id"))
                        .contractAddress(req.pathVariable("contractAddress"))
                        .vsCurrency(req.queryParam("vsCurrency").get())
                        .fromDate(req.queryParam("fromDate").get())
                        .toDate(req.queryParam("toDate").get())
                        .precision(req.queryParam("precision"))
                        .build())
                .flatMap(validatorComponent::validation)
                .flatMap(serviceContract::getContractAddressMarketChartByIdAndRange)
                .flatMap(result -> ServerResponse.ok().bodyValue(result))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error ->
                        Mono.error(
                                new ApiClientErrorException("An expected error occurred in getContractAddressMarketChartByIdAndRange",
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        ErrorTypeEnum.API_SERVER_ERROR)
                        )
                );
    }

}
