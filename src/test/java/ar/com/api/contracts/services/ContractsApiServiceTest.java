package ar.com.api.contracts.services;

import ar.com.api.contracts.configuration.ExternalServerConfig;
import ar.com.api.contracts.configuration.HttpServiceCall;
import ar.com.api.contracts.dto.ContractAddressByIdFilterDTO;
import ar.com.api.contracts.dto.MarketChartByRangeDTO;
import ar.com.api.contracts.dto.MarketChartDTO;
import ar.com.api.contracts.enums.ErrorTypeEnum;
import ar.com.api.contracts.exception.ApiServerErrorException;
import ar.com.api.contracts.model.AssertPlatformAddressById;
import ar.com.api.contracts.model.MarketChart;
import ar.com.api.contracts.utils.ContractTestUtils;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.TargetSelector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ContractsApiServiceTest {

    @Mock
    private HttpServiceCall httpServiceCallMock;

    @Mock
    private ExternalServerConfig externalServerConfigMock;

    @InjectMocks
    private ContractsApiService contractsApiService;

    @BeforeEach
    void setUP() {
        MockitoAnnotations.openMocks(this);

        given(externalServerConfigMock.getContractAddressById())
                .willReturn("contractAddressUrlCoinGeckoMock");
        given(externalServerConfigMock.getContractAddressByIdMarketChart())
                .willReturn("contractAddressByIdMarketChartUrlCoinGeckoMock");
        given(externalServerConfigMock.getContractAddressByIdMarketChartByRange())
                .willReturn("contractAddressByIdMarketCharByRangeUrlCoinGeckoMock");
    }

    @AfterEach
    void tearDown() {
        reset(externalServerConfigMock, httpServiceCallMock);
    }

    @Test
    @DisplayName("Ensure successful retrieval of Contract Address of CoinGecko service")
    void whenGetAssertPlatformAddressById_ThenItShouldCallDependenciesAndFetchSuccessfully() {
        AssertPlatformAddressById expectedObject = Instancio.create(AssertPlatformAddressById.class);
        ContractAddressByIdFilterDTO dtoFilter = Instancio.create(ContractAddressByIdFilterDTO.class);
        given(httpServiceCallMock.getMonoObject(eq("contractAddressUrlCoinGeckoMock"),
                eq(AssertPlatformAddressById.class))).willReturn(Mono.just(expectedObject));

        Mono<AssertPlatformAddressById> actualObject = contractsApiService
                .getAssertPlatformAddressById(dtoFilter);

        ContractTestUtils.assertMonoSuccess(actualObject, assertPlatformAddressById -> {
            assertTrue(Optional.ofNullable(assertPlatformAddressById.getPlatforms()).isPresent(),
                    "Platforms should not be null");
            assertTrue(Optional.of(assertPlatformAddressById.getPlatforms()).map(
                    platforms -> !platforms.isEmpty()
            ).orElse(false), "Platform should not be empty");
            assertTrue(Optional.ofNullable(assertPlatformAddressById.getCategories()).isPresent(),
                    "List of Platforms should not be null");
            assertTrue(Optional.of(assertPlatformAddressById.getCategories()).map( listCategories ->
                    listCategories.size() == expectedObject.getCategories().size()).orElse(false),
                    "Total of elements in actual and expected should be equals");
        });

        verify(externalServerConfigMock, times(1)).getContractAddressById();
        verify(httpServiceCallMock).getMonoObject("contractAddressUrlCoinGeckoMock",
                AssertPlatformAddressById.class);
    }

    @Test
    @DisplayName("Handle 4xx errors when retrieving CoinGecko of Contract Address of CoinGecko service")
    void whenGetAssertPlatformAddressById_ThenItShouldCallAndFetchAndHandleOnStatus4xx() {
        ContractAddressByIdFilterDTO filterDTO = Instancio.create(ContractAddressByIdFilterDTO.class);
        ApiServerErrorException expectedError = new ApiServerErrorException("ApiClient error occurred",
                "Forbidden", ErrorTypeEnum.GECKO_CLIENT_ERROR, HttpStatus.FORBIDDEN);
        given(httpServiceCallMock.getMonoObject(eq("contractAddressUrlCoinGeckoMock"),
                eq(AssertPlatformAddressById.class))).willReturn(Mono.error(expectedError));

        Mono<AssertPlatformAddressById> actualErrorObject = contractsApiService
                .getAssertPlatformAddressById(filterDTO);

        ContractTestUtils.assertService4xxClientError(actualErrorObject,
                expectedError.getMessage(), expectedError.getErrorTypeEnum());

        verify(externalServerConfigMock, times(1)).getContractAddressById();
        verify(httpServiceCallMock).getMonoObject("contractAddressUrlCoinGeckoMock",
                AssertPlatformAddressById.class);
    }

    @Test
    @DisplayName("Handle 5xx errors when retrieving CoinGecko of Contract Address of CoinGecko service")
    void whenGetAssertPlatformAddressById_ThenItShouldCallAndFetchAndHandleOnStatus5xx() {
        ContractAddressByIdFilterDTO filterDTO = Instancio.create(ContractAddressByIdFilterDTO.class);
        ApiServerErrorException expectedError = new ApiServerErrorException("ApiServer error occurred",
                "Variant Also Negotiates", ErrorTypeEnum.GECKO_SERVER_ERROR, HttpStatus.VARIANT_ALSO_NEGOTIATES);
        given(httpServiceCallMock.getMonoObject(eq("contractAddressUrlCoinGeckoMock"),
                eq(AssertPlatformAddressById.class))).willReturn(Mono.error(expectedError));

        Mono<AssertPlatformAddressById> actualErrorObject = contractsApiService
                .getAssertPlatformAddressById(filterDTO);

        ContractTestUtils.assertService5xxServerError(actualErrorObject,
                expectedError.getMessage(), expectedError.getErrorTypeEnum());

        verify(externalServerConfigMock, times(1)).getContractAddressById();
        verify(httpServiceCallMock).getMonoObject("contractAddressUrlCoinGeckoMock",
                AssertPlatformAddressById.class);
    }

    @Test
    @DisplayName("Ensure successful retrieval of Market Chart of CoinGecko service")
    void whenGetContractAddressMarketChartById_ThenItShouldCallDependenciesAndFetchSuccessfully() {
        MarketChart expectedObject = Instancio.create(MarketChart.class);
        MarketChartDTO filterDTO = Instancio.create(MarketChartDTO.class);
        given(httpServiceCallMock.getMonoObject(eq("contractAddressByIdMarketChartUrlCoinGeckoMock" + filterDTO.getUrlFilterService()),
                eq(MarketChart.class))).willReturn(Mono.just(expectedObject));

        Mono<MarketChart> actualObject = contractsApiService.getContractAddressMarketChartById(filterDTO);

        ContractTestUtils.assertMonoSuccess(actualObject, marketChart -> {
            assertTrue(Optional.ofNullable(marketChart.getMarketCaps()).isPresent(),
                    "Market Caps should not be null");
            assertTrue(Optional.ofNullable(marketChart.getPrices()).isPresent(),
                    "Prices should not be null");
            assertTrue(Optional.ofNullable(marketChart.getTotalVolumes()).isPresent(),
                    "Total Volumes should not be null");
            assertTrue(Optional.of(marketChart.getMarketCaps())
                            .map(listMarketCaps -> listMarketCaps.size()
                                    == expectedObject.getMarketCaps().size()).orElse(false),
                    "The number of objects in a Market Caps list is not equal to the expected one.");
            assertTrue(Optional.of(marketChart.getPrices()).map(listPrices ->
                            listPrices.size() == expectedObject.getPrices().size()).orElse(false),
                    "The number of objects in a Prices list is not equal to the expected one.");
            assertTrue(Optional.of(marketChart.getTotalVolumes()).map(listTotalVolumes ->
                            listTotalVolumes.size() == expectedObject.getTotalVolumes().size()).orElse(false),
                    "The number of objects in a Total Volumes list is not equal to the expected one.");
        });

        verify(externalServerConfigMock, times(1)).getContractAddressByIdMarketChart();
        verify(httpServiceCallMock).getMonoObject("contractAddressByIdMarketChartUrlCoinGeckoMock" + filterDTO.getUrlFilterService(),
                MarketChart.class);
    }

    @Test
    @DisplayName("Handle 4xx errors when retrieving Market Chart By Id of CoinGecko service")
    void whenGetContractAddressMarketChartById_ThenItShouldCallAndFetchAndHandleOnStatus4xx() {
        MarketChartDTO filterDTO = Instancio.create(MarketChartDTO.class);
        ApiServerErrorException expectedError = new ApiServerErrorException("ApiClient error occurred", "Forbidden",
                ErrorTypeEnum.GECKO_CLIENT_ERROR, HttpStatus.FORBIDDEN);
        given(httpServiceCallMock.getMonoObject(eq("contractAddressByIdMarketChartUrlCoinGeckoMock"
                + filterDTO.getUrlFilterService()), eq(MarketChart.class))).willReturn(Mono.error(expectedError));

        Mono<MarketChart> actualErrorObject = contractsApiService.getContractAddressMarketChartById(filterDTO);

        ContractTestUtils.assertService4xxClientError(actualErrorObject, expectedError.getMessage(),
                expectedError.getErrorTypeEnum());

        verify(externalServerConfigMock, times(1)).getContractAddressByIdMarketChart();
        verify(httpServiceCallMock).getMonoObject("contractAddressByIdMarketChartUrlCoinGeckoMock" + filterDTO.getUrlFilterService(),
                MarketChart.class);
    }

    @Test
    @DisplayName("Handle 5xx errors when retrieving Market Chart By Id of CoinGecko service")
    void whenGetContractAddressMarketChartById_ThenItShouldCallAndFetchAndHandleOnStatus5xx() {
        MarketChartDTO filterDTO = Instancio.create(MarketChartDTO.class);
        ApiServerErrorException expectedError = new ApiServerErrorException("ApiServer error occurred", "Insufficient Storage",
                ErrorTypeEnum.GECKO_SERVER_ERROR, HttpStatus.INSUFFICIENT_STORAGE);
        given(httpServiceCallMock.getMonoObject(eq("contractAddressByIdMarketChartUrlCoinGeckoMock"
                + filterDTO.getUrlFilterService()), eq(MarketChart.class))).willReturn(Mono.error(expectedError));

        Mono<MarketChart> actualErrorObject = contractsApiService.getContractAddressMarketChartById(filterDTO);

        ContractTestUtils.assertService5xxServerError(actualErrorObject, expectedError.getMessage(),
                expectedError.getErrorTypeEnum());

        verify(externalServerConfigMock, times(1)).getContractAddressByIdMarketChart();
        verify(httpServiceCallMock).getMonoObject("contractAddressByIdMarketChartUrlCoinGeckoMock" + filterDTO.getUrlFilterService(),
                MarketChart.class);
    }

    @Test
    @DisplayName("Ensure successfully retrieval of Market Chart By Id And Range of CoinGecko service")
    void whenGetContractAddressMarketChartByIdAndRange_ThenItShouldCallDependenciesAndFetchSuccessfully() {
        MarketChart expectedObject = Instancio.create(MarketChart.class);
        MarketChartByRangeDTO filterDTO = Instancio.create(MarketChartByRangeDTO.class);
        given(httpServiceCallMock.getMonoObject(anyString(), any())).willReturn(Mono.just(expectedObject));

        Mono<MarketChart> actualObject = contractsApiService.getContractAddressMarketChartByIdAndRange(filterDTO);

        ContractTestUtils.assertMonoSuccess(actualObject, marketChartByRange -> {
            assertTrue(Optional.ofNullable(marketChartByRange.getMarketCaps()).isPresent(),
                    "Market Caps should not be null");
            assertTrue(Optional.ofNullable(marketChartByRange.getPrices()).isPresent(),
                    "Prices should not be null");
            assertTrue(Optional.ofNullable(marketChartByRange.getTotalVolumes()).isPresent(),
                    "Total Volumes should not be null");
            assertTrue(Optional.of(marketChartByRange.getMarketCaps())
                            .map(listMarketCaps -> listMarketCaps.size()
                                    == expectedObject.getMarketCaps().size()).orElse(false),
                    "The number of objects in a Market Caps list is not equal to the expected one.");
            assertTrue(Optional.of(marketChartByRange.getPrices()).map(listPrices ->
                            listPrices.size() == expectedObject.getPrices().size()).orElse(false),
                    "The number of objects in a Prices list is not equal to the expected one.");
            assertTrue(Optional.of(marketChartByRange.getTotalVolumes()).map(listTotalVolumes ->
                            listTotalVolumes.size() == expectedObject.getTotalVolumes().size()).orElse(false),
                    "The number of objects in a Total Volumes list is not equal to the expected one.");
        });

        verify(externalServerConfigMock, times(1)).getContractAddressByIdMarketChartByRange();
    }

    @Test
    @DisplayName("Handle 4xx errors when retrieving Market Chart By Id And Range of CoinGecko service")
    void whenGetContractAddressMarketChartByIdAndRange_ThenItShouldCallAndFetchAndHandleOnStatus4xx() {
        MarketChartByRangeDTO dtoFilter = Instancio.create(MarketChartByRangeDTO.class);
        ApiServerErrorException expectedError = new ApiServerErrorException("ApiClient error occurred",
                "Bad Request", ErrorTypeEnum.GECKO_CLIENT_ERROR, HttpStatus.BAD_REQUEST);
        given(httpServiceCallMock.getMonoObject(anyString(),
                any())).willReturn(Mono.error(expectedError));

        Mono<MarketChart> actualErrorObject = contractsApiService
                .getContractAddressMarketChartByIdAndRange(dtoFilter);

        ContractTestUtils.assertService4xxClientError(actualErrorObject, expectedError.getMessage(),
                ErrorTypeEnum.GECKO_CLIENT_ERROR);
        verify(externalServerConfigMock, times(1)).getContractAddressByIdMarketChartByRange();
    }

    @Test
    @DisplayName("Handle 5xx errors when retrieving Market Chart By Id And Range of CoinGecko service")
    void whenGetContractAddressMarketChartByIdAndRange_ThenItShouldCallAndFetchAndHandleOnStatus5xx() {
        MarketChartByRangeDTO dtoFilter = Instancio.create(MarketChartByRangeDTO.class);
        ApiServerErrorException expectedError = new ApiServerErrorException("ApiServer error occurred",
                "Internal Server Error", ErrorTypeEnum.GECKO_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        given(httpServiceCallMock.getMonoObject(anyString(),
                any())).willReturn(Mono.error(expectedError));

        Mono<MarketChart> actualErrorObject = contractsApiService
                .getContractAddressMarketChartByIdAndRange(dtoFilter);

        ContractTestUtils.assertService5xxServerError(actualErrorObject, expectedError.getMessage(),
                ErrorTypeEnum.GECKO_SERVER_ERROR);
        verify(externalServerConfigMock, times(1)).getContractAddressByIdMarketChartByRange();
    }
}