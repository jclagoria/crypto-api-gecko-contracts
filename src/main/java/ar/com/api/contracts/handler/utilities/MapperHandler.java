package ar.com.api.contracts.handler.utilities;

import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
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

}
