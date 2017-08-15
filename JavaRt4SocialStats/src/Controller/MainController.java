package Controller;

import java.util.Date;
import java.util.List;

import CentralObjects.GeoSearchDTO;
import CentralObjects.TweetDTO;
import Implementations.*;

public class MainController implements IController {

	public List<TweetDTO> GetTwitterTimelineWithScreenname(SocialMediaUserAuth auth, String screenName, int count) {
		ITwitterTimeline twitter = new TwitterTimeline();
		return twitter.GetTwitterTimelineWithScreenname(auth, screenName, count);
	}

	public List<TweetDTO> GetTwitterLatestTimelineforScreenname(SocialMediaUserAuth auth, String screenName) {
		return GetTwitterTimelineWithScreenname(auth, screenName, 200);
	}

	public List<TweetDTO> GetTwitterTimelineWithScreennameDateRange(SocialMediaUserAuth auth, String screenName,
			Date startTime, Date endTime) {
		ITwitterTimeline twitter = new TwitterTimeline();
		return twitter.GetTwitterTimelineWithScreennameDateRange(auth, screenName, startTime, endTime);
	}

	public List<TweetDTO> GetTwitterTimelineWithScreennameFromDate(SocialMediaUserAuth auth, String screenName,
			Date startTime) {
		return GetTwitterTimelineWithScreennameDateRange(auth, screenName, startTime,
				java.sql.Date.valueOf(java.time.LocalDate.now()));
	}

	public List<TweetDTO> GetTwitterSearchWithKeyWord(SocialMediaUserAuth auth, List<String> searchString) {
		ITwitterSearch twitter = new TwittersSearch();
		return twitter.GetTwitterSearchWithKeyWord(auth, searchString);
	}
	
	public List<GeoSearchDTO> GetTwitterGeoSearchWithKeyWord(SocialMediaUserAuth auth, List<String> searchString) {
		ITwitterSearch twitter = new TwittersSearch();
		return twitter.GetTwitterGeoSearchWithKeyWord(auth, searchString);
	}

	public String AuthInstagram(SocialMediaUserAuth auth) {
		return null;
	}

	public String AuthFacebook(SocialMediaUserAuth auth) {
		return null;
	}

}
