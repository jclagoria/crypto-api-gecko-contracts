package ar.com.api.contracts.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ExternalServerConfig {

    private String urlCoinGecko;
    private String ping;
    private String contractAddressById;
    private String contractAddressByIdMarketChart;
    private String contractAddressByIdMarketChartByRange;

}
