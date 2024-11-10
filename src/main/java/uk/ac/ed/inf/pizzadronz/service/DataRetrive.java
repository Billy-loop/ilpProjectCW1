package uk.ac.ed.inf.pizzadronz.service;
import org.springframework.web.client.RestTemplate;
import uk.ac.ed.inf.pizzadronz.model.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class DataRetrive {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://ilp-rest-2024.azurewebsites.net/";

    public static List<Restaurant> getRestaurants() {
        String url = BASE_URL + "restaurants";
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(url, Restaurant[].class)));
    }

    public static Region getCentralArea() {
        String url = BASE_URL + "centralArea";
        return restTemplate.getForObject(url, Region.class);
    }

    public static List<Region> getNoFlyZone() {
        String url = BASE_URL + "noFlyZones";
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(url, Region[].class)));
    }


}
