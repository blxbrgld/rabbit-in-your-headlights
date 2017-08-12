package gr.blxbrgld.rabbit.services;

import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rabbit Service Implementation
 * @author blxbrgld
 */
@Service
public class RabbitServiceImpl implements RabbitService {

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    /*
     * A Convenience Wrapper Providing Access To The REST Methods Of "rabbitDomain:rabbitPort/api/"
     */
    @Autowired
    private RabbitManagementTemplate managementTemplate;

    @Override
    public List<String> getExchanges() {
        return managementTemplate.getExchanges(virtualHost)
            .stream()
            .map(e -> "".equals(e.getName()) ? "(AMQP default)" : e.getName())
            .collect(Collectors.toList());
    }
}
