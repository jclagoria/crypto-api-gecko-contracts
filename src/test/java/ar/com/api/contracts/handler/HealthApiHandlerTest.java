package ar.com.api.contracts.handler;

import ar.com.api.contracts.model.Ping;
import ar.com.api.contracts.services.CoinGeckoServiceStatus;
import ar.com.api.contracts.utils.ContractTestUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.*;

public class HealthApiHandlerTest {

    @Mock
    private CoinGeckoServiceStatus coinGeckoServiceStatusMock;

    @Mock
    private ServerRequest serverRequestMock;

    @InjectMocks
    private HealthApiHandler healthApiHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        reset(serverRequestMock, coinGeckoServiceStatusMock);
    }

    @Test
    @DisplayName("Ensure successfully retrieval of CoinGecko service status 200 form Handler")
    void whenGetStatusServiceCoinGecko_ThenItShouldCallDependenciesAndFetchSuccessfully() {
        Ping expectedObject = Instancio.create(Ping.class);
        given(coinGeckoServiceStatusMock.getStatusCoinGeckoService())
                .willReturn(Mono.just(expectedObject));

        Mono<ServerResponse> actualResponseMono = healthApiHandler
                .getStatusServiceCoinGecko(serverRequestMock);

        ContractTestUtils.assertMonoSuccess(actualResponseMono,
                serverResponse -> serverResponse.statusCode().is2xxSuccessful());

        verify(coinGeckoServiceStatusMock, times(1)).getStatusCoinGeckoService();
    }

    @Test
    @DisplayName("Handle Not Found when retrieve CoinGecko service status 404 from Handler")
    void whenGetStatusServiceCoinGecko_ThenItShouldCallDependenciesAndFetchNotFound() {
        given(coinGeckoServiceStatusMock.getStatusCoinGeckoService()).willReturn(Mono.empty());

        Mono<ServerResponse> actualResponseMono = healthApiHandler
                .getStatusServiceCoinGecko(serverRequestMock);

        ContractTestUtils.assertMonoSuccess(actualResponseMono,
                serverResponse -> serverResponse.statusCode().is4xxClientError());

        verify(coinGeckoServiceStatusMock, times(1)).getStatusCoinGeckoService();
    }

    @Test
    @DisplayName("Handle 5xx errors when retrieving CoinGecko service status from Handler")
    void whenGetStatusServiceCoinGecko_ThenItShouldCalledAndHandlesOnStatus5xx() {
        given(coinGeckoServiceStatusMock.getStatusCoinGeckoService())
                .willReturn(Mono.error(new RuntimeException("An error occurred")));

        Mono<ServerResponse> errorResponseActual = healthApiHandler.getStatusServiceCoinGecko(serverRequestMock);

        ContractTestUtils.assertClient5xxServerError(errorResponseActual,
                "An expected error occurred in getStatusServiceCoinGecko",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
