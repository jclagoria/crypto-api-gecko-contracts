package ar.com.api.contracts.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepoUrl implements Serializable {
 
 @JsonProperty("github")
 private List<String> github;

 @JsonProperty("bitbucket")
 private List<String> bitbucket;

}
