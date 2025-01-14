package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.constant.OrderStatus;
import uk.ac.ed.inf.pizzadronz.model.*;
import uk.ac.ed.inf.pizzadronz.util.ImplementUtil;
import uk.ac.ed.inf.pizzadronz.util.SynSemCheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class responsible for converting a computed drone flight path into GeoJSON format.
 */
@RestController
public class CalcDeliveryPathAsGeoJsonController {

    /**
     * Receives an {@link Order}, validates it, invokes the path calculation, and returns
     * the result in GeoJSON format if successful.
     *
     * @param order The incoming {@link Order} object containing pizzas, credit card info, etc.
     * @return A {@link ResponseEntity} containing a GeoJSON representation of the flight path
     *         or an appropriate HTTP error status if validation or path retrieval fails.
     */
    @PostMapping("/calcDeliveryPathAsGeoJson")
    public ResponseEntity<Map<String, Object>> calcDeliveryPathAsGeoJson(@RequestBody Order order) {
        if (!SynSemCheck.isValidOrder(order)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        // Validate order
        OrderValidationResult validOrder = ImplementUtil.validateOrder(order);
        if (validOrder.getOrderStatus() == OrderStatus.INVALID) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Retrieve the flight path using the existing logic
        CalcDeliveryPathController pathController = new CalcDeliveryPathController();
        ResponseEntity<List<Position>> response = pathController.calcDeliveryPath(order);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Extract delivery path
        List<Position> deliveryPath = response.getBody();

        // Convert to GeoJSON format
        Map<String, Object> geoJson = convertToGeoJson(deliveryPath);

        // Return GeoJSON response
        return ResponseEntity.ok(geoJson);
    }

    /**
     * Converts a list of {@link Position} objects into a GeoJSON FeatureCollection containing
     * a single LineString feature.
     *
     * @param positions The list of positions forming the drone's flight path.
     * @return A {@link Map} structured as a GeoJSON FeatureCollection.
     */
    private Map<String, Object> convertToGeoJson(List<Position> positions) {
        // GeoJSON FeatureCollection structure
        Map<String, Object> geoJson = new HashMap<>();
        geoJson.put("type", "FeatureCollection");

        // Create the LineString feature
        Map<String, Object> feature = new HashMap<>();
        feature.put("type", "Feature");

        // Geometry of the LineString
        Map<String, Object> geometry = new HashMap<>();
        geometry.put("type", "LineString");

        // Extract coordinates from positions
        List<List<Double>> coordinates = new ArrayList<>();
        for (Position position : positions) {
            List<Double> coordinate = Arrays.asList(position.getLng(), position.getLat());
            coordinates.add(coordinate);
        }
        geometry.put("coordinates", coordinates);

        // Add properties (optional, can be extended)
        Map<String, Object> properties = new HashMap<>();
        properties.put("description", "Delivery flight path");

        // Assemble feature
        feature.put("geometry", geometry);
        feature.put("properties", properties);

        // Add feature to FeatureCollection
        List<Map<String, Object>> features = new ArrayList<>();
        features.add(feature);
        geoJson.put("features", features);

        return geoJson;
    }

}
