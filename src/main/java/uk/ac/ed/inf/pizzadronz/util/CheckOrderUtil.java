package uk.ac.ed.inf.pizzadronz.util;
import uk.ac.ed.inf.pizzadronz.model.CardInfo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CheckOrderUtil {
//    public static boolean isValidNo(String orderNo){
//        return orderNo > 0;
//    }
    public static boolean isValidDate(String orderDate){
        LocalDate date = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return !date.isBefore(LocalDate.now());  // Returns true if date is today or in the future
    }
    public static boolean isValidPriceTotalInPence(int priceTotalInPence){
        return priceTotalInPence > 0;
    }

    public static boolean isValidCreditCardNumber(String creditCardNumber){
        return creditCardNumber != null && creditCardNumber.matches("\\d{16}");
    }

    public static boolean isValidCVV(String cvv) {
        // Check if the CVV is 3 digits and numeric
        return cvv != null && cvv.matches("\\d{3}");
    }



}
