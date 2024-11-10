package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.model.*;
import uk.ac.ed.inf.pizzadronz.util.ImplementUtil;


@RestController
public class ValidateOrderController {
    @PostMapping("/validateOrder")
    public ResponseEntity<Order>validateOrder(@RequestBody Order order){
       Order res = ImplementUtil.validateOrder(order);
       return ResponseEntity.ok(res);
    }
}
