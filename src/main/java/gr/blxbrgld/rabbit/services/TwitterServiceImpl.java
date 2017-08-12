package gr.blxbrgld.rabbit.services;

import gr.blxbrgld.rabbit.enums.TimelineType;
import gr.blxbrgld.rabbit.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Trends;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Twitter Service Implementation
 * @author blxbrgld
 */
@Service
public class TwitterServiceImpl implements TwitterService {

    @Autowired
    private TwitterTemplate twitterTemplate;

    @Override
    public List<TwitterProfile> friends() {
        return twitterTemplate.friendOperations().getFriends();
    }

    @Override
    public List<TwitterProfile> followers() {
        return twitterTemplate.friendOperations().getFollowers();
    }

    @Override
    public List<Tweet> timeline(String type) {
        List<Tweet> results = new ArrayList<>();
        TimelineType timelineType = StringUtils.trimToNull(type)!=null && TimelineType.get(type)!=null ? TimelineType.get(type) : TimelineType.HOME;
        switch(timelineType) {
            case HOME:
                results = twitterTemplate.timelineOperations().getHomeTimeline();
                break;
            case USER:
                results = twitterTemplate.timelineOperations().getUserTimeline();
                break;
            case FAVORITES:
                results = twitterTemplate.timelineOperations().getFavorites();
                break;
            case MENTIONS:
                results = twitterTemplate.timelineOperations().getMentions();
                break;
        }
        return results;
    }

    @Override
    public Trends trends() {
        return twitterTemplate.searchOperations().getLocalTrends(Constants.WORLDWIDE_WOE);
    }

    @Override
    public List<Tweet> search(String query) {
        return twitterTemplate.searchOperations().search(query).getTweets();
    }

    @Override
    public void tweet(String text) {
        twitterTemplate.timelineOperations().updateStatus(text);
    }
}
