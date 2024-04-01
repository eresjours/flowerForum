package flower.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class indexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
