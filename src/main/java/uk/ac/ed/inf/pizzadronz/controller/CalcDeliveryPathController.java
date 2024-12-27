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

import java.util.*;

@RestController
public class CalcDeliveryPathController {

    private static final List<Double> COMPASS_DIRECTIONS = new ArrayList<>(Arrays.asList(
            0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
            180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5
    ));

    public static final Position appletonTower = new Position(-3.186874, 55.944494);

    public static final List<Region> noFlyZones = DataRetrive.getNoFlyZone();

    private static final Region centreArea = DataRetrive.getCentralArea();


    @PostMapping("/calcDeliveryPath")
    public ResponseEntity<List<Position>> calcDeliveryPath(@RequestBody Order order) {
        // Validate the order
        OrderValidationResult validOrder = ImplementUtil.validateOrder(order);
        if (validOrder.getOrderStatus() == OrderStatus.INVALID) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Get restaurant location
        Restaurant restaurant = ImplementUtil.getRestaurant(order);
        Position start = restaurant.getLocation();

        // A* Search
        List<Position> path = aStarSearch(start, appletonTower);

        // Return the calculated path
        return path.isEmpty()
                ? ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
                : ResponseEntity.ok(path);
    }

    private List<Position> aStarSearch(Position start, Position goal) {

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        Set<Position> closedSet = new HashSet<>(); // Searched node

        Map<Position, Position> cameFrom = new HashMap<>();//Record the path
        Map<Position, Double> gScore = new HashMap<>(); //Store G value

        openSet.add(new Node(start, 0, heuristic(start, goal)));
        gScore.put(start, 0.0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            Position currentPos = current.getPosition();
            System.out.printf("Polling Node: %f, %f with F: %f\n", currentPos.getLng(), currentPos.getLat(), current.getF());

            // Arrived the goal
            if (ImplementUtil.isCloseTo(new LngLatPairRequest(currentPos, goal))) {

                return reconstructPath(cameFrom, currentPos);
            }

            closedSet.add(currentPos);

            // Iterate the neighbour
            for (double direction : COMPASS_DIRECTIONS) {
                Position neighbor = ImplementUtil.nextPosition(new NextPositionRequest(currentPos, direction));
                boolean crossNonFlyZone = isPathCrossingNoFlyZone(currentPos, neighbor, noFlyZones);

                // Restriction, nonFlyZone, leave centreArea.
                if (crossNonFlyZone || leaveCentreArea(neighbor, currentPos) || closedSet.contains(neighbor)) {
                    continue;
                }

                // Calculate g value, cost so far
                double tentativeGScore = gScore.getOrDefault(currentPos, Double.MAX_VALUE) + 0.00015; // new cost
                if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, currentPos);
                    gScore.put(neighbor, tentativeGScore);

                    double fScore = tentativeGScore + heuristic(neighbor, goal);

                    openSet.add(new Node(neighbor, tentativeGScore, fScore));
                }
            }
        }

        // Fail to find the path
        return new ArrayList<>();
    }

    private double heuristic(Position current, Position goal) {
        return ImplementUtil.distanceTo(new LngLatPairRequest(current, goal));
    }

    private List<Position> reconstructPath(Map<Position, Position> cameFrom, Position current) {
        List<Position> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private boolean leaveCentreArea(Position neighbor, Position current) {
        return ImplementUtil.isInPolygon(current, centreArea.getVertices()) &&
                !ImplementUtil.isInPolygon(neighbor, centreArea.getVertices());
    }


    /**
     * Checks if the line segment from currentPos to targetPos crosses any of the No-Fly-Zone edges.
     */
    public static boolean isPathCrossingNoFlyZone(Position currentPos,
                                                  Position targetPos,
                                                  List<Region> noFlyZones) {
        for (Region noFlyZone : noFlyZones) {
            List<Position> vertices = noFlyZone.getVertices();
            int n = vertices.size();
            // If the polygon is closed or we want to ensure we close it, iterate [0..n] using modulo
            for (int i = 0; i < n; i++) {
                Position v1 = vertices.get(i);
                Position v2 = vertices.get((i + 1) % n);

                if (segmentsIntersect(currentPos, targetPos, v1, v2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the two line segments p1->p2 and q1->q2 intersect.
     */
    private static boolean segmentsIntersect(Position p1, Position p2, Position q1, Position q2) {
        // 1) Quick bounding-box check (inline for efficiency)
        double p1x = p1.getLng(), p1y = p1.getLat();
        double p2x = p2.getLng(), p2y = p2.getLat();
        double q1x = q1.getLng(), q1y = q1.getLat();
        double q2x = q2.getLng(), q2y = q2.getLat();

        // If bounding boxes do not overlap, they cannot intersect.
        if (Math.max(p1x, p2x) < Math.min(q1x, q2x) ||
                Math.min(p1x, p2x) > Math.max(q1x, q2x) ||
                Math.max(p1y, p2y) < Math.min(q1y, q2y) ||
                Math.min(p1y, p2y) > Math.max(q1y, q2y)) {
            return false;
        }

        // 2) Orientation checks
        // orientation(p1, p2, q1) and orientation(p1, p2, q2)
        int o1 = orientation(p1x, p1y, p2x, p2y, q1x, q1y);
        int o2 = orientation(p1x, p1y, p2x, p2y, q2x, q2y);

        // orientation(q1, q2, p1) and orientation(q1, q2, p2)
        int o3 = orientation(q1x, q1y, q2x, q2y, p1x, p1y);
        int o4 = orientation(q1x, q1y, q2x, q2y, p2x, p2y);

        // General case: segments intersect if they properly straddle each other.
        if (o1 != o2 && o3 != o4) {
            return true;
        }


        return false;
    }

    /**
     * Returns:
     *  0 if p1->p2->p3 are collinear,
     * +1 if p1->p2->p3 turn counter-clockwise,
     * -1 if p1->p2->p3 turn clockwise.
     */
    private static int orientation(double x1, double y1,
                                   double x2, double y2,
                                   double x3, double y3) {
        // Cross product of vectors p1->p2 and p1->p3
        double val = (y2 - y1) * (x3 - x1) - (x2 - x1) * (y3 - y1);
        if (val > 0) {
            return +1; // CCW
        } else if (val < 0) {
            return -1; // CW
        } else {
            return 0;  // Collinear
        }
    }



}