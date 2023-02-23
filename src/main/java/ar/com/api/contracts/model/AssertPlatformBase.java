package ar.com.api.contracts.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssertPlatformBase implements Serializable {
 
 private static final long serialVersionUID = 1L;

 @JsonProperty("id")
 private String id;

 @JsonProperty("symbol")
 private String symbol;

 @JsonProperty("asset_platform_id")
 private String assetPlatformId;

}
