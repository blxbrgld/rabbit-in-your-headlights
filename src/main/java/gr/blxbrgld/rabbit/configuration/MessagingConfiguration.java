package gr.blxbrgld.rabbit.configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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
    public RabbitManagementTemplate managementTemplate() { //Construct A Template for "http://username:password@host:port/api/"
        return new RabbitManagementTemplate(
            new StringBuilder()
                .append("http://")
                .append(username)
                .append(":")
                .append(password)
                .append("@")
                .append(host)
                .append(":")
                .append(port)
                .append("/api/")
                .toString()
        );
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
