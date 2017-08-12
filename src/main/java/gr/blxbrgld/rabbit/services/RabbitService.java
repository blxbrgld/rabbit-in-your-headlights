package gr.blxbrgld.rabbit.services;

import java.util.List;

/**
 * Rabbit Service Interface
 * @author blxbrgld
 */
public interface RabbitService {

    List<String> getExchanges();

    void trendsToQueue();
}
