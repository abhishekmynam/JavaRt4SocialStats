package Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CentralObjects.TweetDTO;

public class MainClass {

	public static void main(String[] args) {
		MainController mc = new MainController();
		List<TweetDTO> x = new ArrayList<TweetDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date d1 = null;
		try {
			d1 = sdf.parse("21/07/2017");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date d2 = null;
		try {
			d2 = sdf.parse("23/07/2017");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		SocialMediaUserAuth auth = new SocialMediaUserAuth();
		Map<String, String> keys = new HashMap<String, String>();
		keys.put("ConsumerKey", "I29lYEvNNhq8jnkha6Kwbzx7L");
		keys.put("ConsumerSecret", "xNXNpNJRP0hYgt4dLRzeB4ve5gdVw52gPGI8pjWJzqtLYNLWOR");
		keys.put("AuthToken", "W1QcrtoUlHSOxicSX7f3QuskUO7ZKRtqu6U330w4");
		keys.put("AuthTokenSecret", "T5UIgLgDPZms7oqZnxMwm6M3S54AEngu0yVAfgcZPvaVG");
		auth.authKeys = keys;
		
		x = mc.GetTwitterTimelineWithScreennameDateRange(auth, "salesforce",d1,d2);
		for (TweetDTO t : x) {
			System.out.println(t.tweetDate);
		}

	}

}
