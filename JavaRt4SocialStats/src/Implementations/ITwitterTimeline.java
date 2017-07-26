package Implementations;

import java.util.Date;
import java.util.List;

import CentralObjects.TweetDTO;
import Controller.SocialMediaUserAuth;

public interface ITwitterTimeline {

	List<TweetDTO> GetTwitterTimelineWithScreenname(SocialMediaUserAuth auth, String screenName, int count);

	List<TweetDTO> GetTwitterTimelineWithScreennameDateRange(SocialMediaUserAuth auth, String screenName,
			Date startTime, Date endTime);

}