package ar.com.api.contracts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
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
