package gr.blxbrgld.rabbit.services;

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
    List<String> getExchanges();

    //TODO Javadoc
    Map<String, Map<String, Integer>> getQueues();

    /**
     * Get A Queue's Count Of Messages Given It's Name
     * @param queueName Queue's Name
     * @return Queue's Count Of Messages
     */
    Integer queueCountOfMessages(String queueName);

    //TODO Javadoc
    void trendsToQueue();
}
