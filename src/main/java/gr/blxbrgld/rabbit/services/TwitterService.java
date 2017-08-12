package gr.blxbrgld.rabbit.services;

import org.springframework.social.twitter.api.Trends;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import java.util.List;

/**
 * Twitter Service Interface
 * @author blxbrgld
 */
public interface TwitterService {

    List<TwitterProfile> friends();

    List<TwitterProfile> followers();

    List<Tweet> timeline(String type);

    Trends trends();

    List<Tweet> search(String query);

    void tweet(String text);
}
