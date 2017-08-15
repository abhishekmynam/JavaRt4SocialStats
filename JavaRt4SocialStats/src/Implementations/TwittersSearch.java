package Implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CentralObjects.TweetDTO;
import CentralObjects.ContainedWithinTwitter;
import CentralObjects.GeoSearchDTO;
import Controller.SocialMediaUserAuth;
import Controller.AuthHeadersGenerator.TwitterAuthHeaderGen;

public class TwittersSearch extends TwitterCommonFuncs implements ITwitterSearch {

	public List<TweetDTO> GetTwitterSearchWithKeyWord(SocialMediaUserAuth auth, List<String> searchString) {
		List<TweetDTO> tweets = new ArrayList<TweetDTO>();
		StringBuilder url = null;

		url = new StringBuilder("https://api.twitter.com/1.1/search/tweets.json?");
		url.append(TwitterSearchURLBuilder(searchString));

		TwitterAuthHeaderGen headerGen = new TwitterAuthHeaderGen();
		String header = headerGen.generateAuthorizationHeader(auth, "GET", url.toString());
		URL myURL;
		StringBuilder timeLine = new StringBuilder();
		try {

			myURL = new URL(url.toString());
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

	public List<GeoSearchDTO> GetTwitterGeoSearchWithKeyWord(SocialMediaUserAuth auth, List<String> searchString) {
		List<GeoSearchDTO> tweets = new ArrayList<GeoSearchDTO>();
		StringBuilder url = null;

		url = new StringBuilder("https://api.twitter.com/1.1/geo/search.json?");
		url.append(TwitterGeoSearchURLBuilder(searchString));

		TwitterAuthHeaderGen headerGen = new TwitterAuthHeaderGen();
		String header = headerGen.generateAuthorizationHeader(auth, "GET", url.toString());
		URL myURL;
		StringBuilder timeLine = new StringBuilder();
		try {

			myURL = new URL(url.toString());
			HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
			myURLConnection.setRequestProperty("Authorization", header);
			BufferedReader tweetString = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			timeLine.append(tweetString.readLine());
			tweets = convertGeoResponseToTweet(timeLine.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tweets;
	}

	private List<GeoSearchDTO> convertGeoResponseToTweet(String response) {
		try {
			JSONObject status = new JSONObject(response);
			JSONArray geoarray = status.getJSONObject("result").getJSONArray("places");
			List<GeoSearchDTO> GeoSearch = new ArrayList<GeoSearchDTO>();

			for (int i = 0; i < geoarray.length(); i++) {
				GeoSearchDTO geo = new GeoSearchDTO();
				JSONObject geoJson = geoarray.getJSONObject(i);
				geo.id = (String) geoJson.get("id");
				geo.URL = (String) geoJson.get("url");
				geo.place_type = (String) geoJson.get("place_type");
				geo.name = (String) geoJson.get("name");
				geo.full_name = (String) geoJson.get("full_name");
				geo.country_code = (String) geoJson.get("country_code");
				geo.country = (String) geoJson.get("country");
				geo.centroid = geoJson.getJSONArray("centroid");
				geo.bounding_box.type = (String)geoJson.getJSONObject("bounding_box").get("type");
				geo.bounding_box.coordinates = (double[][][])geoJson.getJSONObject("bounding_box").get("coordinates");
				
				JSONArray contained_within = geoJson.getJSONArray("contained_within");
				ContainedWithinTwitter containedWithin = new ContainedWithinTwitter();
				List<ContainedWithinTwitter> containedWithinList = new ArrayList<ContainedWithinTwitter>();
				for (int j = 0; j < contained_within.length(); j++) {
					JSONObject containJson = contained_within.getJSONObject(i);
					containedWithin.id = (String)containJson.get("id");
					containedWithin.URL = (String) containJson.get("url");
					containedWithin.place_type = (String) containJson.get("place_type");
					containedWithin.name = (String) containJson.get("name");
					containedWithin.full_name = (String) containJson.get("full_name");
					containedWithin.country_code = (String) containJson.get("country_code");
					containedWithin.country = (String) containJson.get("country");
					containedWithin.centroid = (double[]) containJson.get("centriod");
					containedWithin.bounding_box.type = (String)containJson.getJSONObject("bounding_box").get("type");
					containedWithin.bounding_box.coordinates = (double[][][])containJson.getJSONObject("bounding_box").get("coordinates");
					containedWithinList.add(containedWithin);
				}
				geo.AreaContained = containedWithinList;
				GeoSearch.add(geo);
			}
			return GeoSearch;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<TweetDTO> convertResponseToTweet(String response) {
		try {
			JSONObject status = new JSONObject(response);
			JSONArray geoarray = status.getJSONObject("result").getJSONArray("places");
			List<TweetDTO> localTweet = new ArrayList<TweetDTO>();

			for (int i = 0; i < geoarray.length(); i++) {
				TweetDTO tweet = new TweetDTO();
				JSONObject tweetJson = geoarray.getJSONObject(i);
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
				localTweet.add(tweet);
			}
			return localTweet;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String TwitterSearchURLBuilder(List<String> urlParameters) {
		StringBuilder TwitterURL = new StringBuilder("q=");
		for (String param : urlParameters) {
			if (param.contains("@")) {
				TwitterURL = TwitterURL.append("%40").append(param.substring(1));
			} else if (param.contains("#")) {
				TwitterURL = TwitterURL.append("%23").append(param.substring(1));
			} else if (param.contains(" OR ")) {
				TwitterURL = TwitterURL.append(param.replace(" OR ", "%20OR%20"));
			} else if (param.startsWith("\"") && param.endsWith("\"")) {
				TwitterURL = TwitterURL.append(param.replace(" ", "%20"));
			} else if (param.contains(" -")) {
				TwitterURL = TwitterURL.append(param.replace(" -", "%20-"));
			} else if (param.contains("from:")) {
				TwitterURL = TwitterURL.append(param.replace("from:", "from%3A"));
			} else if (param.contains("to:")) {
				TwitterURL = TwitterURL.append(param.replace("to:", "to%3A"));
			} else if (param.contains("list:")) {
				TwitterURL = TwitterURL.append(param.replace("list:", "list%3A").replace("/", "%2"));
			} else if (param.contains(" filter:safe")) {
				TwitterURL = TwitterURL.append(param.replace(" filter:safe", "%20filter%3Asafe"));
			} else if (param.contains(" filter:media")) {
				TwitterURL = TwitterURL.append(param.replace(" filter:media", "%20filter%3Amedia"));
			} else if (param.contains(" -filter:retweets")) {
				TwitterURL = TwitterURL.append(param.replace(" -filter:retweets", "%20-filter%3Aretweets"));
			} else if (param.contains(" filter:native_video")) {
				TwitterURL = TwitterURL.append(param.replace(" filter:native_video", "%20filter%3Anative_video"));
			} else if (param.contains(" filter:periscope")) {
				TwitterURL = TwitterURL.append(param.replace(" filter:periscope", "%20filter%3Aperiscope"));
			} else if (param.contains(" filter:vine")) {
				TwitterURL = TwitterURL.append(param.replace(" filter:vine", "%20filter%3Avine"));
			} else if (param.contains(" filter:images")) {
				TwitterURL = TwitterURL.append(param.replace(" filter:images", "%20filter%3Aimages"));
			} else if (param.contains(" filter:twimg")) {
				TwitterURL = TwitterURL.append(param.replace(" filter:twimg", "%20filter%3Atwimg"));
			} else if (param.contains(" filter:links")) {
				TwitterURL = TwitterURL.append(param.replace(" filter:links", "%20filter%3Alinks"));
			} else if (param.contains("url:")) {
				TwitterURL = TwitterURL.append(param.replace("url:", "url%3"));
			} else if (param.contains(" url:")) {
				TwitterURL = TwitterURL.append(param.replace(" url:", "%20url%3"));
			} else if (param.contains(" since:")) {
				TwitterURL = TwitterURL.append(param.replace(" since:", "%20since%3A"));
			} else if (param.contains(" until:")) {
				TwitterURL = TwitterURL.append(param.replace(" until:", "%20until%3A"));
			} else if (param.contains(" :)")) {
				TwitterURL = TwitterURL.append(param.replace(" :)", "%20%3A)"));
			} else if (param.contains(" :(")) {
				TwitterURL = TwitterURL.append(param.replace(" :(", "%20%3A("));
			} else if (param.contains(" ?")) {
				TwitterURL = TwitterURL.append(param.replace(" :)", "%20%3F"));
			} else if (param.contains("place")) {
				TwitterURL = TwitterURL.append(param.replace("place", "place%3A"));
			} else {
				TwitterURL = TwitterURL.append("%20").append(param);
			}
			TwitterURL = TwitterURL.append("%20");
		}
		return TwitterURL.toString();

	}
	
	
	private Object TwitterGeoSearchURLBuilder(List<String> urlParameters) {
		StringBuilder TwitterURL = new StringBuilder();
		for (String param : urlParameters) {
			TwitterURL.append(param);
			TwitterURL.append("&");
		}
		TwitterURL.deleteCharAt(TwitterURL.length()-1);
		return TwitterURL.toString();
	}

}
