package ar.com.api.contracts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonFilterDTO {

    @NotBlank(message = "Id cannot be blanc.")
    @NotEmpty(message = "Id cannot be empty.")
    private String id;
    @NotEmpty(message = "Contract Address cannot be empty.")
    @NotBlank(message = "Contract Address cannot be blanc.")
    private String contractAddress;

}
