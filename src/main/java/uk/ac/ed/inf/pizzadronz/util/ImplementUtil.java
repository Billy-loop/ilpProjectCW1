package uk.ac.ed.inf.pizzadronz.util;

import uk.ac.ed.inf.pizzadronz.constant.OrderStatus;
import uk.ac.ed.inf.pizzadronz.constant.OrderValidationCode;
import uk.ac.ed.inf.pizzadronz.model.*;
import uk.ac.ed.inf.pizzadronz.service.DataRetrive;

import java.time.LocalDate;
import java.util.List;

/**
 * Utility class for various implementations related to the PizzaDronz application.
 */
public class ImplementUtil {

    /**
     * Calculates the Euclidean distance between two geographical points.
     *
     * @param lnglat1 the pair of positions to calculate the distance between
     * @return the distance between the two positions
     */
    public static Double distanceTo(LngLatPairRequest lnglat1) {
        double lng1 = lnglat1.getPosition1().getLng();
        double lng2 = lnglat1.getPosition2().getLng();
        double lat1 = lnglat1.getPosition1().getLat();
        double lat2 = lnglat1.getPosition2().getLat();
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2));
    }


    /**
     * Determines if two geographical points are close to each other.
     *
     * @param lnglat1 the pair of positions to check
     * @return true if the distance between the two positions is less than 0.00015, false otherwise
     */
    public static Boolean isCloseTo(LngLatPairRequest lnglat1) {
        double lng1 = lnglat1.getPosition1().getLng();
        double lng2 = lnglat1.getPosition2().getLng();
        double lat1 = lnglat1.getPosition1().getLat();
        double lat2 = lnglat1.getPosition2().getLat();
        double distance = Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2));
        return distance < 0.00015;
    }

    /**
     * Calculates the next position based on the starting position and angle.
     *
     * @param nextPosition the request containing the start position and angle
     * @return the next position
     */
    public static Position nextPosition(NextPositionRequest nextPosition) {
        double radian = Math.toRadians(nextPosition.getAngle());
        Position res = new Position(nextPosition.getStart().getLng() + Math.cos(radian) * 0.00015,
                nextPosition.getStart().getLat() + Math.sin(radian) * 0.00015);
        return res;
    }


    /**
     * Checks if a given point lies on the line segment defined by two vertices.
     *
     * @param p the point to check
     * @param v1 the first vertex of the line segment
     * @param v2 the second vertex of the line segment
     * @return true if the point lies on the line segment, false otherwise
     */
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


    /**
     * Checks if a position is inside a polygon defined by a list of vertices.
     *
     * @param position the position to check
     * @param vertices the list of vertices defining the polygon
     * @return true if the position is inside the polygon, false otherwise
     */
    public static boolean isInPolygon(Position position, List<Position> vertices) {
        int intersects = 0;
        for (int i = 0, j = 1; i < vertices.size() - 1; i++, j = i + 1) {
            // Check if the point lies on the border (using collinearity and boundary check)
            if (isOnLine(position, vertices.get(i), vertices.get(j))) {
                return true; // Point is on the border
            }
            // Check whether y of target position in the range of line
            boolean inRange = (vertices.get(i).getLat() > position.getLat())
                    != (vertices.get(j).getLat() > position.getLat());
            // Line to right, check whether there is interaction. Xt <= (Xj - Xi)*(Yt - Yi)/(Yj - Yi)+Xi
            boolean intersection = (position.getLng() <= (vertices.get(j).getLng() - vertices.get(i).getLng())
                    * (position.getLat() - vertices.get(i).getLat()) / (vertices.get(j).getLat() - vertices.get(i).getLat())
                    + vertices.get(i).getLng());
            if (inRange && intersection) {
                intersects++;
            }
        }
        return intersects % 2 == 1;  // odd count inside, even count outside
    }

    /**
     * Retrieves the restaurant associated with an order.
     *
     * @param order the order to get the restaurant for
     * @return the restaurant associated with the order, or null if not found
     */
    public static Restaurant getRestaurant(Order order) {
        List<Pizza> pizzasInOrder = order.getPizzasInOrder();
        List<Restaurant> restaurants = DataRetrive.getRestaurants();
        String restaurantCode = pizzasInOrder.get(0).getName().split(":")[0].trim();
        for (Restaurant restaurant : restaurants) {
            String code = restaurant.getMenu().get(0).getName().split(":")[0].trim();
            if (code.equals(restaurantCode)) {
                return restaurant;
            }
        }
        return null;
    }

    /**
     * Validates an order based on various criteria.
     *
     * @param order the order to validate
     * @return the validated order with updated status and validation code
     */
    public static OrderValidationResult validateOrder(Order order) {
        LocalDate orderDate = order.getOrderDate();
        int priceTotalInPence = order.getPriceTotalInPence();
        List<Pizza> pizzasInOrder = order.getPizzasInOrder();
        CardInfo creditCardInformation = order.getCreditCardInformation();

        if (pizzasInOrder.isEmpty()) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EMPTY_ORDER);
        }

        Restaurant restaurant = getRestaurant(order);

        if (!CheckOrderUtil.isValidCVV(creditCardInformation.getCvv())) {
            return new OrderValidationResult(OrderStatus.INVALID,OrderValidationCode.CVV_INVALID );

        } else if (!CheckOrderUtil.isValidExpiryDate(creditCardInformation.getCreditCardExpiry(), orderDate)) {
            return new OrderValidationResult(OrderStatus.INVALID,OrderValidationCode.EXPIRY_DATE_INVALID);

        } else if (!CheckOrderUtil.isValidCreditCardNumber(creditCardInformation.getCreditCardNumber())) {
            return new OrderValidationResult(OrderStatus.INVALID,OrderValidationCode.CARD_NUMBER_INVALID);

        } else if (pizzasInOrder.size() > 4) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);

        } else if (!CheckOrderUtil.isValidPriceTotalInPence(pizzasInOrder, priceTotalInPence)) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.TOTAL_INCORRECT);

        } else if (!CheckOrderUtil.isValidPizza(order)) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.PIZZA_NOT_DEFINED);

        } else if (!CheckOrderUtil.isSameRestaurant(pizzasInOrder)) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);

        } else if (!CheckOrderUtil.isOpen(restaurant, orderDate)) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.RESTAURANT_CLOSED);

        } else if (!CheckOrderUtil.isValidPizzaPrice(restaurant, pizzasInOrder)) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.PRICE_FOR_PIZZA_INVALID);
        }
        return new OrderValidationResult(OrderStatus.VALID, OrderValidationCode.NO_ERROR);

    }
}