package gr.blxbrgld.rabbit.configuration;

import gr.blxbrgld.rabbit.messaging.Receiver;
import gr.blxbrgld.rabbit.utils.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Messaging Configuration Class
 * @author blxbrgld
 */
@Configuration
public class MessagingConfiguration {

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(Constants.QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public Queue queue() {
        return new Queue(Constants.QUEUE_NAME, false); //durable = false
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(Constants.TOPIC_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder
            .bind(queue)
            .to(topicExchange)
            .with(Constants.QUEUE_NAME);
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public RabbitManagementTemplate managementTemplate() {
        return new RabbitManagementTemplate(); //Construct a template using uri "localhost:15672/api/" and user guest/guest
    }
}
