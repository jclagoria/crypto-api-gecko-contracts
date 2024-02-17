package ar.com.api.contracts.router;

import ar.com.api.contracts.configuration.ApiServiceConfig;
import ar.com.api.contracts.services.ContractsApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import ar.com.api.contracts.handler.ContractApiHandler;

@Configuration
public class ContractApiRouter {

 private ApiServiceConfig apiServiceConfig;

 public ContractApiRouter(ApiServiceConfig aServiceConfig) {
  this.apiServiceConfig = aServiceConfig;
 }
 @Bean
 public RouterFunction<ServerResponse> routeContractApi(ContractApiHandler handler) {

  return RouterFunctions
            .route()
            .GET(apiServiceConfig.getBaseURL() + apiServiceConfig.getContractAddressId(),
                        handler::getContractAddressById)
            .GET(apiServiceConfig.getBaseURL() + apiServiceConfig.getContractAddressByIdMarketChart(),
                        RequestPredicates.accept(MediaType.APPLICATION_JSON),
                        handler::getContractAddressMarketChartById)
            .GET(apiServiceConfig.getBaseURL() + apiServiceConfig.getContractAddressByIdMarketChartByRange(),
                        RequestPredicates.accept(MediaType.APPLICATION_JSON),
                        handler::getContractAddressMarketChartByIdAndRange)
            .build();

 }

}
