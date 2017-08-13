package gr.blxbrgld.rabbit.services;

import gr.blxbrgld.rabbit.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Trend;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Rabbit Service Implementation
 * @author blxbrgld
 */
@Service @Slf4j
public class RabbitServiceImpl implements RabbitService {

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    /*
     * A Convenience Wrapper Providing Access To The REST Methods Of "rabbitDomain:rabbitPort/api/"
     */
    @Autowired
    private RabbitManagementTemplate managementTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getExchanges() {
        return managementTemplate.getExchanges(virtualHost)
            .stream()
            .map(e -> "".equals(e.getName()) ? "(AMQP default)" : e.getName())
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Map<String, Integer>> getQueues() {
        Map<String, Map<String, Integer>> outer = new HashMap<>();
        for(String exchangeName : getExchanges()) {
            Map<String, Integer> inner = new HashMap<>();
            List<Binding> bindings = managementTemplate.getBindingsForExchange(virtualHost, exchangeName);
            for(Binding binding : bindings) {
                String queueName = binding.getDestination();
                Integer queueCount = queueCountOfMessages(queueName);
                if(queueCount>0) { //Keeping Only Queues With Messages
                    inner.put(queueName, queueCount);
                }
            }
            outer.put(exchangeName, inner);
        }
        return outer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer queueCountOfMessages(String queueName) {
        Properties properties = rabbitAdmin.getQueueProperties(queueName);
        return Integer.parseInt(properties.get("QUEUE_MESSAGE_COUNT").toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trendsToQueue() {
        int counter = 0; //TODO Delete This
        for(Trend trend : twitterService.trends().getTrends()) {
            if(counter < 3) {
                List<Tweet> tweets = twitterService.search(trend.getQuery());
                for (Tweet tweet : tweets) {
                    //Not All Attributes Are Needed, Converting To Light Tweet Objects
                    gr.blxbrgld.rabbit.domain.Tweet lightTweet = new gr.blxbrgld.rabbit.domain.Tweet();
                    BeanUtils.copyProperties(tweet, lightTweet);
                    log.info("Trend = {}, Tweet = {}", new Object[] { trend.getName(), lightTweet });
                    rabbitTemplate.convertAndSend(Constants.QUEUE_NAME, lightTweet);
                    break; //Only The First Result Of Every Trend
                    //TODO Create Different Topic For Every Trend?
                }
            }
            counter++;
        }
    }
}
