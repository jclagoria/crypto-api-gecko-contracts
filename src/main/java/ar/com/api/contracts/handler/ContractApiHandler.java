package ar.com.api.contracts.handler;

import ar.com.api.contracts.exception.BadRequestException;
import ar.com.api.contracts.handler.utils.StringHandlerUtils;
import ar.com.api.contracts.handler.validations.ContractValidation;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ar.com.api.contracts.model.MarketChart;
import ar.com.api.contracts.services.ContractsApiService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ContractApiHandler {

    private final ContractsApiService serviceContract;

    public ContractApiHandler(ContractsApiService sContract) {
        this.serviceContract = sContract;
    }

    public Mono<ServerResponse> getContractAddressById(ServerRequest sRequest) {
        log.info("Fetching Contract Address by Id from CoinGecko API");

        return ContractValidation
                .validationForCallingGetContractByIdAndAddress(sRequest)
                .flatMap(valid -> StringHandlerUtils.buildContractAddressByIdAndFilterDTO(sRequest))
                .flatMap(filterDTO -> ServerResponse
                        .ok()
                        .body(serviceContract
                                        .getAssertPlatformAddressById(filterDTO),
                                AssertPlatformAddressById.class))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> {
                    log.error("Error retrieving AssertPlatformAddressById", error);
                    int valueErrorCode = HttpStatus.BAD_REQUEST.value();
                    String strErrorMessage = error.getMessage();
                    return ServerResponse
                            .status(valueErrorCode)
                            .bodyValue(new BadRequestException(strErrorMessage));
                });
    }

    public Mono<ServerResponse> getContractAddressMarketChartById (ServerRequest sRequest){

        log.info("Fetching Contract Address Market Chart by Id from CoinGecko API");

        return ContractValidation.validationGetContractAddressMarketChartById(sRequest)
                .flatMap(valid -> StringHandlerUtils.buildFilterDTO(sRequest))
                .flatMap(filterDto -> ServerResponse.ok()
                        .body(serviceContract
                                .getContractAddressMarketChartById(filterDto), MarketChart.class))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> {
                    log.error("Error retrieving MarketChart", error);
                    int valueErrorCode = HttpStatus.BAD_REQUEST.value();
                    String strErrorMessage = error.getMessage();
                    return ServerResponse
                            .status(valueErrorCode)
                            .bodyValue(new BadRequestException(strErrorMessage));
                });
    }

    public Mono<ServerResponse> getContractAddressMarketChartByIdAndRange (ServerRequest sRequest){

        log.info("Fetching Contract Address Market Chart by Id and Range from CoinGecko API");

        return ContractValidation
                .validateGetContractAddressMarketChartByIdAndRange(sRequest)
                .flatMap(valid -> StringHandlerUtils.buildMarketChartByRangeDTO(sRequest))
                .flatMap(filterDTO -> ServerResponse
                        .ok()
                        .body(serviceContract
                                .getContractAddressMarketChartByIdAndRange(filterDTO), MarketChart.class))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> {
                    log.error("Error retrieving MarketChart by Range", error);
                    int valueErrorCode = HttpStatus.BAD_REQUEST.value();
                    String strErrorMessage = error.getMessage();
                    return ServerResponse
                            .status(valueErrorCode)
                            .bodyValue(new BadRequestException(strErrorMessage));
                });
    }

}
