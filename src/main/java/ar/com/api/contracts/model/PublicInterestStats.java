package ar.com.api.contracts.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicInterestStats implements Serializable {
 
 @JsonProperty("alexa_rank")
 private double alexaRank;
 
 @JsonProperty("bing_matches")
 private double bingMatches;

}
