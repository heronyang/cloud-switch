package main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WebController {

    @RequestMapping("/hello")
    public String hello() {
        System.out.println("Hello");
        return "Hello World";
    }

}
