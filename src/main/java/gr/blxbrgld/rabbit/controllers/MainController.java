package gr.blxbrgld.rabbit.controllers;

import gr.blxbrgld.rabbit.aop.LogMethodInvocation;
import gr.blxbrgld.rabbit.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * RabbitController Class
 * @author blxbrgld
 */
@Controller
public class MainController {

    @LogMethodInvocation
    @RequestMapping("/")
    public String home() { //TODO Do Something Meaningful With This Page
        return Constants.HOME_PAGE;
    }
}
