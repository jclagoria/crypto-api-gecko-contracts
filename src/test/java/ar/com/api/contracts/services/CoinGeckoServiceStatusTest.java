package ar.com.api.contracts.services;

import ar.com.api.contracts.configuration.ExternalServerConfig;
import ar.com.api.contracts.model.Ping;
import org.instancio.Instancio;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
public class CoinGeckoServiceStatusTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private ExternalServerConfig externalServerConfig;

    @InjectMocks
    private CoinGeckoServiceStatus coinGeckoServiceStatus;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(externalServerConfig.getPing()).thenReturn("urlPingMock");

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    }

    @Test
    public void testGetStatusCoinGeckoService_returnPingObjectSuccessfully() {
        Ping ping = Instancio.create(Ping.class);
        when(responseSpec.bodyToMono(Ping.class)).thenReturn(Mono.just(ping));

        Mono<Ping> result = coinGeckoServiceStatus.getStatusCoinGeckoService();

        result.subscribe(actualPing -> {
            assert actualPing.equals(ping) : "The received ping does not match the expected one.";
        });

        verify(webClient).get();
    }
}