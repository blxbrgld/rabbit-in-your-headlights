package gr.blxbrgld.rabbit.controllers;

import gr.blxbrgld.rabbit.aop.LogMethodInvocation;
import gr.blxbrgld.rabbit.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RabbitRestController Class
 * @author blxbrgld
 */
@Slf4j
@RestController
public class RabbitRestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @LogMethodInvocation
    @RequestMapping("/")
    public String home() { //TODO Delete This
        return Constants.SUCCESS_MESSAGE; //Up And Running
    }

    @LogMethodInvocation
    @RequestMapping("/message")
    public String message(@RequestParam(value = "message", required = true) String message) { //TODO Delete This
        rabbitTemplate.convertAndSend(Constants.QUEUE_NAME, message);
        return Constants.SUCCESS_MESSAGE;
    }
}
