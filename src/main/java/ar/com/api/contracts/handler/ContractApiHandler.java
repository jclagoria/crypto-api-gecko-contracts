package ar.com.api.contracts.handler;

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
@AllArgsConstructor
@Slf4j
public class ContractApiHandler {

    private ContractsApiService serviceContract;

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
                    int valueErrorCode = ((WebClientResponseException) error.getCause())
                            .getStatusCode().value();
                    return ServerResponse.status(valueErrorCode)
                            .bodyValue(((WebClientResponseException) error.getCause()).getStatusText());
                });
    }

    public Mono<ServerResponse> getContractAddressMarketChartById(ServerRequest sRequest) {

        String idAsset = sRequest.pathVariable("id");
        String contractAddress = sRequest.pathVariable("contractAddress");

        if (idAsset.isEmpty() || contractAddress.isEmpty()) {
            log.error("Id Asset or Contract Address are null or blank");
            return ServerResponse.noContent().build();
        }

        Optional<String> daysSearch = sRequest.queryParam("days");
        Optional<String> currencySearch = sRequest.queryParam("vsCurrency");

        if(!daysSearch.isPresent() || !currencySearch.isPresent()) {
            log.error("Days of search or Vs Currency are null or blank");
            return ServerResponse.noContent().build();
        }

        MarketChartDTO filterDto = MarketChartDTO
                .builder()
                .id(idAsset)
                .contractAddress(contractAddress)
                .days(daysSearch.get())
                .vsCurrency(currencySearch.get())
                .precision(sRequest.queryParam("precision"))
                .build();

        return ServerResponse
                .ok()
                .body(
                        serviceContract.getContractAddressMarketChartById(filterDto),
                        MarketChart.class);
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

}
