package ar.com.api.contracts.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.com.api.contracts.dto.Ping;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoServiceStatus {
 
 private static String URL_PING_SERVICE = "/ping";

 private WebClient webClient;

 public CoinGeckoServiceStatus(WebClient webClient) {
  this.webClient = webClient;
 }

 public Mono<Ping> getStatusCoinGeckoService() {
  
  log.info("Calling method: ", URL_PING_SERVICE); 

  return webClient
         .get()
         .uri(URL_PING_SERVICE)
         .retrieve()
         .bodyToMono(Ping.class)
         .doOnError(throwable -> log.error("The service is unavailable!", throwable));
 }

}
