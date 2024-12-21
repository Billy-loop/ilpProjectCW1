package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.constant.OrderStatus;
import uk.ac.ed.inf.pizzadronz.model.*;
import uk.ac.ed.inf.pizzadronz.service.DataRetrive;
import uk.ac.ed.inf.pizzadronz.util.ImplementUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class calcDeliveryPathAsGeoJsonController {
    @PostMapping("/calcDeliveryPathAsGeoJson")
    public ResponseEntity<Map<String, Object>> calcDeliveryPathAsGeoJson(@RequestBody Order order) {
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
