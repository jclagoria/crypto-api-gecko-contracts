package ar.com.api.contracts.handler;

import ar.com.api.contracts.exception.ApiCustomException;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

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

    @AfterMethod
    public void resetMocks() {
        reset(serviceStatus, serverRequest);
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
        WebClientResponseException exception = WebClientResponseException.BadRequest.create(
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        null, null, null,null);
        when(serviceStatus.getStatusCoinGeckoService()).thenReturn(Mono.error(exception));

        Mono<ServerResponse> responseMono = handler.getStatusServiceCoinGecko(serverRequest);

        responseMono.subscribe(
                responseObject -> {},
                error -> {
                    assert error instanceof ApiCustomException : "error isn't a instance of ApiCustomerException";
                    assert error.getMessage()
                            .equals("An expected error occurred in getStatusServiceCoinGecko") :
                            "The error message does not match.";
                });
    }

    @Test
    public void getStatusServiceCoinGecko_ServerError() {
        WebClientResponseException exception = WebClientResponseException
                .BadRequest.create(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal Server Error",
                        null, null, null,null);
        when(serviceStatus.getStatusCoinGeckoService()).thenReturn(Mono.error(exception));

        Mono<ServerResponse> responseMono = handler.getStatusServiceCoinGecko(serverRequest);

        responseMono.subscribe(
                responseObject -> {},
                error -> {
                    assert error instanceof ApiCustomException : "error isn't a instance of ApiCustomerException";
                    assert error.getMessage()
                            .equals("An expected error occurred in getStatusServiceCoinGecko") :
                            "The error message does not match.";
                });

    }

}
