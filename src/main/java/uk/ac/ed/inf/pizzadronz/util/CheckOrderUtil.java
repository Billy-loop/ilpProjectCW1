package uk.ac.ed.inf.pizzadronz.util;
import uk.ac.ed.inf.pizzadronz.model.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CheckOrderUtil {

    private static final String today = LocalDate.now().getDayOfWeek().toString();

    public static boolean isValidDate(LocalDate orderDate){
        return orderDate != null && !orderDate.isBefore(LocalDate.now());
    }

    public static boolean isValidExpiryDate(String creditCardExpiry, LocalDate orderDate){
        try {
            // Get the date of expiry date
            //String expiryDate = order.getCreditCardInformation().getCreditCardExpiry();
            String[] parts = creditCardExpiry.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000;
            YearMonth cardExpiry = YearMonth.of(year, month);

            // Get the date of order date
            String[] orderDateParts = orderDate.toString().split("-");
            int orderYear = Integer.parseInt(orderDateParts[0]);
            int orderMonth = Integer.parseInt(orderDateParts[1]);
            YearMonth orderYearMonth = YearMonth.of(orderYear, orderMonth);
            return !cardExpiry.isBefore(orderYearMonth) && creditCardExpiry.matches("(0[1-9]|1[0-2])/\\d{2}");
        } catch (Exception e) {
            return creditCardExpiry.matches("(0[1-9]|1[0-2])/\\d{2}");
        }
        // Get the date of expiry date
        //String expiryDate = order.getCreditCardInformation().getCreditCardExpiry();
//        String[] parts = creditCardExpiry.split("/");
//        int month = Integer.parseInt(parts[0]);
//        int year = Integer.parseInt(parts[1]) + 2000;
//        YearMonth cardExpiry = YearMonth.of(year, month);
//
//        // Get the date of order date
//        String[] orderDateParts = orderDate.toString().split("-");
//        int orderYear = Integer.parseInt(orderDateParts[0]);
//        int orderMonth = Integer.parseInt(orderDateParts[1]);
//        YearMonth orderYearMonth = YearMonth.of(orderYear, orderMonth);
//
//        // Compare the order date and the expiry date.
//        return !cardExpiry.isBefore(orderYearMonth) && creditCardExpiry.matches("(0[1-9]|1[0-2])/\\d{2}");
    }

    public static boolean isValidPriceTotalInPence(List<Pizza> pizzasInOrder, int priceTotalInPence ){
        int sum = 0;
        for (Pizza pizza : pizzasInOrder) {
            sum = sum + pizza.getPriceInPence();
        }
        return sum + 100 == priceTotalInPence;
    }

    public static boolean isValidCreditCardNumber(String creditCardNumber){
        return creditCardNumber != null && creditCardNumber.matches("\\d{16}");
    }

    public static boolean isValidCVV(String cvv) {
        // Check if the CVV is 3 digits and numeric
        return cvv != null && cvv.matches("\\d{3}");
    }

    public static boolean isSameRestaurant(List<Pizza> pizzasInOrder){
        String restaurant = pizzasInOrder.get(0).getName().split(":")[0].trim();
        for (Pizza pizza : pizzasInOrder) {
            if(!pizza.getName().split(":")[0].trim().equals(restaurant)){
                return false;
            }
        }
        return true;
    }

    public static boolean isValidPizza(Order order){

        String regex = "R\\d+: .+$";
        return order.getPizzasInOrder().stream().allMatch(pizza -> pizza.getName().matches(regex));
    }

    public static boolean isOpen(Restaurant restaurant, LocalDate orderDate){
        List<String> openDays = restaurant.getOpeningDays();

        for (String day : openDays) {
            if (orderDate.getDayOfWeek().toString().equals(day)){
                return true;
            }
        }
        return false;
    }

    public static boolean isValidPizzaPrice(Restaurant restaurant, List<Pizza> pizzasInOrder){
        List<Pizza> menu = restaurant.getMenu();

        // Check each pizza in the order
        for (Pizza pizza : pizzasInOrder) {
            boolean matchFound = false;

            // Check if this pizza exists in the restaurant's menu with the same price
            for (Pizza pizzaMenu : menu) {
                if (pizzaMenu.getName().equals(pizza.getName()) && pizzaMenu.getPriceInPence() == pizza.getPriceInPence()) {
                    matchFound = true;
                    break;
                }
            }

            // If a pizza in the order has no match in the menu, return false
            if (!matchFound) {
                return false;
            }
        }

        // All pizzas in the order matched the menu prices
        return true;
    }

//        List<Pizza> menu = restaurant.getMenu();
//        for (Pizza pizza : pizzasInOrder) {
//            for (Pizza pizzaMenu : menu) {
//                if (pizzaMenu.getName().equals(pizza.getName()) && pizzaMenu.getPriceInPence() == pizza.getPriceInPence()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }



}
