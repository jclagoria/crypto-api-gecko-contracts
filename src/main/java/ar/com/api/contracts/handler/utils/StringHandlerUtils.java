package ar.com.api.contracts.handler.utils;

import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public class StringHandlerUtils {

    public static Mono<ContractAddressByIdFilterDTO> buildContractAddressByIdAndFilterDTO(ServerRequest sRequest){
        return Mono.just(sRequest.pathVariable("id"))
                .zipWith(Mono.just(sRequest.pathVariable("contractAddress")),
                        (idAsset, contractAddress) -> ContractAddressByIdFilterDTO
                                .builder()
                                .idCoin(idAsset)
                                .contractAddress(contractAddress)
                                .build());
    }

    public static Mono<MarketChartDTO> buildFilterDTO (ServerRequest request){
        return Mono.just(request.queryParam("days"))
                .zipWith(Mono.just(request.queryParam("vsCurrency")),
                        (days, vsCurrency) -> MarketChartDTO.builder()
                                .id(request.pathVariable("id"))
                                .contractAddress(request.pathVariable("contractAddress"))
                                .days(days.get())
                                .vsCurrency(vsCurrency.get())
                                .precision(request.queryParam("precision"))
                                .build());
    }

}
