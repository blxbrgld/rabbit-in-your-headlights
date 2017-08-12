package gr.blxbrgld.rabbit.controllers;

import gr.blxbrgld.rabbit.enums.TimelineType;
import gr.blxbrgld.rabbit.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Trends;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Twitter Controller
 * @author blxbrgld
 */
@RestController
@RequestMapping("twitter")
public class TwitterController {

    @Autowired
    private TwitterTemplate twitterTemplate;

    @RequestMapping("friends")
    public List<TwitterProfile> friends() {
        return twitterTemplate.friendOperations().getFriends();
    }

    @RequestMapping("followers")
    public List<TwitterProfile> followers() {
        return twitterTemplate.friendOperations().getFollowers();
    }

    @RequestMapping("timeline/{type}")
    public List<Tweet> timeline(@PathVariable("type") String type) {
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

    @RequestMapping("trends")
    public Trends trends() {
        return twitterTemplate.searchOperations().getLocalTrends(Constants.WORLDWIDE_WOE);
    }

    @RequestMapping("search")
    public List<Tweet> search(@RequestParam("query") String query) { //TODO Select The Number Of Results
        return twitterTemplate.searchOperations().search(query).getTweets();
    }
}
