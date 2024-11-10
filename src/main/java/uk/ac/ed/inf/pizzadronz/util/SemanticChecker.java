package uk.ac.ed.inf.pizzadronz.util;
import uk.ac.ed.inf.pizzadronz.model.LngLatPairRequest;
import uk.ac.ed.inf.pizzadronz.model.Pizza;
import uk.ac.ed.inf.pizzadronz.model.Position;
import uk.ac.ed.inf.pizzadronz.model.Restaurant;

import java.util.List;
import java.util.Objects;


public class SemanticChecker {

    public static boolean checkLngLatPair(LngLatPairRequest lnglat1 ) {
        if (lnglat1 == null){
            return false;
        }
        if(lnglat1.getPosition1() == null || lnglat1.getPosition2() == null) {
            return false;
        }
        // IF isValidPosition is true, the if below will not run, vice visa
        if (!isValidPosition(lnglat1.getPosition1()) ||
                !isValidPosition(lnglat1.getPosition2())) {
            return false;
        }
        return true;
    }

    // valid return ture, invalid return false.
    public static boolean isValidPosition( Position position){

        if(position == null){
            return false;
        }

        if(position.getLng() == null  || position.getLat() == null){
            return false;
        }
        if(position.getLat() > 90 || position.getLat() < -90){
            return false;
        }
        if(position.getLng() > 180 || position.getLng() < -180){
            return false;
        }
        return true;
    }

    public static boolean isOnLine(Position p, Position v1, Position v2) {
        // Calculate the cross product (to check for collinearity)
        double crossProduct = (p.getLat() - v1.getLat()) * (v2.getLng() - v1.getLng())
                - (p.getLng() - v1.getLng()) * (v2.getLat() - v1.getLat());

        // If crossProduct is not zero, the point is not collinear
        if (crossProduct != 0) {
            return false;
        }

        // Check if the point lies within the bounds of the line segment
        boolean lngBounds = Math.min(v1.getLng(), v2.getLng()) <= p.getLng() &&
                p.getLng() <= Math.max(v1.getLng(), v2.getLng());
        boolean latBounds = Math.min(v1.getLat(), v2.getLat()) <= p.getLat() &&
                p.getLat() <= Math.max(v1.getLat(), v2.getLat());

        return lngBounds && latBounds;
    }

    public static boolean checkVertices(List<Position> vertices){

        if (vertices == null){
            return false;
        }
        //check the position in vertices is valid.
        for (int i = 0; i < vertices.size(); i++) {
            if(!isValidPosition(vertices.get(i))){
                return false;
            }
        }
        //check whether this polygon is close
        if (!Objects.equals(vertices.get(0).getLng(), vertices.get(vertices.size() - 1).getLng()) ||
                !Objects.equals(vertices.get(0).getLat(), vertices.get(vertices.size() - 1).getLat())) {
            return false;
        }
        return true;
    }

    public static boolean isOnStraightLine(List<Position> vertices) {
        double x1 = vertices.get(0).getLng();
        double y1 = vertices.get(0).getLat();
        double x2 = vertices.get(1).getLng();
        double y2 = vertices.get(1).getLat();

        for (int i = 2; i < vertices.size(); i++) {
            double x3 = vertices.get(i).getLng();
            double y3 = vertices.get(i).getLat();

            boolean notSameGrad = (((x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1))  != 0);

            if (notSameGrad) {
                return false;
            }
        }
        return true;
    }

}
