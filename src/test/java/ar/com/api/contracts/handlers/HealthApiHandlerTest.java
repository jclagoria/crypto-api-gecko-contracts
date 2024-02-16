package ar.com.api.contracts.handlers;

import ar.com.api.contracts.handler.HealthApiHandler;
import ar.com.api.contracts.model.Ping;
import ar.com.api.contracts.services.CoinGeckoServiceStatus;
import org.instancio.Instancio;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class HealthApiHandlerTest {

    @Mock
    private CoinGeckoServiceStatus serviceStatus;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private HealthApiHandler handler;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getStatusServiceCoinGecko_Success() {
        Ping expectedPing = Instancio.create(Ping.class);
        when(serviceStatus.getStatusCoinGeckoService()).thenReturn(Mono.just(expectedPing));

        Mono<ServerResponse> responseMono = handler.getStatusServiceCoinGecko(serverRequest);

        responseMono.subscribe(response -> {
            assertEquals(response.statusCode(), HttpStatus.OK);
        });
    }

    @Test
    public void getStatusServiceCoinGecko_ClientError() {
        WebClientResponseException exception = new WebClientResponseException(
                "Bad Request", 400, "Bad Request", null, null, null, null);
        when(serviceStatus.getStatusCoinGeckoService()).thenReturn(Mono.error(exception));

        Mono<ServerResponse> responseMono = handler.getStatusServiceCoinGecko(serverRequest);

        responseMono.subscribe(response -> {
            assertEquals(response.statusCode().value(), 400);
        }, error -> {
            fail("Expected WebClientResponseException to be handled, but it was not.");
        });
    }

    @Test
    public void getStatusServiceCoinGecko_ServerError() {
        WebClientResponseException exception = new WebClientResponseException(
                "Bad Request", 400, "Bad Request", null, null, null, null);
        when(serviceStatus.getStatusCoinGeckoService()).thenReturn(Mono.error(exception));

        Mono<ServerResponse> responseMono = handler.getStatusServiceCoinGecko(serverRequest);

        responseMono.subscribe(response -> {
            assertEquals(response.statusCode().value(), 500);
        }, error -> {
            fail("Expected WebClientResponseException to be handled, but it was not.");
        });

    }

}
