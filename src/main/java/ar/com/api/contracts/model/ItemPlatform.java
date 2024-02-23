package ar.com.api.contracts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemPlatform implements Serializable {
 
 @JsonProperty("decimal_place")
 private long decimalPlace;

 @JsonProperty("contract_address")
 private String contractAddress;

}
