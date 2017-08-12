package gr.blxbrgld.rabbit.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 * Main Configuration Class
 * @author blxbrgld
 */
@SpringBootApplication
@ComponentScan("gr.blxbrgld.rabbit")
public class Application {

    @Value("${consumerKey}")
    private String consumerKey;
    @Value("${consumerSecret}")
    private String consumerSecret;
    @Value("${accessToken}")
    private String accessToken;
    @Value("${accessTokenSecret}")
    private String accessTokenSecret;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*
     * The Twitter Template Need consumer + access Tokens. These Can Be Externalized In A
     * .properties File Or Declared As JVM Parameters (For Now, The Later Solution Is Used)
     */
    @Bean
    public TwitterTemplate twitterTemplate() {
        return new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }
}
