package gr.blxbrgld.rabbit.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RabbitRestController Class
 * @author blxbrgld
 */
@Slf4j
@RestController
public class RabbitRestController {

    @RequestMapping("/")
    public String home() { //TODO Delete This
        log.debug("home() Endpoint Invoked.");
        return "rabbit-in-your-headlights"; //Up And Running
    }
}
