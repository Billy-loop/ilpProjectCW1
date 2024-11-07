package uk.ac.ed.inf.pizzadronz.service;
import org.springframework.web.client.RestTemplate;
import uk.ac.ed.inf.pizzadronz.model.Restaurant;
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


}
