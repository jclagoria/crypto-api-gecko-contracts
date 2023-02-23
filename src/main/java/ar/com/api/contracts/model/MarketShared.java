package ar.com.api.contracts.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketShared implements Serializable {
 
 @JsonProperty("name")
 private String name;
 
 @JsonProperty("identifier")
 private String identifier;
 
 @JsonProperty("has_trading_incentive")
 private boolean hasTradingIncentive;

}
