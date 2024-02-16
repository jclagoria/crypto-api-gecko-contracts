package ar.com.api.contracts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarketChartByRangeDTO implements IFilterDTO {

    private String id;
    private String contractAddress;
    private String vsCurrency;
    private String fromDate;
    private String toDate;

    @Override
    public String getUrlFilterService() {

        String urlServicBuilder = "/?vs_currency=" + vsCurrency +
                "&from=" + fromDate +
                "&to=" + toDate;

        return urlServicBuilder;
    }

}
