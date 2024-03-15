package ar.com.api.contracts.handler.utilities;

import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartByRangeDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public class MapperHandler {

    public static Mono<ContractAddressByIdFilterDTO> createContractAddressByIdFilterDTOFromServerRequest(ServerRequest sRequest) {
        return Mono.just(ContractAddressByIdFilterDTO
                .builder()
                .id(sRequest.pathVariable("id"))
                .contractAddress(sRequest.pathVariable("contractAddress"))
                .build());
    }

    public static Mono<MarketChartDTO> createMarketChartDTOFilterDTOFromServerRequest(ServerRequest sRequest) {
        return Mono.just(MarketChartDTO
                .builder()
                .id(sRequest.pathVariable("id"))
                .contractAddress(sRequest.pathVariable("contractAddress"))
                .vsCurrency(sRequest.queryParam("vsCurrency").get())
                .days(sRequest.queryParam("days").get())
                .precision(sRequest.queryParam("precision"))
                .build());
    }

    public static Mono<MarketChartByRangeDTO> createMarketChartByRangeDTOFromServerRequest(ServerRequest sRequest) {
        return Mono.just(MarketChartByRangeDTO
                .builder()
                .id(sRequest.pathVariable("id"))
                .contractAddress(sRequest.pathVariable("contractAddress"))
                .vsCurrency(sRequest.queryParam("vsCurrency").get())
                .fromDate(sRequest.queryParam("fromDate").get())
                .toDate(sRequest.queryParam("toDate").get())
                .precision(sRequest.queryParam("precision"))
                .build());
    }

}
