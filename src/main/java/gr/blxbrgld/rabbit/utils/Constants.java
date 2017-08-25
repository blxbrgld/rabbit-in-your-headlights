package gr.blxbrgld.rabbit.utils;

/**
 * Constants Class
 * @author blxbrgld
 */
public class Constants {

    // Labels and Messages
    public static final String SUCCESS_MESSAGE = "SUCCESS";
    public static final String ERROR_MESSAGE = "ERROR";

    // RabbitMQ
    public static final String DEFAULT_DIRECT_EXCHANGE = "amq.direct";
    public static final String DEFAULT_FANOUT_EXCHANGE = "amq.fanout";
    public static final String DEFAULT_HEADERS_EXCHANGE = "amq.headers";
    public static final String DEFAULT_TOPIC_EXCHANGE = "amq.topic";
    public static final String DEAD_LETTER_EXCHANGE = "dead-letter-exchange";
    public static final String DEAD_LETTER_QUEUE = "dead-letter-queue";

    // Twitter
    public static final long WORLDWIDE_WOE = 23424833L; // Yahoo Where On Earth ID representing Greece

    // View Names
    public static final String HOME_PAGE = "home";
    public static final String ERROR_PAGE = "error";
    public static final String EXCHANGES_PAGE = "management/exchanges";
    public static final String EXCHANGE_PAGE = "management/exchange";
    public static final String QUEUE_PAGE = "management/queue";
}
