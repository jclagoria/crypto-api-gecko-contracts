package ar.com.api.contracts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityData implements Serializable {

 @JsonProperty("facebook_likes")
 private long facebookLikes;

 @JsonProperty("twitter_followers")
 private long twitterFollowers;

 @JsonProperty("reddit_average_posts_48h")
 private long redditAveragePosts48h;

 @JsonProperty("reddit_average_comments_48h")
 private long redditAverageComments48h;

 @JsonProperty("reddit_subscribers")
 private long redditSubscribers;

 @JsonProperty("reddit_accounts_active_48h")
 private long redditAccountsActive48h;

 @JsonProperty("telegram_channel_user_count")
 private String telegramChannelUserCount;
 
}
