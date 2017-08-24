package gr.blxbrgld.rabbit.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Receiver For Messages
 * @author blxbrgld
 */
@Component
@Slf4j
public class MessagesReceiver {

    public void handleMessage(String message) {
        log.info("Message Received {}.", message);
    }
}
