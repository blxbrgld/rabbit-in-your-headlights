package gr.blxbrgld.rabbit.services;

import gr.blxbrgld.rabbit.aop.LogMethodExecutionTime;
import gr.blxbrgld.rabbit.aop.LogMethodInvocation;
import gr.blxbrgld.rabbit.enums.ExchangeType;
import gr.blxbrgld.rabbit.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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
@Slf4j
@Service
public class RabbitServiceImpl implements RabbitService {

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private SimpleMessageListenerContainer container;

    /*
     * A Convenience Wrapper Providing Access To The REST Methods Of "rabbitDomain:rabbitPort/api/"
     */
    @Autowired
    private RabbitManagementTemplate managementTemplate;

    private static final boolean durable = false, autoDelete = false, exclusive = false; //TODO These Can Also Be Dynamically Declared

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getExchangeNames() {
        return managementTemplate.getExchanges(virtualHost)
            .stream()
            .map(e -> "".equals(e.getName()) ? "(AMQP default)" : e.getName())
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public gr.blxbrgld.rabbit.domain.Exchange getExchange(String name) {
        gr.blxbrgld.rabbit.domain.Exchange exchange = new gr.blxbrgld.rabbit.domain.Exchange();
        BeanUtils.copyProperties(managementTemplate.getExchange(virtualHost, name), exchange);
        exchange.setQueues(getExchangeQueues(name));
        return exchange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Integer> getExchangeQueues(String name) {
        Map<String, Integer> result = new LinkedHashMap<>();
        List<Binding> bindings = managementTemplate.getBindingsForExchange(virtualHost, name);
        for(Binding binding : bindings) {
            String queueName = binding.getDestination();
            result.put(queueName, queueCountOfMessages(queueName));
        }
        return result;
    }

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
            log.debug("Exchange With Name '{}' Already Exists.", name);
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
    @Override
    public List<String> getQueueNames() {
        return managementTemplate.getQueues(virtualHost)
            .stream()
            .map(Queue::getName)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public gr.blxbrgld.rabbit.domain.Queue getQueue(String name) {
        gr.blxbrgld.rabbit.domain.Queue queue = new gr.blxbrgld.rabbit.domain.Queue();
        BeanUtils.copyProperties(managementTemplate.getQueue(virtualHost, name), queue);
        managementTemplate.getBindings(virtualHost).forEach(
            binding -> {
                if(name.equals(binding.getDestination())) {
                    queue.setExchangeFrom(binding.getExchange());
                    queue.setRoutingKey(binding.getRoutingKey());
                }
            }
        );
        queue.setCountOfMessages(queueCountOfMessages(name));
        return queue;
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
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", Constants.DEAD_LETTER_EXCHANGE);
            arguments.put("x-dead-letter-routing-key", Constants.DEAD_LETTER_QUEUE); //Do Not Use Message's Routing Key
            managementTemplate.addQueue(virtualHost, new Queue(queueName, durable, exclusive, autoDelete, arguments));
            // Create The Binding
            Binding binding = new Binding(
                queueName,
                Binding.DestinationType.QUEUE,
                exchangeName,
                queueName, // The Routing Key
                null // Arguments Are Not Needed
            );
            rabbitAdmin.declareBinding(binding);
            container.addQueueNames(queueName); //Add Queue To SimpleMessageListenerContainer
        } else {
            log.debug("Queue With Name '{}' Already Exists.", queueName);
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
    @LogMethodInvocation
    @LogMethodExecutionTime
    @Override
    public Map<String, Map<String, Integer>> getQueues() {
        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
        for(String exchangeName : getExchangeNames()) {
            result.put(exchangeName, getExchangeQueues(exchangeName));
        }
        return result;
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
    public void produceTrends() {
        String queueName = "trends-queue";
        declareQueue(queueName, Constants.DEFAULT_DIRECT_EXCHANGE, ExchangeType.DIRECT); //Declare Before Proceeding
        int counter = 0; //TODO Temporary Due Twitter's Rate Limits
        for(Trend trend : twitterService.trends().getTrends()) {
            if(counter < 3) {
                List<Tweet> tweets = twitterService.search(trend.getQuery());
                for (Tweet tweet : tweets) {
                    //Not All Attributes Are Needed, Converting To Light Tweet Objects
                    gr.blxbrgld.rabbit.domain.Tweet lightTweet = new gr.blxbrgld.rabbit.domain.Tweet();
                    BeanUtils.copyProperties(tweet, lightTweet);
                    log.info("Trend = {}, Tweet = {}", new Object[] { trend.getName(), lightTweet });
                    rabbitTemplate.convertAndSend(queueName, lightTweet);
                    break; //Only The First Result Of Every Trend
                }
            }
            counter++;
        }
    }
}
