package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pizza {
    private String name;
    private int priceInPence;

    public Pizza(String name, int priceInPence) {
        this.name = name;
        this.priceInPence = priceInPence;
    }
//
//    public String getName() {
//        return name;
//    }
//    public int getPriceInPence() {
//        return priceInPence;
//    }
//    public void setPriceInPence(int priceInPence) {
//        this.priceInPence = priceInPence;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
}
