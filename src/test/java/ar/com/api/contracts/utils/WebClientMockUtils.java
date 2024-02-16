package ar.com.api.contracts.utils;

import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class WebClientMockUtils {

    public static WebClient.ResponseSpec responseSpecMock() {
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);

        return responseSpecMock;
    }

    public static WebClient mockWebClient() {
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock());

        return webClientMock;
    }

}
