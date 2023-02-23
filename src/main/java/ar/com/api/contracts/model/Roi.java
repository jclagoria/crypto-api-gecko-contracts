package ar.com.api.contracts.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Roi implements Serializable {
 
 @ToString.Exclude 
 @JsonProperty("times")
 private double times;

 @JsonProperty("currency")
 private String currency;

 @JsonProperty("percentage")
 private double percentage;

}
