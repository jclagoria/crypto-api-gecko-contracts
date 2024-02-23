package ar.com.api.contracts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ContractAddressByIdFilterDTO extends CommonFilterDTO implements IFilterDTO{
    @Builder
    public ContractAddressByIdFilterDTO(String id, String contractAddress) {
        super(id, contractAddress);
    }

    @Override
    public String getUrlFilterService() {

        return null;
    }

}
