package uk.ac.ed.inf.pizzadronz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/uuid")
    public String getUUid(){
        return "s" + "1234567";
    }
}
