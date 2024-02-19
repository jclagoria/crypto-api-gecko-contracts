package ar.com.api.contracts.handler;

import ar.com.api.contracts.exception.BadRequestException;
import ar.com.api.contracts.exception.CoinGeckoDataNotFoudException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartByRangeDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.model.MarketChart;
import ar.com.api.contracts.model.Ping;
import ar.com.api.contracts.services.CoinGeckoServiceStatus;
import ar.com.api.contracts.services.ContractsApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Slf4j
public class ContractApiHandler {

    private ContractsApiService serviceContract;

    public ContractApiHandler(ContractsApiService sContract) {
        this.serviceContract = sContract;
    }

    public Mono<ServerResponse> getContractAddressById(ServerRequest sRequest) {

        String idAsset = sRequest.pathVariable("id");
        String contractAddress = sRequest.pathVariable("contractAddress");

        if (idAsset.isEmpty() || contractAddress.isEmpty()) {
            log.error("Id Asset or Contract Address are null or blank");
            return ServerResponse.noContent().build();
        }

        ContractAddressByIdFilterDTO filterDTO = ContractAddressByIdFilterDTO
                .builder()
                .idCoin(idAsset)
                .contractAddress(contractAddress)
                .build();

        return serviceContract
                .getAssertPlatformAddressById(filterDTO)
                .flatMap(assertPlatformAddressById ->
                        ServerResponse.ok().bodyValue(assertPlatformAddressById))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> {
                    log.error("Error retrieving AssertPlatformAddressById", error);
                    int valueErrorCode = HttpStatus.NOT_FOUND.value();
                    String strErrorMessage = error.getMessage();
                    return ServerResponse
                            .status(valueErrorCode)
                            .bodyValue(new BadRequestException(strErrorMessage));
                });
    }

    public Mono<ServerResponse> getContractAddressMarketChartById(ServerRequest sRequest) {

        String idAsset = sRequest.pathVariable("id");
        String contractAddress = sRequest.pathVariable("contractAddress");

        return validateParameters(idAsset, contractAddress)
                .flatMap(valid -> buildFilterDTO(sRequest))
                .flatMap(filterDto -> ServerResponse.ok()
                        .body(serviceContract
                                .getContractAddressMarketChartById(filterDto), MarketChart.class))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(error -> {
                    log.error("Error retrieving MarketChart", error);
                    int valueErrorCode = HttpStatus.NOT_FOUND.value();
                    String strErrorMessage = error.getMessage();
                    return ServerResponse
                            .status(valueErrorCode)
                            .bodyValue(new BadRequestException(strErrorMessage));
                });
    }

    public Mono<ServerResponse> getContractAddressMarketChartByIdAndRange(ServerRequest sRequest) {

        MarketChartByRangeDTO filterDto = MarketChartByRangeDTO
                .builder()
                .id(sRequest.pathVariable("id"))
                .contractAddress(sRequest.pathVariable("contractAddress"))
                .vsCurrency(sRequest.queryParam("vsCurrency").get())
                .fromDate(sRequest.queryParam("fromDate").get())
                .toDate(sRequest.queryParam("toDate").get())
                .build();

        return ServerResponse
                .ok()
                .body(
                        serviceContract.getContravtAddressMarketChartByIdAndRange(filterDto),
                        MarketChart.class);
    }

    private Mono<Boolean> validateParameters(String idAsset, String contractAddress) {
        if (idAsset.isEmpty() || contractAddress.isEmpty()) {
            log.error("Id Asset or Contract Address are null or blank");
            return Mono.error(new IllegalArgumentException("Id Asset or Contract Address are null or blank"));
        }
        return Mono.just(true);
    }

    private Mono<MarketChartDTO> buildFilterDTO(ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam("days"))
                .zipWith(Mono.justOrEmpty(request.queryParam("vsCurrency")),
                        (days, vsCurrency) -> MarketChartDTO.builder()
                                .id(request.pathVariable("id"))
                                .contractAddress(request.pathVariable("contractAddress"))
                                .days(days)
                                .vsCurrency(vsCurrency)
                                .precision(request.queryParam("precision"))
                                .build())
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Days of search or Vs Currency are null or blank")));
    }

}
