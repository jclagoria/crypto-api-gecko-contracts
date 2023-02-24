package ar.com.api.contracts.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.model.MarketChart;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ContractsApiService {
 
 @Value("${api.contractAddressById}")
 private String URL_CONTRACT_ADDRESS_BY_ID_API;

 @Value("${api.contractAddressByIdMarketChart}")
 private String URL_CONTRACT_ADDRESS_MARKET_CHART_API; 

 private WebClient wClient;

 public ContractsApiService(WebClient webClient) {
  this.wClient = webClient;
 }

 public Mono<AssertPlatformAddressById> getAssertPlatformAddressById(ContractAddressByIdFilterDTO filterDto) {
  
  String urlServiceApi = String.format(
                            URL_CONTRACT_ADDRESS_BY_ID_API, 
                            filterDto.getIdCoin(), 
                            filterDto.getContractAddress());
  
  return wClient
            .get()
            .uri(urlServiceApi)
            .retrieve()
            .bodyToMono(AssertPlatformAddressById.class)
            .doOnError(throwable -> log.error("The service is unavailable!", throwable))
            .onErrorComplete();

 }

 public Flux<MarketChart> getContractAddressMarketChartById(MarketChartDTO filterDto){

  String urlService = String.format(
             URL_CONTRACT_ADDRESS_MARKET_CHART_API, 
             filterDto.getId(), 
             filterDto.getContractAddress());
  
  return wClient
          .get()
          .uri(urlService + filterDto.getUrlFilterService())
          .retrieve()
          .bodyToFlux(MarketChart.class)
          .doOnError(throwable -> log.error("The service is unavailable!", throwable))
          .onErrorComplete();
 }

}
