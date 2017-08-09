package gr.blxbrgld.rabbit.controllers;

import gr.blxbrgld.rabbit.enums.TimelineType;
import gr.blxbrgld.rabbit.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Trends;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
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
    private ConnectionRepository connectionRepository;

    @Autowired
    private Twitter twitter;

    /*
    @RequestMapping("connect")
    public TwitterProfile connect() {
        Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
        if(connection==null) {
            //return "redirect:/connect/twitter";
        }
        return connection.getApi().userOperations().getUserProfile();
    }
    */

    @RequestMapping("friends")
    public List<TwitterProfile> friends() { //TODO MissingAuthorizationException: Authorization is required for the operation
        return twitter.friendOperations().getFriends();
    }

    @RequestMapping("followers")
    public List<TwitterProfile> followers() { //TODO MissingAuthorizationException: Authorization is required for the operation
        return twitter.friendOperations().getFollowers();
    }

    @RequestMapping("timeline/{type}")
    public List<Tweet> timeline(@PathVariable("type") String type) { //TODO MissingAuthorizationException: Authorization is required for the operation
        List<Tweet> results = new ArrayList<>();
        TimelineType timelineType = StringUtils.trimToNull(type)!=null && TimelineType.get(type)!=null ? TimelineType.get(type) : TimelineType.HOME;
        switch(timelineType) {
            case HOME:
                results = twitter.timelineOperations().getHomeTimeline();
                break;
            case USER:
                results = twitter.timelineOperations().getUserTimeline();
                break;
            case FAVORITES:
                results = twitter.timelineOperations().getFavorites();
                break;
            case MENTIONS:
                results = twitter.timelineOperations().getMentions();
                break;
        }
        return results;
    }

    @RequestMapping("trends")
    public Trends trends() {
        return twitter.searchOperations().getLocalTrends(Constants.WORLDWIDE_WOE);
    }

    @RequestMapping("search")
    public List<Tweet> search(@RequestParam("query") String query) { //TODO Select The Number Of Results
        return twitter.searchOperations().search(query).getTweets();
    }
}
