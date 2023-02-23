package ar.com.api.contracts.router;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import ar.com.api.contracts.handler.ContractApiHandler;

@Configuration
public class ContractApiRouter {
 
 @Value("${coins.baseURL}") 
 private String URL_SERVICE_API;

 @Value("${coins.healthAPI}") 
 private String URL_HEALTH_GECKO_API;

 @Value("${coins.contractAddressId}")
 private String URL_CONTRACT_BY_ID_URL;
 
 @Bean
 public RouterFunction<ServerResponse> route(ContractApiHandler handler) {

  return RouterFunctions
            .route()
            .GET(URL_SERVICE_API + URL_HEALTH_GECKO_API, 
                        handler::getStatusServiceCoinGecko)
            .GET(URL_SERVICE_API + URL_CONTRACT_BY_ID_URL, 
                        handler::getContractAddressById)            
            .build();

 }

}
