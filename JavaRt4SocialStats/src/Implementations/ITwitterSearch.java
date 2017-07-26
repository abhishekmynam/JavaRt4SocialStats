package Implementations;

import java.util.List;

import CentralObjects.TweetDTO;
import Controller.SocialMediaUserAuth;

public interface ITwitterSearch {
	List<TweetDTO> GetTwitterSearchWithKeyWord(SocialMediaUserAuth auth, List<String> searchString);
}
