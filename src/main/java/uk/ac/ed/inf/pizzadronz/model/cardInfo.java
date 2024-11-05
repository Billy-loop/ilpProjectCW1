package uk.ac.ed.inf.pizzadronz.model;

public class cardInfo {
    private int creditCardNumber;
    private String creditCardExpiry;
    private int cvv;

    public cardInfo(int creditCardNumber, String creditCardExpiry, int cvv) {
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.cvv = cvv;
    }

    public int getCreditCardNumber() {
        return creditCardNumber;
    }
    public void setCreditCardNumber(int creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
    public String getCreditCardExpiry() {
        return creditCardExpiry;
    }
    public void setCreditCardExpiry(String creditCardExpiry) {
        this.creditCardExpiry = creditCardExpiry;
    }
    public int getCvv() {
        return cvv;
    }
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
}
