package gr.blxbrgld.rabbit.services;

import gr.blxbrgld.rabbit.domain.Exchange;
import gr.blxbrgld.rabbit.domain.Queue;
import gr.blxbrgld.rabbit.enums.ExchangeType;

import java.util.List;
import java.util.Map;

/**
 * Rabbit Service Interface
 * @author blxbrgld
 */
public interface RabbitService {

    /**
     * Get A List Of All Registered Exchange Names
     * @return List Of Exchange Names
     */
    List<String> getExchangeNames();

    /**
     * Get An gr.blxbrgld.rabbit.domain.Exchange Object Given It's Name
     * @param name Exchange's Name
     * @return Exchange Object
     */
    Exchange getExchange(String name);

    /**
     * Get A Map Of All Exchange's Queues Along With The Count Of Their Messages
     * @param name Exchange's Name
     * @return Map Of Exchange's Queues
     */
    Map<String, Integer> getExchangeQueues(String name);

    /**
     * Declare An Exchange Given It's Name And Type. The Method Checks If The Exchange Already Exist In The Application's Virtual Host
     * @param name Exchange's Name
     * @param type Exchange's Type
     */
    void declareExchange(String name, ExchangeType type);

    /**
     * Check If The Exchange With The Given Name Already Exists
     * @param name Exchange's Name
     * @return boolean Indicating If The Exchange Exists Or Not
     */
    boolean exchangeExists(String name);

    /**
     * Delete An Exchange (And It's Related Queues) Given It's Name
     * @param name Exchange's Name
     */
    void deleteExchange(String name);

    /**
     * Get A List Of All Registered Queue Names
     * @return List Of Queue Names
     */
    List<String> getQueueNames();

    /**
     * Get An gr.blxbrgld.rabbit.domain.Queue Object Given It's Name
     * @param name Queue's Name
     * @return Queue Object
     */
    Queue getQueue(String name);

    /**
     * Declare A Queue And Bind It To The Given Exchange. The Method Checks If The Queue and Exchange Already Exist In The Application's Virtual Host
     * @param queueName Queue's Name
     * @param exchangeName Exchange's Name
     * @param exchangeType Exchange's Type In Order To Create It If It Does Not Already Exist
     */
    void declareQueue(String queueName, String exchangeName, ExchangeType exchangeType);

    /**
     * Check If The Queue With The Given Name Already Exists
     * @param name Queue's Name
     * @return boolean Indicating If The Queue Exists Or Not
     */
    boolean queueExists(String name);

    /**
     * Delete A Queue Given It's Name
     * @param name Queue's Name
     */
    void deleteQueue(String name);

    /**
     * Purge A Queue's Messages Given It's Name
     * @param name Queue's Name
     * @return The Number Of Purged Messages
     */
    Integer purgeQueue(String name);

    /**
     * Get A Map Of All Registered Exchanges Along With Their Related Queues Containing Messages
     * @return Map Of Exchanges and Queue
     */
    Map<String, Map<String, Integer>> getQueues();

    /**
     * Get A Queue's Count Of Messages Given It's Name
     * @param queueName Queue's Name
     * @return Queue's Count Of Messages
     */
    Integer queueCountOfMessages(String queueName);

    //TODO Javadoc
    void produceTrends();
}
