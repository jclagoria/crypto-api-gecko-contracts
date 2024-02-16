package ar.com.api.contracts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContractAddressByIdFilterDTO implements IFilterDTO {

    private String idCoin;
    private String contractAddress;

    @Override
    public String getUrlFilterService() {

        return null;
    }

}
