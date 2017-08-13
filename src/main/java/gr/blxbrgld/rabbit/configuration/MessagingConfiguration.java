package gr.blxbrgld.rabbit.configuration;

import gr.blxbrgld.rabbit.utils.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Messaging Configuration Class
 * @author blxbrgld
 */
@Configuration
public class MessagingConfiguration {

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.http.port}")
    private String port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    public Queue queue() {
        return new Queue(Constants.QUEUE_NAME, false); //durable = false, No Need To Survive Broker Restarts
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(Constants.EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder
            .bind(queue)
            .to(topicExchange)
            .with(Constants.QUEUE_NAME);
    }

    @Bean
    public RabbitManagementTemplate managementTemplate() { //Construct A Template for "http://username:password@host:port/api/"
        String uri = new StringBuilder()
            .append("http://")
            .append(username)
            .append(":")
            .append(password)
            .append("@")
            .append(host)
            .append(":")
            .append(port)
            .append("/api/")
            .toString();
        return new RabbitManagementTemplate(uri);
    }
}
