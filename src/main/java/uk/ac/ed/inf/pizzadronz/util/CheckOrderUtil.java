package uk.ac.ed.inf.pizzadronz.util;

import uk.ac.ed.inf.pizzadronz.model.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Utility class for checking various aspects of an order.
 */
public class CheckOrderUtil {

    // Today's day of the week as a string
    private static final String today = LocalDate.now().getDayOfWeek().toString();


    /**
     * Validates if the given credit card expiry date is valid and not expired.
     *
     * @param creditCardExpiry the expiry date of the credit card in MM/YY format
     * @param orderDate the date of the order
     * @return true if the expiry date is valid and not expired, false otherwise
     */
    public static boolean isValidExpiryDate(String creditCardExpiry, LocalDate orderDate) {
        try {
            // Split the expiry date into month and year
            String[] parts = creditCardExpiry.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000;
            YearMonth cardExpiry = YearMonth.of(year, month);

            // Get the year and month of the order date
            String[] orderDateParts = orderDate.toString().split("-");
            int orderYear = Integer.parseInt(orderDateParts[0]);
            int orderMonth = Integer.parseInt(orderDateParts[1]);
            YearMonth orderYearMonth = YearMonth.of(orderYear, orderMonth);

            // Check if the card expiry date is not before the order date and matches the MM/YY format
            return !cardExpiry.isBefore(orderYearMonth) && creditCardExpiry.matches("(0[1-9]|1[0-2])/\\d{2}");
        } catch (Exception e) {
            // If an exception occurs, check if the expiry date matches the MM/YY format
            return creditCardExpiry.matches("(0[1-9]|1[0-2])/\\d{2}");
        }
    }

    /**
     * Validates if the total price in pence matches the sum of the prices of the pizzas in the order plus a delivery fee.
     *
     * @param pizzasInOrder the list of pizzas in the order
     * @param priceTotalInPence the total price in pence
     * @return true if the total price is valid, false otherwise
     */
    public static boolean isValidPriceTotalInPence(List<Pizza> pizzasInOrder, int priceTotalInPence) {
        int sum = 0;
        for (Pizza pizza : pizzasInOrder) {
            sum = sum + pizza.getPriceInPence();
        }
        return sum + 100 == priceTotalInPence;
    }

    /**
     * Validates if the given credit card number is a 16-digit numeric string.
     *
     * @param creditCardNumber the credit card number
     * @return true if the credit card number is valid, false otherwise
     */
    public static boolean isValidCreditCardNumber(String creditCardNumber) {
        return creditCardNumber != null && creditCardNumber.matches("\\d{16}");
    }

    /**
     * Validates if the given CVV is a 3-digit numeric string.
     *
     * @param cvv the CVV of the credit card
     * @return true if the CVV is valid, false otherwise
     */
    public static boolean isValidCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    /**
     * Checks if all pizzas in the order are from the same restaurant.
     *
     * @param pizzasInOrder the list of pizzas in the order
     * @return true if all pizzas are from the same restaurant, false otherwise
     */
    public static boolean isSameRestaurant(List<Pizza> pizzasInOrder) {
        String restaurant = pizzasInOrder.get(0).getName().split(":")[0].trim();
        for (Pizza pizza : pizzasInOrder) {
            if (!pizza.getName().split(":")[0].trim().equals(restaurant)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates if all pizzas in the order have valid names.
     *
     * @param order the order to check
     * @return true if all pizzas have valid names, false otherwise
     */
    public static boolean isValidPizza(Order order) {
        String regex = "R\\d+: .+$";
        return order.getPizzasInOrder().stream().allMatch(pizza -> pizza.getName().matches(regex));
    }

    /**
     * Checks if the restaurant is open on the given order date.
     *
     * @param restaurant the restaurant to check
     * @param orderDate the date of the order
     * @return true if the restaurant is open, false otherwise
     */
    public static boolean isOpen(Restaurant restaurant, LocalDate orderDate) {
        List<String> openDays = restaurant.getOpeningDays();
        for (String day : openDays) {
            if (orderDate.getDayOfWeek().toString().equals(day)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates if the prices of the pizzas in the order match the prices in the restaurant's menu.
     *
     * @param restaurant the restaurant to check
     * @param pizzasInOrder the list of pizzas in the order
     * @return true if all pizza prices are valid, false otherwise
     */
    public static boolean isValidPizzaPrice(Restaurant restaurant, List<Pizza> pizzasInOrder) {
        List<Pizza> menu = restaurant.getMenu();
        for (Pizza pizza : pizzasInOrder) {
            boolean matchFound = false;
            for (Pizza pizzaMenu : menu) {
                if (pizzaMenu.getName().equals(pizza.getName()) && pizzaMenu.getPriceInPence() == pizza.getPriceInPence()) {
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound) {
                return false;
            }
        }
        return true;
    }
}