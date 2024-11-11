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
import java.util.List;

@RestController
public class CalcDeliveryPathController {

    private static final List<Double> COMPASS_DIRECTIONS = new ArrayList<>(Arrays.asList(
            0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
            180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5
    ));

    public static final Position appletonTower = new Position(-3.186874, 55.944494);

    public static final List<Region> noFlyZones = DataRetrive.getNoFlyZone();

    private static Region centreArea = DataRetrive.getCentralArea();



    @PostMapping("/calcDeliveryPath")
    public ResponseEntity <List<Position>> calcDeliveryPath(@RequestBody Order order){
        Order validOrder = ImplementUtil.validateOrder(order);
        if(validOrder.getOrderStatus() == OrderStatus.INVALID){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Restaurant restaurant = ImplementUtil.getRestaurant(order);
        Position curLoc = restaurant.getLocation();

        List<Position> deliveryPath = new ArrayList<Position>();
        List<Position> reachPoint = new ArrayList<>();
        boolean InCentreArea = false;

        deliveryPath.add(curLoc);
        reachPoint.add(curLoc);

        Position predictLoc;
        List <Double> direction = new ArrayList<>(Arrays.asList(
                0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
                180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5
        ));

        LngLatPairRequest curPair = new LngLatPairRequest(curLoc, appletonTower);
        System.out.println(1);
        while (!ImplementUtil.isCloseTo(curPair)){

            double deltaY = curPair.getPosition2().getLat() - curPair.getPosition1().getLat();
            double deltaX = curPair.getPosition2().getLng() - curPair.getPosition1().getLng();
            double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
            double nextAngle = findClosestDirection(angle, COMPASS_DIRECTIONS);


            predictLoc = ImplementUtil.nextPosition(new NextPositionRequest(curLoc, nextAngle));
            //NextPosition in nonFlyZone, or in centre area but leave
            while ((isInNonFlyZone(predictLoc) || (InCentreArea && leaveCentreArea(predictLoc))) || reachPoint.contains(predictLoc)) {
                direction.remove(nextAngle);
                nextAngle = findClosestDirection(angle, direction);
                predictLoc = ImplementUtil.nextPosition(new NextPositionRequest(curLoc, nextAngle));
            }

            curLoc = predictLoc;
            direction = new ArrayList<>(Arrays.asList(
                    0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
                    180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5
            ));
            if(ImplementUtil.isInPolygon(curLoc, centreArea.getVertices())){
                InCentreArea = true;
            }

            deliveryPath.add(curLoc);
            reachPoint.add(curLoc);
            curPair.setPosition1(curLoc);
            System.out.printf("Current lng: %f current lat: %f ; Target lng: %f Target lat: %f\n", curPair.getPosition1().getLng(), curPair.getPosition1().getLat(),
                    curPair.getPosition2().getLng(), curPair.getPosition2().getLat());

        }

        return ResponseEntity.ok(deliveryPath);
    }

    // Method to calculate the closest compass direction
    public static double findClosestDirection(double angle, List <Double> directions) {
        // Normalize angle to range [0, 360)
        if (angle < 0) {
            angle += 360;
        }

        double closestDifference = 360;  // Start with a large difference
        int closestIndex = -1;

        // Find the closest compass direction
        for (int i = 0; i < directions.size(); i++) {
            double difference = Math.abs(angle - directions.get(i));
            if (difference > 180) {
                difference = 360 - difference;
            }
            if (difference < closestDifference) {
                closestDifference = difference;
                closestIndex = i;
            }
        }
        // Return the name of the closest direction
        return directions.get(closestIndex);
    }

    public static boolean isInNonFlyZone(Position position) {
        for (Region nonFlyZone : noFlyZones) {
            if(ImplementUtil.isInPolygon(position, nonFlyZone.getVertices())){
                return true;
            }
        }
        return false;
    }

    public static  boolean leaveCentreArea(Position position) {
        return !ImplementUtil.isInPolygon(position, centreArea.getVertices());
    }
}
