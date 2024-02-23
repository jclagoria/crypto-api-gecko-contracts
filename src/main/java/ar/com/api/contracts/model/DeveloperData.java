package ar.com.api.contracts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeveloperData implements Serializable {
 
 @JsonProperty("forks") 
 private double forks;

 @JsonProperty("stars")
 private double stars;

 @JsonProperty("subscribers")
 private long subscribers;

 @JsonProperty("total_issues")
 private long totalIssues;

 @JsonProperty("closed_issues")
 private long closedIssues;

 @JsonProperty("pull_requests_merged")
 private long pullRequestsMerged;

 @JsonProperty("pull_request_contributors")
 private long pullRequestContributors;

 @JsonProperty("code_additions_deletions_4_weeks")
 private CodeAdditionsDeletions4Weeks codeAdditionsDeletions4Weeks;

 @JsonProperty("commit_count_4_weeks")
 private long commitCount4Weeks;

 @JsonProperty("last_4_weeks_commit_activity_series")
 private List<String> last4WeeksCommitActivitySeries;

}
