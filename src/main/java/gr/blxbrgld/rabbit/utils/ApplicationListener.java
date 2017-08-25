package gr.blxbrgld.rabbit.utils;

import gr.blxbrgld.rabbit.services.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Listener(s) Class
 * @author blxbrgld
 */
@Slf4j
@Component
public class ApplicationListener {

    @Autowired
    private SimpleMessageListenerContainer container;

    @Autowired
    private RabbitService rabbitService;

    /**
     * By Not Adding Any Queues To The SimpleMessageListenerContainer During @Bean Creation, On Application Startup The Container
     * Will Be Created With No Queues Bound To It. This Event Listener Is Used As A Way To Add All Existing Queues To The Container
     */
    @EventListener(ContextRefreshedEvent.class)
    private void contextRefresh() {
        log.info("Adding All Existing RabbitMQ Queues To SimpleMessageListenerContainer.");
        List<String> existingQueues = rabbitService.getQueueNames() //DEAD_LETTER_QUEUE Should Not Be Added To Those The Listener Handles
            .stream()
            .filter(s -> Constants.DEAD_LETTER_QUEUE.equals(s))
            .collect(Collectors.toList());
        container.addQueueNames(existingQueues.toArray(new String[existingQueues.size()]));
    }
}