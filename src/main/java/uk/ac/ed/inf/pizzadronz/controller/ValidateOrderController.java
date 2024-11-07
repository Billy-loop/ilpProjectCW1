package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.model.*;
import uk.ac.ed.inf.pizzadronz.service.DataRetrive;
import uk.ac.ed.inf.pizzadronz.util.CheckOrderUtil;
import uk.ac.ed.inf.pizzadronz.constant.*;
import uk.ac.ed.inf.pizzadronz.util.CommonFunction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ValidateOrderController {
    @PostMapping("/validateOrder")
    public ResponseEntity<Order>validateOrder(@RequestBody Order order){

        String orderNo = order.getOrderNo();
        LocalDate orderDate = order.getOrderDate();
        int priceTotalInPence = order.getPriceTotalInPence();
        List<Pizza> pizzasInOrder = order.getPizzasInOrder();
        CardInfo creditCardInformation = order.getCreditCardInformation();

        //Find the Restaurant
        Restaurant restaurant = CommonFunction.getRestaurant(pizzasInOrder, DataRetrive.getRestaurants());

        if(!CheckOrderUtil.isValidDate(orderDate)){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.UNDEFINED);
            return ResponseEntity.ok(order);
        }

        if(!CheckOrderUtil.isValidCVV(creditCardInformation.getCvv())){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
            return ResponseEntity.ok(order);
        }

        if(!CheckOrderUtil.isValidExpiryDate(creditCardInformation.getCreditCardExpiry())){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
            return ResponseEntity.ok(order);
        }

        if(!CheckOrderUtil.isValidCreditCardNumber(creditCardInformation.getCreditCardNumber())){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
            return ResponseEntity.ok(order);
        }

        if(pizzasInOrder.isEmpty()){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.EMPTY_ORDER);
            return ResponseEntity.ok(order);
        }

        if(pizzasInOrder.size() > 4){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
            return ResponseEntity.ok(order);
        }

        if(!CheckOrderUtil.isValidPriceTotalInPence(pizzasInOrder, priceTotalInPence)){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
            return ResponseEntity.ok(order);
        }

        if(!CheckOrderUtil.isSameRestaurant(pizzasInOrder)){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
            return ResponseEntity.ok(order);
        }

        assert restaurant != null;
        if(!CheckOrderUtil.isOpen(restaurant)){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
            return ResponseEntity.ok(order);
        }

        if(!CheckOrderUtil.isValidPizza(restaurant,pizzasInOrder)){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
            return ResponseEntity.ok(order);
        }

        if(!CheckOrderUtil.isValidPizzaPrice(restaurant,pizzasInOrder)){
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.PRICE_FOR_PIZZA_INVALID);
            return ResponseEntity.ok(order);
        }

        order.setOrderStatus(OrderStatus.VALID);
        order.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        return ResponseEntity.ok(order);
    }
}
