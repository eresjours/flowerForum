package flower.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class figureController {

    @GetMapping("/figure")
    public String figure(){
        return "/figure";
    }
}
