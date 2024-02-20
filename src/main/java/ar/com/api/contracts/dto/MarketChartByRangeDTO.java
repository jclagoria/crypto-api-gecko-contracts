package ar.com.api.contracts.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public class MarketChartByRangeDTO implements IFilterDTO {

    private String id;
    private String contractAddress;
    private String vsCurrency;
    private String fromDate;
    private String toDate;
    private Optional<String> precision;

    @Override
    public String getUrlFilterService() {

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("?vs_currency=").append(vsCurrency)
                .append("&from=").append(fromDate)
                .append("&to=").append(toDate)
                .append("&precision=").append(precision.orElse("18"));

        return urlBuilder.toString();
    }

}
