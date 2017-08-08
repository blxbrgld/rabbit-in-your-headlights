package gr.blxbrgld.rabbit.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ Receiver
 * @author blxbrgld
 */
@Component
@Slf4j
public class Receiver {

    public void receiveMessage(String message) {
        log.info("Received <{}>", message);
    }
}
