package gr.blxbrgld.rabbit.controllers;

import gr.blxbrgld.rabbit.services.RabbitService;
import gr.blxbrgld.rabbit.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RabbitService rabbitService;

    @RequestMapping("exchanges")
    public String getExchanges(Model model) {
        model.addAttribute("exchanges", rabbitService.getExchanges());
        return Constants.EXCHANGES_PAGE;
    }
}
