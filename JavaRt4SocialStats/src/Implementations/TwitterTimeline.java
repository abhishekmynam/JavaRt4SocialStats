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

import CentralObjects.TweetDTO;
import Controller.SocialMediaUserAuth;
import Controller.AuthHeadersGenerator.TwitterAuthHeaderGen;

public class TwitterTimeline extends TwitterCommonFuncs implements ITwitterTimeline {

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

	

}
