package Implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CentralObjects.TweetDTO;
import Controller.SocialMediaUserAuth;
import Controller.AuthHeadersGenerator.TwitterAuthHeaderGen;

public class TwitterTimeline implements ITwitterTimeline {

	private long MaxId = 0;

	@Override
	public List<TweetDTO> GetTwitterTimelineWithScreenname(SocialMediaUserAuth auth, String screenName, int count) {

		List<TweetDTO> tweets = new ArrayList<TweetDTO>();
		String url = "https://api.twitter.com/1.1/statuses/user_timeline.json" + "?screen_name=" + screenName
				+ "&count=" + count;
		TwitterAuthHeaderGen headerGen = new TwitterAuthHeaderGen();
		String header = headerGen.generateAuthorizationHeader(auth, "GET", url);
		URL myURL;
		StringBuilder timeLine = new StringBuilder();
		try {

			myURL = new URL(url);
			HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
			myURLConnection.setRequestProperty("Authorization", header);
			BufferedReader tweetString = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			timeLine.append(tweetString.readLine());
			tweets = convertResponseToTweet(timeLine.toString());
			while (tweets.size() < count) {
				timeLine = new StringBuilder(
						GetTwitterTimelineWithScreennameAndMaxId(auth, screenName, MaxId));//, count - tweets.size()));
				tweets.addAll(convertResponseToTweet(timeLine.toString()));

			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tweets;
	}

	@Override
	public List<TweetDTO> GetTwitterTimelineWithScreennameDateRange(SocialMediaUserAuth auth, String screenName,
			Date startTime, Date endTime) {

		List<TweetDTO> tweetsTemp = new ArrayList<TweetDTO>();
		List<TweetDTO> tweets = new ArrayList<TweetDTO>();
		String url = "https://api.twitter.com/1.1/statuses/user_timeline.json" + "?screen_name=" + screenName
				+ "&count=" + 200;
		TwitterAuthHeaderGen headerGen = new TwitterAuthHeaderGen();
		String header = headerGen.generateAuthorizationHeader(auth, "GET", url);
		URL myURL;
		StringBuilder timeLine = new StringBuilder();
		try {
			Boolean flag = true;
			myURL = new URL(url);
			HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
			myURLConnection.setRequestProperty("Authorization", header);
			BufferedReader tweetString = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			timeLine.append(tweetString.readLine());
			while (flag) {
				tweetsTemp.addAll(convertResponseToTweet(timeLine.toString()));
				for (int i = 0; i < tweetsTemp.size(); i++) {
					SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM dd HH:mm:ss +0000 yyyy");
					Date date = formatter.parse(tweetsTemp.get(i).tweetDate);

					if (date.compareTo(startTime) > 0 && date.compareTo(endTime) < 0) {
						tweets.add(tweetsTemp.get(i));
					} else {
						flag = false;
					}
				}
				if (flag) {
					timeLine = new StringBuilder(
							GetTwitterTimelineWithScreennameAndMaxId(auth, screenName, MaxId));
				}

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tweets;
	}

	private String GetTwitterTimelineWithScreennameAndMaxId(SocialMediaUserAuth auth, String screenName, long maxId) {
		String url = "https://api.twitter.com/1.1/statuses/user_timeline.json" + "?screen_name=" + screenName
				+ "&maxid=" + maxId;// + "&count=" + count;
		TwitterAuthHeaderGen headerGen = new TwitterAuthHeaderGen();
		String header = headerGen.generateAuthorizationHeader(auth, "GET", url);
		StringBuilder timeLine = new StringBuilder();
		URL myURL;
		try {
			myURL = new URL(url);
			HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
			myURLConnection.setRequestProperty("Authorization", header);
			BufferedReader tweetString = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			timeLine.append(tweetString.readLine());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return timeLine.toString();
	}

	private List<TweetDTO> convertResponseToTweet(String response) {
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
