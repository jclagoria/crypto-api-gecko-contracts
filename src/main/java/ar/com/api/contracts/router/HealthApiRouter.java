package ar.com.api.contracts.router;

import ar.com.api.contracts.configuration.ApiServiceConfig;
import ar.com.api.contracts.handler.HealthApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HealthApiRouter {

    private ApiServiceConfig apiServiceConfig;

    public HealthApiRouter(ApiServiceConfig aServiceConfig) {
        this.apiServiceConfig = aServiceConfig;
    }

    @Bean
    public RouterFunction<ServerResponse> routeHealthApi(HealthApiHandler handler) {
        return RouterFunctions
                .route()
                .GET(apiServiceConfig.getBaseURL() + apiServiceConfig.getHealthAPI(),
                        handler::getStatusServiceCoinGecko)
                .build();
    }
}
