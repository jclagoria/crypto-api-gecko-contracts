package ar.com.api.contracts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class MarketChartByRangeDTO extends CommonFilterDTO implements IFilterDTO {

    @NotBlank(message = "Currency cannot be blanc.")
    @NotEmpty(message = "Currency cannot be empty.")
    private String vsCurrency;
    @NotBlank(message = "From Date cannot be blanc.")
    @NotEmpty(message = "From Date cannot be empty.")
    private String fromDate;
    @NotBlank(message = "To Date cannot be blanc.")
    @NotEmpty(message = "To Date cannot be empty.")
    private String toDate;
    private Optional<String> precision;

    @Builder
    public MarketChartByRangeDTO(String id, String contractAddress,
                                 String vsCurrency, String fromDate,
                                 String toDate, Optional<String> precision) {
        super(id, contractAddress);
        this.vsCurrency = vsCurrency;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.precision = precision;
    }



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
