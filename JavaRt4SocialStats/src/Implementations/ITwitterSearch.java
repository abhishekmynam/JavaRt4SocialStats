package Implementations;

import java.util.List;

import CentralObjects.GeoSearchDTO;
import CentralObjects.TweetDTO;
import Controller.SocialMediaUserAuth;

public interface ITwitterSearch {
	List<TweetDTO> GetTwitterSearchWithKeyWord(SocialMediaUserAuth auth, List<String> searchString);
	List<GeoSearchDTO> GetTwitterGeoSearchWithKeyWord(SocialMediaUserAuth auth, List<String> searchString);
}
