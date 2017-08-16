package gr.blxbrgld.rabbit.services;

import gr.blxbrgld.rabbit.aop.LogMethodInvocation;
import gr.blxbrgld.rabbit.enums.ExchangeType;
import gr.blxbrgld.rabbit.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Trend;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private static boolean durable = false, autoDelete = false, exclusive = false; //TODO These Can Also Be Dynamically Declared

    /**
     * {@inheritDoc}
     */
    @LogMethodInvocation
    @Override
    public void declareExchange(String name, ExchangeType type) {
        if(!exchangeExists(name)) {
            log.info("Exchange With Name '{}' Does Not Exist. Trying To Create It With Type '{}'.", new Object[] { name, type.getCode() });
            Exchange exchange;
            switch (type) {
                case TOPIC:
                    exchange = new TopicExchange(name, durable, autoDelete);
                    break;
                case FANOUT:
                    exchange = new FanoutExchange(name, durable, autoDelete);
                    break;
                case HEADERS:
                    exchange = new HeadersExchange(name, durable, autoDelete);
                    break;
                case DIRECT:
                default: //The default Case Creates A Direct Exchange Too
                    exchange = new DirectExchange(name, durable, autoDelete);
            }
            managementTemplate.addExchange(virtualHost, exchange);
        } else {
            log.info("Exchange With Name '{}' Already Exists.", name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exchangeExists(String name) {
        return managementTemplate.getExchange(virtualHost, name) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteExchange(String name) {
        // Delete Exchange's Queues
        List<Binding> bindings = managementTemplate.getBindingsForExchange(virtualHost, name);
        for(Binding binding : bindings) {
            deleteQueue(binding.getDestination());
        }
        // Delete The Exchange
        managementTemplate.deleteExchange(virtualHost, managementTemplate.getExchange(virtualHost, name));
    }

    /**
     * {@inheritDoc}
     */
    @LogMethodInvocation
    @Override
    public void declareQueue(String queueName, String exchangeName, ExchangeType exchangeType) {
        declareExchange(exchangeName, exchangeType); //We Should Bind the Queue To An Exchange, So The Exchange Must Already Exist
        if(!queueExists(queueName)) {
            // Create The Queue
            log.info("Queue With Name '{}' Does Not Exist. Trying To Create It.", queueName);
            managementTemplate.addQueue(virtualHost, new Queue(queueName, durable, exclusive, autoDelete));
            // Create The Binding
            Binding binding = new Binding(
                queueName,
                Binding.DestinationType.QUEUE,
                exchangeName,
                queueName, // The Routing Key
                null // Arguments Are Not Needed
            );
            rabbitAdmin.declareBinding(binding);
        } else {
            log.info("Queue With Name '{}' Already Exists.", queueName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean queueExists(String name) {
        return managementTemplate.getQueue(virtualHost, name) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteQueue(String name) {
        managementTemplate.deleteQueue(virtualHost, managementTemplate.getQueue(virtualHost, name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer purgeQueue(String name) {
        Integer countOfMessages = queueCountOfMessages(name); //Get The Number Of Existing Messages Before Purging
        managementTemplate.purgeQueue(virtualHost, managementTemplate.getQueue(virtualHost, name));
        return countOfMessages;
    }

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
    @LogMethodInvocation
    @Override
    public Map<String, Map<String, Integer>> getQueues() {
        Map<String, Map<String, Integer>> outer = new LinkedHashMap<>();
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
