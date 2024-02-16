package ar.com.api.contracts.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "coins")
@Getter
@Setter
public class ApiServiceConfig {

    private String baseURL;
    private String healthAPI;
    private String contractAddressId;
    private String contractAddressByIdMarketChart;
    private String contractAddressByIdMarketChartByRange;

}
