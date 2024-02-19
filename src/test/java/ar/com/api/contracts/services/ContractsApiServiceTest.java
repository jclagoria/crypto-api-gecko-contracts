package ar.com.api.contracts.services;

import ar.com.api.contracts.configuration.ExternalServerConfig;
import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.model.MarketChart;
import org.instancio.Instancio;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

public class ContractsApiServiceTest {

    private WebClient webClientMock;

    private WebClient.ResponseSpec responseSpecMock;

    private ExternalServerConfig externalServerConfigMock;


    private ContractsApiService contractsApiServiceMock;

    @BeforeMethod
    public void setUp() {
        webClientMock = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        responseSpecMock = mock(WebClient.ResponseSpec.class);
        externalServerConfigMock = mock(ExternalServerConfig.class);

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);

        when(externalServerConfigMock.getContractAddressById()).thenReturn("/idValue/contract/contractAddress");
        when(externalServerConfigMock.getContractAddressByIdMarketChart())
                .thenReturn("/idValue/contract/contractAddress/marketChart/");

        contractsApiServiceMock = new ContractsApiService(webClientMock, externalServerConfigMock);
    }

    @AfterMethod
    void resetMocks() {
        reset(webClientMock, responseSpecMock);
    }

    @Test()
    void testGetAssertPlatformAddressById_returnObjectSuccessfully() {

        ContractAddressByIdFilterDTO filterDTOMock = Instancio.create(ContractAddressByIdFilterDTO.class);
        AssertPlatformAddressById expectedObject = Instancio.create(AssertPlatformAddressById.class);
        when(responseSpecMock.bodyToMono(AssertPlatformAddressById.class))
                .thenReturn(Mono.just(expectedObject));

        Mono<AssertPlatformAddressById> actualObject = contractsApiServiceMock
                .getAssertPlatformAddressById(filterDTOMock);

        actualObject.subscribe(object -> {
            assert object.equals(expectedObject) :
                    "The received AssertPlatformAddress does not match the expected one.";
        });

        verify(webClientMock).get();
    }

    @Test()
    void testGetAssertPlatformAddressById_handle400BadRequestError() {
        ContractAddressByIdFilterDTO filterDTO = Instancio.create(ContractAddressByIdFilterDTO.class);
        WebClientResponseException exceptionMock = new WebClientResponseException
                ("Bad Request", 400, null, null, null, null);
        when(responseSpecMock.bodyToMono(AssertPlatformAddressById.class))
                .thenReturn(Mono.error(exceptionMock));

        Mono<AssertPlatformAddressById> actualError4xx = contractsApiServiceMock
                .getAssertPlatformAddressById(filterDTO);

        actualError4xx.subscribe(
                actualObject -> {},
                error -> {
                    assert error.getMessage().equals("Bad Request") : "The error message does not match.";
                });

    }

    @Test()
    void testGetAssertPlatformAddressById_handle500InternalServerError() {
        ContractAddressByIdFilterDTO filterDTO = Instancio.create(ContractAddressByIdFilterDTO.class);
        WebClientResponseException exception5xxMock = new WebClientResponseException
                ("Internal Server Error", 500, null, null, null, null);

        when(responseSpecMock.bodyToMono(AssertPlatformAddressById.class))
                .thenReturn(Mono.error(exception5xxMock));

        Mono<AssertPlatformAddressById> actualError5xx = contractsApiServiceMock
                .getAssertPlatformAddressById(filterDTO);

        actualError5xx.subscribe(
                actualObject -> {},
                error -> {
                    assert error.getMessage().equals("Internal Server Error") : "The error message does not match.";
                }
        );

    }

    @Test()
    void testGetContractAddressMarketChartById_returnObjectSuccessfully() {

        MarketChartDTO filterDTOMock = Instancio.create(MarketChartDTO.class);
        MarketChart expectedObject = Instancio.create(MarketChart.class);
        when(responseSpecMock.bodyToMono(MarketChart.class)).thenReturn(Mono.just(expectedObject));

        Mono<MarketChart> actualObject = contractsApiServiceMock.getContractAddressMarketChartById(filterDTOMock);

        actualObject.subscribe(object -> {
            assert object.equals(expectedObject) :
                    "The received AssertPlatformAddress does not match the expected one.";
            assert !object.getMarketCaps().isEmpty();
            assert object.getMarketCaps().size() == object.getMarketCaps().size();
            assert object.getPrices().get(0) == object.getPrices().get(0);
            assert object.getTotalVolumes() != null && !object.getTotalVolumes().isEmpty();
        });

        verify(webClientMock).get();
    }

}