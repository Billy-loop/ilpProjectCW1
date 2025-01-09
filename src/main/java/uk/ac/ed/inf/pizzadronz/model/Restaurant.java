package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Restaurant {
    private String name;
    private Position location;
    private List<Pizza> menu;
    private List<String> openingDays ;

    public Restaurant(String name, Position location, ArrayList<Pizza> menu, ArrayList<String> openingDays) {
        this.name = name;
        this.location = location;
        this.menu = menu;
        this.openingDays = openingDays;
    }

}
