package ar.com.api.contracts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class MarketChartDTO extends CommonFilterDTO implements IFilterDTO {

    @NotBlank(message = "Currency cannot be blanc.")
    @NotEmpty(message = "Days cannot be empty.")
    private String vsCurrency;
    @NotBlank(message = "Days cannot be blanc.")
    @NotEmpty(message = "Days cannot be empty.")
    private String days;
    private Optional<String> precision;

    @Builder
    public MarketChartDTO(String id, String contractAddress,
                          String vsCurrency, String days, Optional<String> precision) {
        super(id, contractAddress);
        this.vsCurrency = vsCurrency;
        this.days = days;
        this.precision = precision;
    }

    @Override
    public String getUrlFilterService() {

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("?vs_currency=").append(vsCurrency)
                .append("&days=").append(days)
                .append("&precision=").append(precision.orElse("18"));

        return urlBuilder.toString();
    }

}
