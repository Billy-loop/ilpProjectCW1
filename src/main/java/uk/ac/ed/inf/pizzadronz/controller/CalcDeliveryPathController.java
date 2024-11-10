package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.ac.ed.inf.pizzadronz.constant.OrderStatus;
import uk.ac.ed.inf.pizzadronz.model.*;
import uk.ac.ed.inf.pizzadronz.service.DataRetrive;
import uk.ac.ed.inf.pizzadronz.util.ImplementUtil;

import java.util.ArrayList;
import java.util.List;

public class CalcDeliveryPathController {
    private static final double[] COMPASS_DIRECTIONS = {
            0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5,
            180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5
    };

    public static final Position appletonTower = new Position(-3.186874, 55.944494);

    public static final List<Region> noFlyZones = DataRetrive.getNoFlyZone();

    private static boolean InCentreArea = false;

    private static Region centreArea = DataRetrive.getCentralArea();

    @PostMapping("calcDeliveryPath")
    public ResponseEntity <List<Position>> calcDeliveryPath(@RequestBody Order order){
        Order validOrder = ImplementUtil.validateOrder(order);
        if(validOrder.getOrderStatus() == OrderStatus.INVALID){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Restaurant restaurant = ImplementUtil.getRestaurant(order);
        Position curLoc = restaurant.getLocation();

        List<Position> deliveryPath = new ArrayList<Position>();
        deliveryPath.add(curLoc);

        Position predictLoc = curLoc;

        LngLatPairRequest curPair = new LngLatPairRequest(curLoc, appletonTower);
        while (!ImplementUtil.isCloseTo(curPair)){
            double deltaY = curPair.getPosition1().getLat() - curPair.getPosition2().getLat();
            double deltaX = curPair.getPosition1().getLng() - curPair.getPosition2().getLng();
            double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
            double nextAngle = findClosestDirection(angle);

            predictLoc = ImplementUtil.nextPosition(new NextPositionRequest(curLoc, nextAngle));

            //NextPosition in nonFlyZone, or in centre area but leave
            if (isInNonFlyZone(predictLoc) || (InCentreArea && leaveCentreArea(predictLoc))) {
                deliveryPath.add(curLoc);
            }

            curLoc = predictLoc;

            if(ImplementUtil.isInPolygon(curLoc, centreArea.getVertices())){
                InCentreArea = true;
            }

            deliveryPath.add(curLoc);
            curPair = new LngLatPairRequest(curLoc, appletonTower);

        }

        return ResponseEntity.ok(deliveryPath);
    }

    // Method to calculate the closest compass direction
    public static double findClosestDirection(double angle) {
        // Normalize angle to range [0, 360)
        if (angle < 0) {
            angle += 360;
        }

        double closestDifference = 360;  // Start with a large difference
        int closestIndex = -1;

        // Find the closest compass direction
        for (int i = 0; i < COMPASS_DIRECTIONS.length; i++) {
            double difference = Math.abs(angle - COMPASS_DIRECTIONS[i]);
            if (difference > 180) {
                difference = 360 - difference;
            }
            if (difference < closestDifference) {
                closestDifference = difference;
                closestIndex = i;
            }
        }
        // Return the name of the closest direction
        return COMPASS_DIRECTIONS[closestIndex];
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
