package Controller;

import java.util.Date;
import java.util.List;

import CentralObjects.TweetDTO;

public interface IController {
	String AuthFacebook(SocialMediaUserAuth auth);

	List<TweetDTO> GetTwitterTimelineWithScreenname(SocialMediaUserAuth auth, String screenName, int count);

	List<TweetDTO> GetTwitterTimelineWithScreennameDateRange(SocialMediaUserAuth auth, String screenName, Date startTime, Date endTime);
	
	List<TweetDTO> GetTwitterTimelineWithScreennameFromDate(SocialMediaUserAuth auth, String screenName, Date startTime);

	String AuthInstagram(SocialMediaUserAuth auth);
}
