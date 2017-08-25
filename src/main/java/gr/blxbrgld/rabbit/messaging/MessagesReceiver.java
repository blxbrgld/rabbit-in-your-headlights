package gr.blxbrgld.rabbit.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Messages Receiver Class
 * @author blxbrgld
 */
@Component
@Slf4j
public class MessagesReceiver implements MessageListener {

    /*
     * When A Listener Throws An Exception, It Is Wrapped In A ListenerExecutionFailedException And,
     * Normally The Message Is Rejected And Requeued By The Broker. To Reject (And Not Requeue) Messages
     * That Fail With An Irrecoverable Error The Listener Can Throw An AmqpRejectAndDontRequeueException.
     *
     * https://docs.spring.io/spring-amqp//reference/html/_reference.html#exception-handling
     */
    @Override
    public void onMessage(Message message) {
        String body = new String(message.getBody());
        if(validMessage(body)) {
            log.info("Message Received {}.", body);
        } else { //Would Be Published To DEAD_LETTER_QUEUE
            throw new AmqpRejectAndDontRequeueException("AmqpRejectAndDontRequeueException Raised.");
        }
    }

    private boolean validMessage(String message) { //TODO Temporary For Testing
        return !"error".equalsIgnoreCase(message);
    }
}
