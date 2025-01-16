package uk.ac.ed.inf.pizzadronz.unitTest;

import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.pizzadronz.model.*;
import uk.ac.ed.inf.pizzadronz.util.CheckOrderUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckOrderUtilTest {

    @Test
    void testIsValidExpiryDate() {
        // Valid expiry date
        assertTrue(CheckOrderUtil.isValidExpiryDate("12/25", LocalDate.of(2024, 1, 1)));
        // Expired card
        assertFalse(CheckOrderUtil.isValidExpiryDate("12/23", LocalDate.of(2024, 1, 1)));
        // Invalid format
        assertFalse(CheckOrderUtil.isValidExpiryDate("13/25", LocalDate.of(2024, 1, 1)));
        assertFalse(CheckOrderUtil.isValidExpiryDate("12-25", LocalDate.of(2024, 1, 1)));
    }

    @Test
    void testIsValidPriceTotalInPence() {
        List<Pizza> pizzas = new ArrayList<>();
        pizzas.add(new Pizza("R1: Margherita", 800));
        pizzas.add(new Pizza("R1: Pepperoni", 1000));
        // Valid total price
        assertTrue(CheckOrderUtil.isValidPriceTotalInPence(pizzas, 1900));
        // Invalid total price
        assertFalse(CheckOrderUtil.isValidPriceTotalInPence(pizzas, 1800));
    }

    @Test
    void testIsValidCreditCardNumber() {
        // Valid credit card number
        assertTrue(CheckOrderUtil.isValidCreditCardNumber("1234567812345678"));
        // Invalid credit card number
        assertFalse(CheckOrderUtil.isValidCreditCardNumber("12345678"));
        assertFalse(CheckOrderUtil.isValidCreditCardNumber(null));
    }

    @Test
    void testIsValidCVV() {
        // Valid CVV
        assertTrue(CheckOrderUtil.isValidCVV("123"));
        // Invalid CVV
        assertFalse(CheckOrderUtil.isValidCVV("12"));
        assertFalse(CheckOrderUtil.isValidCVV(null));
    }

    @Test
    void testIsSameRestaurant() {
        List<Pizza> pizzas = new ArrayList<>();
        pizzas.add(new Pizza("R1: Margherita", 800));
        pizzas.add(new Pizza("R1: Pepperoni", 1000));
        // All pizzas from the same restaurant
        assertTrue(CheckOrderUtil.isSameRestaurant(pizzas));

        pizzas.add(new Pizza("R2: Veggie", 900));
        // Pizzas from different restaurants
        assertFalse(CheckOrderUtil.isSameRestaurant(pizzas));
    }

    @Test
    void testIsValidPizza() {
        List<Pizza> pizzas = new ArrayList<>();
        pizzas.add(new Pizza("R1: Margherita", 800));
        pizzas.add(new Pizza("R1: Pepperoni", 1000));

        Order order = new Order();
        order.setPizzasInOrder(pizzas);

        // All pizza names valid
        assertTrue(CheckOrderUtil.isValidPizza(order));

        pizzas.add(new Pizza("InvalidPizzaName", 900));
        order.setPizzasInOrder(pizzas);
        // Invalid pizza name
        assertFalse(CheckOrderUtil.isValidPizza(order));
    }

    @Test
    void testIsOpen() {
        Position restaurantLocation = new Position(-3.186874, 55.944494);
        List<Pizza> menu = new ArrayList<>();
        menu.add(new Pizza("R1: Margherita", 800));
        menu.add(new Pizza("R1: Pepperoni", 1000));

        List<String> openingDays = new ArrayList<>();
        openingDays.add("MONDAY");
        openingDays.add("TUESDAY");

        Restaurant restaurant = new Restaurant("R1", restaurantLocation, new ArrayList<>(menu), new ArrayList<>(openingDays));

        // Restaurant open on Monday
        assertTrue(CheckOrderUtil.isOpen(restaurant, LocalDate.of(2024, 1, 1))); // Monday
        // Restaurant closed on Sunday
        assertFalse(CheckOrderUtil.isOpen(restaurant, LocalDate.of(2024, 1, 7))); // Sunday
    }

    @Test
    void testIsValidPizzaPrice() {
        List<Pizza> menu = new ArrayList<>();
        menu.add(new Pizza("R1: Margherita", 800));
        menu.add(new Pizza("R1: Pepperoni", 1000));

        Position restaurantLocation = new Position(-3.186874, 55.944494);
        List<String> openingDays = new ArrayList<>();
        openingDays.add("MONDAY");

        Restaurant restaurant = new Restaurant("R1", restaurantLocation, new ArrayList<>(menu), new ArrayList<>(openingDays));

        List<Pizza> orderPizzas = new ArrayList<>();
        orderPizzas.add(new Pizza("R1: Margherita", 800));
        orderPizzas.add(new Pizza("R1: Pepperoni", 1000));

        // Prices match the menu
        assertTrue(CheckOrderUtil.isValidPizzaPrice(restaurant, orderPizzas));

        orderPizzas.add(new Pizza("R1: Veggie", 900));
        // Price does not match
        assertFalse(CheckOrderUtil.isValidPizzaPrice(restaurant, orderPizzas));
    }
}
