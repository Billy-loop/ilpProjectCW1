package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.model.*;
import uk.ac.ed.inf.pizzadronz.util.CheckOrderUtil;
import uk.ac.ed.inf.pizzadronz.constant.*;
import java.util.ArrayList;

@RestController
public class ValidateOrderController {
    @PostMapping("/validateOrder")
    public ResponseEntity<OrderValidationResult>validateOrder(Order order){
        String orderNo = order.getOrderNo();
        String orderDate = order.getOrderDate();
        int priceTotalInPence = order.getPriceTotalInPence();
        ArrayList<Pizza> pizzasInOrder = order.getPizzasInOrder();
        CardInfo creditCardInformation = order.getCreditCardInformation();

        if(!CheckOrderUtil.isValidCVV(creditCardInformation.getCvv())){
            return ResponseEntity.ok(new OrderValidationResult(OrderValidationCode.CVV_INVALID,OrderStatus.INVALID));
        }

        if(!CheckOrderUtil.isValidDate(creditCardInformation.getCreditCardExpiry())){
            return ResponseEntity.ok(new OrderValidationResult(OrderValidationCode.EXPIRY_DATE_INVALID,OrderStatus.INVALID));
        }

        if(!CheckOrderUtil.isValidCreditCardNumber(creditCardInformation.getCreditCardNumber())){
            return ResponseEntity.ok(new OrderValidationResult(OrderValidationCode.CARD_NUMBER_INVALID,OrderStatus.INVALID));
        }

        if(pizzasInOrder.size() > 4){
            return ResponseEntity.ok(new OrderValidationResult(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED,OrderStatus.INVALID));
        }

        


        return ResponseEntity.ok(new OrderValidationResult(OrderValidationCode.NO_ERROR,OrderStatus.VALID));
    }
}
