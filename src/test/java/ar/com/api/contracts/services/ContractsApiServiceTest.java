package ar.com.api.contracts.services;

import ar.com.api.contracts.configuration.ExternalServerConfig;
import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import org.instancio.Instancio;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

public class ContractsApiServiceTest {

    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;
    @Mock
    private ExternalServerConfig externalServerConfigMock;

    @InjectMocks
    private ContractsApiService contractsApiServiceMock;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);

        when(externalServerConfigMock.getContractAddressById()).thenReturn("/idValue/contract/contractAddress");

        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);
    }

    @Test(priority = 1)
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

    @Test(priority = 2)
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

    @Test(priority = 3)
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

}