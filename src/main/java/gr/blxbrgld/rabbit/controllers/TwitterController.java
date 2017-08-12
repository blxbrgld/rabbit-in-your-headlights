package gr.blxbrgld.rabbit.controllers;

import gr.blxbrgld.rabbit.services.TwitterService;
import gr.blxbrgld.rabbit.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Trends;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Twitter Controller
 * @author blxbrgld
 */
@RestController
@RequestMapping("twitter")
public class TwitterController {

    @Autowired
    private TwitterService twitterService;

    @RequestMapping("friends")
    public List<TwitterProfile> friends() {
        return twitterService.friends();
    }

    @RequestMapping("followers")
    public List<TwitterProfile> followers() {
        return twitterService.followers();
    }

    @RequestMapping("timeline/{type}")
    public List<Tweet> timeline(@PathVariable("type") String type) {
        return twitterService.timeline(type);
    }

    @RequestMapping("trends")
    public Trends trends() {
        return twitterService.trends();
    }

    @RequestMapping("search")
    public List<Tweet> search(@RequestParam("query") String query) { //TODO Select The Number Of Results
        return twitterService.search(query);
    }

    @RequestMapping("tweet")
    public String tweet(@RequestParam("text") String text) {
        twitterService.tweet(text);
        return Constants.SUCCESS_MESSAGE;
    }
}
