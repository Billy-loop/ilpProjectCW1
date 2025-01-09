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

}
