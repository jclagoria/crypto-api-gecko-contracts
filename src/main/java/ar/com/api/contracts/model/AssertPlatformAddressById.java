package ar.com.api.contracts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssertPlatformAddressById extends AssertPlatformBase {

    @JsonProperty("platforms")
    private Map<String, String> platforms;

    @JsonProperty("detail_platforms")
    private Map<String, ItemPlatform> detailPlatforms;

    @JsonProperty("block_time_in_minutes")
    private Long blockTimeInMinutes;

    @JsonProperty("hashing_algorithm")
    private String hashingAlgorithm;

    @JsonProperty("categories")
    private List<String> categories;

    @JsonProperty("public_notice")
    private String publicNotice;

    @JsonProperty("additional_notices")
    private List<String> additionalNotices;

    @JsonProperty("localization")
    private Map<String, String> localization;

    @JsonProperty("description")
    private Map<String, String> description;

    @JsonProperty("links")
    private Link link;

    @JsonProperty("image")
    private Image image;

    @JsonProperty("country_origin")
    private String countryOrigin;

    @JsonProperty("genesis_date")
    private String genesisDate;

    @JsonProperty("contract_address")
    private String contractAddress;

    @JsonProperty("sentiment_votes_up_percentage")
    private long sentimentVotesUpPercentage;

    @JsonProperty("sentiment_votes_down_percentage")
    private long sentimentVotesDownPercentage;

    @JsonProperty("market_cap_rank")
    private long marketCapRank;

    @JsonProperty("coingecko_rank")
    private long coingeckoRank;

    @JsonProperty("coingecko_score")
    private double coingeckoScore;

    @JsonProperty("developer_score")
    private double developerScore;

    @JsonProperty("community_score")
    private double communityScore;

    @JsonProperty("liquidity_score")
    private long liquidityScore;

    @JsonProperty("public_interest_score")
    private double publicInterestScore;

    @JsonProperty("market_data")
    private MarketData marketData;

    @JsonProperty("community_data")
    private CommunityData communityData;

    @JsonProperty("developer_data")
    private DeveloperData developerData;

    @JsonProperty("public_interest_stats")
    private PublicInterestStats publicInterestStats;

    @JsonProperty("status_updates")
    private List<String> statusUpdates;

    @JsonProperty("last_updated")
    private String lastUpdated;

    @JsonProperty("tickers")
    private List<Ticker> tickers;

}
