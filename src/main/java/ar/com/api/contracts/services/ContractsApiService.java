package ar.com.api.contracts.services;

import ar.com.api.contracts.exception.ManageExceptionCoinGeckoServiceApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartByRangeDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.model.MarketChart;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ContractsApiService extends  CoinGeckoServiceApi {
 
 @Value("${api.contractAddressById}")
 private String URL_CONTRACT_ADDRESS_BY_ID_API;

 @Value("${api.contractAddressByIdMarketChart}")
 private String URL_CONTRACT_ADDRESS_MARKET_CHART_API; 

 @Value("${api.contractAddressByIdMarketChartByRange}")
 private String URL_CONTRACT_ADDRESS_MARKET_CHART_RANGE_API; 

 private final WebClient wClient;

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
          .onStatus(
                  HttpStatusCode::is4xxClientError,
                  getClientResponseMonoDataException()
          )
          .onStatus(
                  HttpStatusCode::is5xxServerError,
                  getClientResponseMonoServerException()
          )
          .bodyToMono(AssertPlatformAddressById.class)
          .doOnError(
                  ManageExceptionCoinGeckoServiceApi::throwServiceException
          );

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
          .onStatus(
                  HttpStatusCode::is4xxClientError,
                  getClientResponseMonoDataException()
          )
          .onStatus(
                  HttpStatusCode::is5xxServerError,
                  getClientResponseMonoServerException()
          )
          .bodyToFlux(MarketChart.class)
          .doOnError(
                  ManageExceptionCoinGeckoServiceApi::throwServiceException
          );
 }

 public Flux<MarketChart> getContravtAddressMarketChartByIdAndRange(MarketChartByRangeDTO filterDto) {

   String urlService = String.format(
            URL_CONTRACT_ADDRESS_MARKET_CHART_RANGE_API, 
            filterDto.getId(), 
            filterDto.getContractAddress());
   
   return wClient
          .get()
          .uri(urlService + filterDto.getUrlFilterService())
          .retrieve()
           .onStatus(
                   HttpStatusCode::is4xxClientError,
                   getClientResponseMonoDataException()
           )
           .onStatus(
                   HttpStatusCode::is5xxServerError,
                   getClientResponseMonoServerException()
           )
          .bodyToFlux(MarketChart.class)
           .doOnError(
                   ManageExceptionCoinGeckoServiceApi::throwServiceException
           );

 }

}
