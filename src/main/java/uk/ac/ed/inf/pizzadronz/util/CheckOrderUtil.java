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

    public static boolean isValidExpiryDate(String creditCardExpiry){
        return creditCardExpiry != null && creditCardExpiry.matches("(0[1-9]|1[0-2])/\\d{2}");

//        if(creditCardExpiry != null && creditCardExpiry.matches("(0[1-9]|1[0-2])/\\d{2}")){
//            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/yy");
//            YearMonth yearMonth = YearMonth.parse(creditCardExpiry, inputFormatter);
//            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
//
//            String outputDate = yearMonth.format(outputFormatter);
//
//
//            return true;
//        }
//        return false;
    }

    public static boolean isValidPriceTotalInPence(List<Pizza> pizzasInOrder, int priceTotalInPence ){
        int sum = 0;
        for (Pizza pizza : pizzasInOrder) {
            sum = sum + pizza.getPriceInPence();
        }
        return sum == priceTotalInPence;
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

    public static boolean isOpen(Restaurant restaurant){
        List<String> openDays = restaurant.getOpeningDays();
        for (String day : openDays) {
            if (today.equals(day)){
                return true;
            }
        }
        return false;
    }

    public static boolean isValidPizza(Restaurant restaurant, List<Pizza> pizzasInOrder){
        List<Pizza> menu = restaurant.getMenu();
        for (Pizza pizza : pizzasInOrder) {
            for (Pizza pizzaMenu : menu) {
                if (pizzaMenu.getName().equals(pizza.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isValidPizzaPrice(Restaurant restaurant, List<Pizza> pizzasInOrder){
        List<Pizza> menu = restaurant.getMenu();
        for (Pizza pizza : pizzasInOrder) {
            for (Pizza pizzaMenu : menu) {
                if (pizza.equals(pizzaMenu)) {
                    return true;
                }
            }
        }
        return false;
    }



}
