package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.model.*;
import uk.ac.ed.inf.pizzadronz.util.ImplementUtil;
import uk.ac.ed.inf.pizzadronz.util.SemanticChecker;


@RestController
public class ValidateOrderController {
    @PostMapping("/validateOrder")
    public ResponseEntity<OrderValidationResult>validateOrder(@RequestBody Order order){
        if (!SemanticChecker.isValidOrder(order)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        OrderValidationResult res = ImplementUtil.validateOrder(order);
       return ResponseEntity.ok(res);
    }
}
