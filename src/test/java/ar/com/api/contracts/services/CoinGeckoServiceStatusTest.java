package ar.com.api.contracts.services;

import ar.com.api.contracts.configuration.ExternalServerConfig;
import ar.com.api.contracts.configuration.HttpServiceCall;
import ar.com.api.contracts.enums.ErrorTypeEnum;
import ar.com.api.contracts.exception.ApiServerErrorException;
import ar.com.api.contracts.model.Ping;
import ar.com.api.contracts.utils.ContractTestUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CoinGeckoServiceStatusTest {

    @Mock
    private HttpServiceCall httpServiceCallMock;

    @Mock
    private ExternalServerConfig externalServerConfig;

    @InjectMocks
    private CoinGeckoServiceStatus coinGeckoServiceStatus;

    @BeforeEach
    void setUP() {
        MockitoAnnotations.openMocks(this);
        given(externalServerConfig.getPing()).willReturn("healthUrlCoinGeckoMock");
    }

    @AfterEach
    void tearDown() {
        reset(externalServerConfig, externalServerConfig);
    }

    @Test
    @DisplayName("Ensure successfully retrieval of CoinGecko service status")
    void whenGetStatusCoinGeckoServiceCalled_ThenShouldCallDependenciesAndFetchSuccessfully() {
        Ping expectedPing = Instancio.create(Ping.class);
        given(httpServiceCallMock.getMonoObject(eq("healthUrlCoinGeckoMock"), eq(Ping.class)))
                .willReturn(Mono.just(expectedPing));

        Mono<Ping> actualObject = coinGeckoServiceStatus.getStatusCoinGeckoService();

        ContractTestUtils.assertMonoSuccess(actualObject, pingObject -> {
            Optional.ofNullable(pingObject.getGeckoSays()).ifPresentOrElse(
                    name -> {},
                    () -> fail("Ping not be null")
            );
            Assertions.assertEquals(expectedPing.getGeckoSays(),
                    pingObject.getGeckoSays());
        });

        then(externalServerConfig).should(times(2)).getPing();
        then(httpServiceCallMock).should(times(1))
                .getMonoObject("healthUrlCoinGeckoMock", Ping.class);
    }

    @Test
    @DisplayName("Handle 4xx errors when retrieving CoinGecko service status")
    void whenGetStatusCoinGeckoServiceIsCalled_ThenItShouldCallDependenciesAndHandlesOnStatus4xx() {
        ApiServerErrorException expectedException =
                new ApiServerErrorException("ApiClient error occurred", "Bad Request",
                        ErrorTypeEnum.GECKO_CLIENT_ERROR, HttpStatus.BAD_REQUEST);
        given(httpServiceCallMock.getMonoObject(eq("healthUrlCoinGeckoMock"), eq(Ping.class)))
                .willReturn(Mono.error(expectedException));

        Mono<Ping> actualObject = coinGeckoServiceStatus.getStatusCoinGeckoService();

        ContractTestUtils.assertService4xxClientError(actualObject,
                expectedException.getMessage(),
                ErrorTypeEnum.GECKO_CLIENT_ERROR);

        then(externalServerConfig).should(times(2)).getPing();
        then(httpServiceCallMock).should(times(1))
                .getMonoObject("healthUrlCoinGeckoMock", Ping.class);
    }

    @Test
    @DisplayName("Handle 5xx errors when retrieving CoinGecko service status")
    void whenGetStatusCoinGeckoServiceCalled_ThenItShouldCallDependenciesAndHandlesOnStatus5xx() {
        ApiServerErrorException expectedException = new ApiServerErrorException(
                "ApiServer error occurred",
                "Internal Server Error",
                ErrorTypeEnum.GECKO_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR);
        given(httpServiceCallMock.getMonoObject(eq("healthUrlCoinGeckoMock"), eq(Ping.class)))
                .willReturn(Mono.error(expectedException));

        Mono<Ping> actualObject = coinGeckoServiceStatus.getStatusCoinGeckoService();

        ContractTestUtils.assertService5xxServerError(actualObject,
                expectedException.getMessage(),
                ErrorTypeEnum.GECKO_SERVER_ERROR);

        then(externalServerConfig).should(times(2)).getPing();
        then(httpServiceCallMock).should(times(1))
                .getMonoObject("healthUrlCoinGeckoMock", Ping.class);
    }

}