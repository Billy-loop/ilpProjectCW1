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
            if (SemanticChecker.isOnLine(position, vertices.get(i), vertices.get(j))) {
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
    public static Order validateOrder(Order order) {
        String orderNo = order.getOrderNo();
        LocalDate orderDate = order.getOrderDate();
        int priceTotalInPence = order.getPriceTotalInPence();
        List<Pizza> pizzasInOrder = order.getPizzasInOrder();
        CardInfo creditCardInformation = order.getCreditCardInformation();

        if (pizzasInOrder.isEmpty()) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.EMPTY_ORDER);
            return order;
        }

        Restaurant restaurant = getRestaurant(order);

        if (!CheckOrderUtil.isValidDate(orderDate)) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
            return order;
        } else if (!CheckOrderUtil.isValidCVV(creditCardInformation.getCvv())) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
            return order;
        } else if (!CheckOrderUtil.isValidExpiryDate(creditCardInformation.getCreditCardExpiry(), orderDate)) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
            return order;
        } else if (!CheckOrderUtil.isValidCreditCardNumber(creditCardInformation.getCreditCardNumber())) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
            return order;
        } else if (pizzasInOrder.size() > 4) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
            return order;
        } else if (!CheckOrderUtil.isValidPriceTotalInPence(pizzasInOrder, priceTotalInPence)) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
            return order;
        } else if (!CheckOrderUtil.isValidPizza(order)) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
            return order;
        } else if (!CheckOrderUtil.isSameRestaurant(pizzasInOrder)) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
            return order;
        } else if (!CheckOrderUtil.isOpen(restaurant, orderDate)) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
            return order;
        } else if (!CheckOrderUtil.isValidPizzaPrice(restaurant, pizzasInOrder)) {
            order.setOrderStatus(OrderStatus.INVALID);
            order.setOrderValidationCode(OrderValidationCode.PRICE_FOR_PIZZA_INVALID);
            return order;
        }

        order.setOrderStatus(OrderStatus.VALID);
        order.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        return order;
    }
}