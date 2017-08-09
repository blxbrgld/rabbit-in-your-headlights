package gr.blxbrgld.rabbit.controllers;

import gr.blxbrgld.rabbit.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Management Controller For RabbitMQ Server
 * @author blxbrgld
 */
@Slf4j
@Controller
@RequestMapping("management")
public class ManagementController {

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    /*
     * A Convenience Wrapper Providing Access To The REST Methods Of "rabbitDomain:rabbitPort/api/"
     */
    @Autowired
    private RabbitManagementTemplate managementTemplate;

    @RequestMapping("exchanges")
    public String getExchanges(Model model) {
        List<String> names = managementTemplate.getExchanges(virtualHost)
            .stream()
            .map(e -> "".equals(e.getName()) ? "(AMQP default)" : e.getName())
            .collect(Collectors.toList());
        model.addAttribute("exchanges", names);
        return Constants.EXCHANGES_PAGE;
    }
}
