package ar.com.api.contracts.handler;

import org.springframework.stereotype.Component;
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

@Component
@AllArgsConstructor
@Slf4j
public class ContractApiHandler {
 
 private ContractsApiService serviceContract;

 public Mono<ServerResponse> getContractAddressById(ServerRequest sRequest) {

     ContractAddressByIdFilterDTO filterDTO = ContractAddressByIdFilterDTO
                                                  .builder()
                                                  .idCoin(sRequest.pathVariable("id"))
                                                  .contractAddress(sRequest.pathVariable("contractAddress"))
                                                  .build();

     return ServerResponse
               .ok()
               .body(
                    serviceContract.getAssertPlatformAddressById(filterDTO), 
                    AssertPlatformAddressById.class);
 }

 public Mono<ServerResponse> getContractAddressMarketChartById(ServerRequest sRequest) {
     
     MarketChartDTO filterDto = MarketChartDTO
                                   .builder()
                                   .id(sRequest.pathVariable("id"))
                                   .contractAddress(sRequest.pathVariable("contractAddress"))
                                   .days(sRequest.queryParam("days").get())
                                   .vsCurrency(sRequest.queryParam("vsCurrency").get())
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
