package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.model.*;
import java.util.ArrayList;

@RestController
public class ValidateOrderController {
    @PostMapping("/validateOrder")
    public ResponseEntity<Boolean>validateOrder(Order order){
        int orderNo = order.getOrderNo();
        String orderDate = order.getOrderDate();
        int priceTotalInPence = order.getPriceTotalInPence();
        ArrayList<Pizza> pizzasInOrder = order.getPizzasInOrder();
        CardInfo creditCardInformation = order.getCreditCardInformation();

        return ResponseEntity.ok(true);
    }

    private boolean isValidNo(int orderNo){
        return orderNo > 0;
    }
    private boolean isValidDate(String orderDate){
        return orderDate.matches("\\d{4}-\\d{2}-\\d{2}");
    }
    private boolean isValidPriceTotalInPence(int priceTotalInPence){
        return priceTotalInPence > 0;
    }
    private boolean isValidCreditCardNumber(String creditCardNumber){
        return true;
    }

}
