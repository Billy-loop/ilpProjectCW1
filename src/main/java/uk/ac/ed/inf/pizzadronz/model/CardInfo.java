package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardInfo {
    private String creditCardNumber;
    private String creditCardExpiry;
    private String cvv;

    public CardInfo(String creditCardNumber, String creditCardExpiry, String cvv) {
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.cvv = cvv;
    }

}
