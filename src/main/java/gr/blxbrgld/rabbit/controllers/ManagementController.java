package gr.blxbrgld.rabbit.controllers;

import gr.blxbrgld.rabbit.services.RabbitService;
import gr.blxbrgld.rabbit.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Management Controller For RabbitMQ Server
 * @author blxbrgld
 */
@Controller
@RequestMapping("management")
public class ManagementController {

    @Autowired
    private RabbitService rabbitService;

    @RequestMapping("exchanges")
    public String getExchanges(Model model) {
        model.addAttribute("exchanges", rabbitService.getQueues());
        return Constants.EXCHANGES_PAGE;
    }

    @RequestMapping("exchange")
    public String getExchange(@RequestParam("name") String name, Model model) {
        model.addAttribute("exchange", rabbitService.getExchange(name));
        return Constants.EXCHANGE_PAGE;
    }
}
