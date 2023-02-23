package ar.com.api.contracts.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeveloperData implements Serializable {
 
 private double forks;
 private double stars;
 private long subscribers;
 private long total_issues;
 private long closed_issues;
 private long pull_requests_merged;
 private long pull_request_contributors;
 private CodeAdditionsDeletions4Weeks code_additions_deletions_4_weeks;
 private long commit_count_4_weeks;
 private List<String> last_4_weeks_commit_activity_series;

}
