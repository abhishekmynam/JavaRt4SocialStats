package Implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import CentralObjects.TweetDTO;
import Controller.SocialMediaUserAuth;
import Controller.AuthHeadersGenerator.TwitterAuthHeaderGen;

public class TwittersSearch extends TwitterCommonFuncs implements ITwitterSearch {

	public List<TweetDTO> GetTwitterSearchWithKeyWord(SocialMediaUserAuth auth, List<String> searchString) {
		List<TweetDTO> tweets = new ArrayList<TweetDTO>();
		String url = TwitterURLBuilder(searchString);
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

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tweets;
	}

	private String TwitterURLBuilder(List<String> urlParameters) {
		StringBuilder TwitterURL = new StringBuilder("https://api.twitter.com/1.1/search/tweets.json?q=");
		for (String param : urlParameters) {
			if (param.contains("@")) {
				TwitterURL = TwitterURL.append("%40").append(param.substring(1));
			} else if (param.contains("#")) {
				TwitterURL = TwitterURL.append("%23").append(param.substring(1));
			} else if (param.contains(" OR ")) {
				param.replace(" OR ", "%20OR%20");
				TwitterURL = TwitterURL.append(param);
			} else if (param.startsWith("\"") && param.endsWith("\"")) {
				param.replace(" ", "%20");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" -")) {
				param.replace(" -", "%20-");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains("from:")) {
				param.replace("from:", "from%3");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains("to:")) {
				param.replace("to:", "to%3");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains("list:")) {
				param.replace("list:", "list%3");
				param.replace("/", "%2");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" filter:safe")) {
				param.replace(" filter:safe", "%20filter%3Asafe");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" filter:media")) {
				param.replace(" filter:media", "%20filter%3Amedia");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" -filter:retweets")) {
				param.replace(" -filter:retweets", "%20-filter%3Aretweets");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" filter:native_video")) {
				param.replace(" filter:native_video", "%20filter%3Anative_video");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" filter:periscope")) {
				param.replace(" filter:periscope", "%20filter%3Aperiscope");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" filter:vine")) {
				param.replace(" filter:vine", "%20filter%3Avine");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" filter:images")) {
				param.replace(" filter:images", "%20filter%3Aimages");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" filter:twimg")) {
				param.replace(" filter:twimg", "%20filter%3Atwimg");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" filter:links")) {
				param.replace(" filter:links", "%20filter%3Alinks");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains("url:")) {
				param.replace("url:", "url%3");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" url:")) {
				param.replace(" url:", "%20url%3");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" since:")) {
				param.replace(" since:", "%20since%3A");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" until:")) {
				param.replace(" until:", "%20until%3A");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" :)")) {
				param.replace(" :)", "%20%3A)");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" :(")) {
				param.replace(" :(", "%20%3A(");
				TwitterURL = TwitterURL.append(param);
			} else if (param.contains(" ?")) {
				param.replace(" :)", "%20%3F");
				TwitterURL = TwitterURL.append(param);
			} else {
				TwitterURL = TwitterURL.append("%20").append(param);
			}
		}
		return TwitterURL.toString();

	}

}
