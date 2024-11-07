package uk.ac.ed.inf.pizzadronz.model;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    @Setter
    private String name;
    @Setter
    private Position location;
    @Setter
    private List<Pizza> menu;
    private List<String> openingDays ;

    public Restaurant(String name, Position location, ArrayList<Pizza> menu, ArrayList<String> openingDays) {
        this.name = name;
        this.location = location;
        this.menu = menu;
        this.openingDays = openingDays;
    }

    public String getName() {
        return name;
    }

    public Position getLocation() {
        return location;
    }

    public List<Pizza> getMenu() {
        return menu;
    }

    public List<String> getOpeningDays() {
        return openingDays;
    }
}
