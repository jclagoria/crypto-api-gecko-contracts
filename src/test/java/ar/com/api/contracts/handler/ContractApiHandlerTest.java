package ar.com.api.contracts.handler;

import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.services.ContractsApiService;
import org.instancio.Instancio;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class ContractApiHandlerTest {

    @Mock
    private ContractsApiService contractsApiService;

    @Mock
    private ServerRequest serverRequestMock;
    @InjectMocks
    private ContractApiHandler contractApiHandler;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getContractAddressById_success() {
        when(serverRequestMock.pathVariable("id")).thenReturn(Instancio.create(String.class));
        when(serverRequestMock.pathVariable("contractAddress")).thenReturn(Instancio.create(String.class));
        AssertPlatformAddressById mockResponse = Instancio
                .create(AssertPlatformAddressById.class);
        when(contractsApiService.getAssertPlatformAddressById(any(ContractAddressByIdFilterDTO.class)))
                .thenReturn(Mono.just(mockResponse));

        Mono<ServerResponse> result = contractApiHandler.getContractAddressById(serverRequestMock);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

}