package ar.com.api.contracts.handler;

import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartByRangeDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.model.MarketChart;
import ar.com.api.contracts.services.ContractsApiService;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class ContractApiHandlerTest {

    @Mock
    private ContractsApiService contractsApiService;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private ContractApiHandler contractApiHandler;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(contractsApiService, serverRequest);
    }

    @Test
    public void getContractAddressById_success() {

        when(serverRequest.pathVariable("id")).thenReturn("testId");
        when(serverRequest.pathVariable("contractAddress")).thenReturn("testAddress");

        AssertPlatformAddressById mockResponse = Instancio.create(AssertPlatformAddressById.class);
        when(contractsApiService.getAssertPlatformAddressById(any(ContractAddressByIdFilterDTO.class)))
                .thenReturn(Mono.just(mockResponse));

        Mono<ServerResponse> responseMono = contractApiHandler.getContractAddressById(serverRequest);
        ServerResponse response = responseMono.block();
        assertEquals(response.statusCode(), HttpStatus.OK);
    }

    @Test
    public void getContractAddressById_badRequestError() {

        when(serverRequest.pathVariable("id")).thenReturn("testId");
        when(serverRequest.pathVariable("contractAddress")).thenReturn("testAddress");

        WebClientResponseException expectedException = WebClientResponseException
                .BadRequest.create(
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        null, null, null,null);
        when(contractsApiService.getAssertPlatformAddressById(any(ContractAddressByIdFilterDTO.class)))
                .thenReturn(Mono.error(expectedException));

        Mono<ServerResponse> actualObject = contractApiHandler.getContractAddressById(serverRequest);

        actualObject.subscribe(
                responseObject -> {},
                error -> {
                    assert error.getMessage()
                            .equals("Bad Request") : "The error message does not match.";
                });
    }

    @Test
    public void getContractAddressById_internalServerError() {

        when(serverRequest.pathVariable("id")).thenReturn("testId");
        when(serverRequest.pathVariable("contractAddress")).thenReturn("testAddress");

        WebClientResponseException expectedException = WebClientResponseException
                .BadRequest.create(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal Server Error",
                        null, null, null,null);
        when(contractsApiService.getAssertPlatformAddressById(any())).thenReturn(Mono.error(expectedException));

        Mono<ServerResponse> actualObject = contractApiHandler.getContractAddressById(serverRequest);

        actualObject.subscribe(
                responseObject -> {},
                error -> {
                    assert error.getMessage()
                            .equals("Internal Server Error") : "The error message does not match.";
                });
    }

    @Test
    public void getContractAddressMarketChartById_success() {
        when(serverRequest.pathVariable("id")).thenReturn("testId");
        when(serverRequest.pathVariable("contractAddress")).thenReturn("testAddress");
        when(serverRequest.queryParam("days")).thenReturn(Optional.of("30"));
        when(serverRequest.queryParam("vsCurrency")).thenReturn(Optional.of("usd"));
        when(serverRequest.queryParam("precision")).thenReturn(Optional.empty());

        MarketChart mockMarketChart = Instancio.create(MarketChart.class);
        when(contractsApiService.getContractAddressMarketChartById(any(MarketChartDTO.class)))
                .thenReturn(Mono.just(mockMarketChart));

        Mono<ServerResponse> responseMono = contractApiHandler.getContractAddressMarketChartById(serverRequest);
        ServerResponse response = responseMono.block();
        assertEquals(response.statusCode(), HttpStatus.OK);
    }

    @Test
    public void getContractAddressMarketChartById_badRequestError() {
        when(serverRequest.pathVariable("id")).thenReturn("testId");
        when(serverRequest.pathVariable("contractAddress")).thenReturn("testAddress");
        when(serverRequest.queryParam("days")).thenReturn(Optional.of("30"));
        when(serverRequest.queryParam("vsCurrency")).thenReturn(Optional.of("usd"));
        when(serverRequest.queryParam("precision")).thenReturn(Optional.empty());

        WebClientResponseException expectedException = WebClientResponseException.BadRequest
                .create(
                        HttpStatus.BAD_REQUEST,
                        "Bad Request",
                        null, null,
                        null, null);

        when(contractsApiService.getContractAddressMarketChartById(any(MarketChartDTO.class)))
                .thenReturn(Mono.error(expectedException));

        Mono<ServerResponse> responseApiService = contractApiHandler.getContractAddressMarketChartById(serverRequest);
        responseApiService.subscribe(
                actualObject -> {},
                error -> {
                    assert error.getMessage()
                            .equals("Bad Request") : "The error message does not match.";
                });

    }

    @Test
    public void getContractAddressMarketChartById_internalServerError() {
        when(serverRequest.pathVariable("id")).thenReturn("testId");
        when(serverRequest.pathVariable("contractAddress")).thenReturn("testAddress");
        when(serverRequest.queryParam("days")).thenReturn(Optional.of("30"));
        when(serverRequest.queryParam("vsCurrency")).thenReturn(Optional.of("usd"));
        when(serverRequest.queryParam("precision")).thenReturn(Optional.empty());

        WebClientResponseException expectedException = WebClientResponseException.InternalServerError
                .create(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal Server Error",
                        null, null,
                        null, null);

        when(contractsApiService.getContractAddressMarketChartById(any(MarketChartDTO.class)))
                .thenReturn(Mono.error(expectedException));

        Mono<ServerResponse> responseApiService = contractApiHandler
                .getContractAddressMarketChartById(serverRequest);
        responseApiService.subscribe(
                actualObject -> {},
                error -> {
                    assert error.getMessage()
                            .equals("Internal Server Error") : "The error message does not match.";
                });
    }

    @Test
    public void getContractAddressMarketChartByIdAndRange_success() {
        when(serverRequest.pathVariable("id")).thenReturn("testId");
        when(serverRequest.pathVariable("contractAddress")).thenReturn("testAddress");
        when(serverRequest.queryParam("vsCurrency")).thenReturn(Optional.of("usd"));
        when(serverRequest.queryParam("fromDate")).thenReturn(Optional.of("1392577232"));
        when(serverRequest.queryParam("toDate")).thenReturn(Optional.of("1422577232"));
        when(serverRequest.queryParam("precision")).thenReturn(Optional.empty());

        MarketChart marketChartByRangeMock = Instancio.create(MarketChart.class);
        when(contractsApiService.getContractAddressMarketChartByIdAndRange(any(MarketChartByRangeDTO.class)))
                .thenReturn(Mono.just(marketChartByRangeMock));

        Mono<ServerResponse> responseMonoActual = contractApiHandler
                .getContractAddressMarketChartByIdAndRange(serverRequest);
        ServerResponse response = responseMonoActual.block();
        assertEquals(response.statusCode(), HttpStatus.OK);
    }

    @Test
    public void getContractAddressMarketChartByIdAndRange_badRequestError() {
        when(serverRequest.pathVariable("id")).thenReturn("testId");
        when(serverRequest.pathVariable("contractAddress")).thenReturn("testAddress");
        when(serverRequest.queryParam("vsCurrency")).thenReturn(Optional.of("usd"));
        when(serverRequest.queryParam("fromDate")).thenReturn(Optional.of("1392577232"));
        when(serverRequest.queryParam("toDate")).thenReturn(Optional.of("1422577232"));
        when(serverRequest.queryParam("precision")).thenReturn(Optional.empty());

        WebClientResponseException expectedException = WebClientResponseException.BadRequest
                .create(HttpStatus.BAD_REQUEST,
                        "Bad Request",
                        null, null, null, null);

        when(contractsApiService.getContractAddressMarketChartByIdAndRange(any(MarketChartByRangeDTO.class)))
                .thenReturn(Mono.error(expectedException));

        Mono<ServerResponse> responseApiHandler = contractApiHandler
                .getContractAddressMarketChartByIdAndRange(serverRequest);

        responseApiHandler.subscribe(
                actualObject -> {},
                error -> {
                    assert error.getMessage()
                            .equals("Bad Request") : "The error message does not match.";
                });

    }

}
