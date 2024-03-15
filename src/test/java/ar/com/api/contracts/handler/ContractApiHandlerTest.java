package ar.com.api.contracts.handler;

import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.exception.ApiClientErrorException;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.services.ContractsApiService;
import ar.com.api.contracts.utils.ContractTestUtils;
import ar.com.api.contracts.validators.ValidatorOfDTOComponent;
import org.instancio.Instancio;
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

public class ContractApiHandlerTest {

    @Mock
    private ContractsApiService contractsApiServiceMock;

    @Mock
    private ServerRequest serverRequestMock;

    @Mock
    private ValidatorOfDTOComponent validatorOfDTOComponentMock;

    @InjectMocks
    private ContractApiHandler apiHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Ensure successfully retrieval AssertPlatformAddressById service status 200 from Handler")
    void whenGetContractAddressById_ThenItShouldCallDependenciesAndFetchSuccessfully() {
        AssertPlatformAddressById expectedObject = Instancio.create(AssertPlatformAddressById.class);
        ContractAddressByIdFilterDTO filterDTO = Instancio.create(ContractAddressByIdFilterDTO.class);
        given(serverRequestMock.pathVariable(anyString())).willReturn("ethereum");
        given(serverRequestMock.pathVariable(anyString())).willReturn("0xd26114cd6EE289AccF82350c8d8487fedB8A0C07");
        given(validatorOfDTOComponentMock.validation(any())).willReturn(Mono.just(filterDTO));
        given(contractsApiServiceMock.getAssertPlatformAddressById(any(ContractAddressByIdFilterDTO.class)))
                .willReturn(Mono.just(expectedObject));

        Mono<ServerResponse> expectedResponse = apiHandler.getContractAddressById(serverRequestMock);

        ContractTestUtils.assertMonoSuccess(expectedResponse, serverResponse ->
                serverResponse.statusCode().is2xxSuccessful());

        verify(contractsApiServiceMock, times(1)).getAssertPlatformAddressById(filterDTO);
    }

    @Test
    @DisplayName("Ensure successfully retrieval AssertPlatformAddressById service status 404 Not found from Handler")
    void whenGetContractAddressById_ThenItShouldCallDependenciesAndFetchNotFound() {
        ContractAddressByIdFilterDTO filterDTO = Instancio.create(ContractAddressByIdFilterDTO.class);
        given(serverRequestMock.pathVariable(anyString())).willReturn("ethereum");
        given(serverRequestMock.pathVariable(anyString())).willReturn("0xd26114cd6EE289AccF82350c8d8487fedB8A0C07");
        given(validatorOfDTOComponentMock.validation(any())).willReturn(Mono.just(filterDTO));
        given(contractsApiServiceMock.getAssertPlatformAddressById(any(ContractAddressByIdFilterDTO.class)))
                .willReturn(Mono.empty());

        Mono<ServerResponse> expectedResponse = apiHandler.getContractAddressById(serverRequestMock);

        ContractTestUtils.assertMonoSuccess(expectedResponse, serverResponse ->
                serverResponse.statusCode().is4xxClientError());

        verify(contractsApiServiceMock, times(1)).getAssertPlatformAddressById(filterDTO);
    }

    @Test
    @DisplayName("Ensure error handling in GetContractAddressById returns INTERNAL_SERVER_ERROR")
    void whenGetContractAddressById_ThenItShouldHandleErrorAndReturnInternalServerError() {
        ContractAddressByIdFilterDTO filterDTO = Instancio.create(ContractAddressByIdFilterDTO.class);
        given(serverRequestMock.pathVariable(anyString())).willReturn("ethereum");
        given(serverRequestMock.pathVariable(anyString())).willReturn("0xd26114cd6EE289AccF82350c8d8487fedB8A0C07");
        given(validatorOfDTOComponentMock.validation(any())).willReturn(Mono.just(filterDTO));
        given(contractsApiServiceMock.getAssertPlatformAddressById(any(ContractAddressByIdFilterDTO.class)))
                .willReturn(Mono.error(new RuntimeException("Unexpected Error")));

        Mono<ServerResponse> actualErrorResponse = apiHandler.getContractAddressById(serverRequestMock);

        ContractTestUtils.assertClient5xxServerError(actualErrorResponse,
                "An expected error occurred in getContractAddressById",
                HttpStatus.INTERNAL_SERVER_ERROR);

        verify(contractsApiServiceMock, times(1)).getAssertPlatformAddressById(filterDTO);
    }

}
