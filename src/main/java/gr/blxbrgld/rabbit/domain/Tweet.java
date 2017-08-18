package gr.blxbrgld.rabbit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Tweet Domain Object (A Lighter Version of org.springframework.social.twitter.api.Tweet)
 * @author blxbrgld
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Tweet implements Serializable {

    private long id;
    private String text;
    private Date createdAt;
    private String fromUser;
    private Integer retweetCount;
    private Integer favoriteCount;
}
