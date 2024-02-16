package ar.com.api.contracts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarketChartDTO implements IFilterDTO {

    private String id;
    private String contractAddress;
    private String vsCurrency;
    private String days;

    @Override
    public String getUrlFilterService() {

        String urlBuilder = "?vs_currency=" + vsCurrency +
                "&days=" + days;

        return urlBuilder;
    }

}
