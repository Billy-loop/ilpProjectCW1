package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UuidController {
    @GetMapping("/uuid")
    public String uuid(){
        return "s" + "2292976";
    }
}
