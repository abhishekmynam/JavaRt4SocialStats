package Implementations;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CentralObjects.TweetDTO;

public class TwitterCommonFuncs {
	private long MaxId = 0;
	protected List<TweetDTO> convertResponseToTweet(String response) {
		JSONArray tweetarray;
		MaxId = 9223372036854775807L;
		List<TweetDTO> localTweet = new ArrayList<TweetDTO>();
		try {
			tweetarray = new JSONArray(response);

			for (int i = 0; i < tweetarray.length(); i++) {
				TweetDTO tweet = new TweetDTO();
				JSONObject tweetJson = tweetarray.getJSONObject(i);
				tweet.Id = (long) tweetJson.get("id");
				tweet.retweets = (int) tweetJson.get("retweet_count");
				tweet.screenName = (String) tweetJson.getJSONObject("user").get("screen_name");
				tweet.tweetDate = (String) tweetJson.get("created_at");
				tweet.tweetText = (String) tweetJson.get("text");
				tweet.userImage = (String) tweetJson.getJSONObject("user").get("profile_image_url");
				tweet.userName = (String) tweetJson.getJSONObject("user").get("name");
				JSONArray urls = tweetJson.getJSONObject("entities").getJSONArray("urls");
				List<String> urlsList = new ArrayList<String>();
				for (int j = 0; j < urls.length(); j++) {
					urlsList.add(urls.getJSONObject(j).getString("url"));
				}
				tweet.tweetImage = new ArrayList<String>(urlsList);
				if (MaxId > tweet.Id) {
					MaxId = tweet.Id;
				}
				localTweet.add(tweet);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return localTweet;
	}
}
