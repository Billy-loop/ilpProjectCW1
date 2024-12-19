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

    private static Region centreArea = DataRetrive.getCentralArea();


//    @PostMapping("/calcDeliveryPath")
//    public ResponseEntity <List<Position>> calcDeliveryPath(@RequestBody Order order){
//        Order validOrder = ImplementUtil.validateOrder(order);
//        if(validOrder.getOrderStatus() == OrderStatus.INVALID){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        //Set the initial parameter
//        Restaurant restaurant = ImplementUtil.getRestaurant(order);
//        Position curLoc = restaurant.getLocation();
//
//        List<Position> deliveryPath = new ArrayList<Position>();
//        List<Position> reachPoint = new ArrayList<>();
//        boolean InCentreArea = false;
//
//        deliveryPath.add(curLoc);
//        reachPoint.add(curLoc);
//
//        Position predictLoc;
//        List <Double> direction = new ArrayList<>(Arrays.asList(
//                0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
//                180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5
//        ));
//
//        LngLatPairRequest curPair = new LngLatPairRequest(curLoc, appletonTower);
////        System.out.println(1);
//        while (!ImplementUtil.isCloseTo(curPair)){
//
//            double deltaY = curPair.getPosition2().getLat() - curPair.getPosition1().getLat();
//            double deltaX = curPair.getPosition2().getLng() - curPair.getPosition1().getLng();
//            double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
//            double nextAngle = findClosestDirection(angle, COMPASS_DIRECTIONS);
//
//
//            predictLoc = ImplementUtil.nextPosition(new NextPositionRequest(curLoc, nextAngle));
//            //NextPosition in nonFlyZone, or in centre area but leave
//            while ((isInNonFlyZone(predictLoc) || (InCentreArea && leaveCentreArea(predictLoc))) || reachPoint.contains(predictLoc)) {
//                direction.remove(nextAngle);
//                nextAngle = findClosestDirection(angle, direction);
//                predictLoc = ImplementUtil.nextPosition(new NextPositionRequest(curLoc, nextAngle));
//            }
//
//            curLoc = predictLoc;
//            direction = new ArrayList<>(Arrays.asList(
//                    0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
//                    180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5
//            ));
//            if(ImplementUtil.isInPolygon(curLoc, centreArea.getVertices())){
//                InCentreArea = true;
//            }
//
//            deliveryPath.add(curLoc);
//            reachPoint.add(curLoc);
//            curPair.setPosition1(curLoc);
//            System.out.printf("Current lng: %f current lat: %f ; Target lng: %f Target lat: %f\n", curPair.getPosition1().getLng(), curPair.getPosition1().getLat(),
//                    curPair.getPosition2().getLng(), curPair.getPosition2().getLat());
//
//        }
//
//        return ResponseEntity.ok(deliveryPath);
//    }
//
//    // Method to calculate the closest compass direction
//    public static double findClosestDirection(double angle, List <Double> directions) {
//        // Normalize angle to range [0, 360)
//        if (angle < 0) {
//            angle += 360;
//        }
//
//        double closestDifference = 360;  // Start with a large difference
//        int closestIndex = -1;
//
//        // Find the closest compass direction
//        for (int i = 0; i < directions.size(); i++) {
//            double difference = Math.abs(angle - directions.get(i));
//            if (difference > 180) {
//                difference = 360 - difference;
//            }
//            if (difference < closestDifference) {
//                closestDifference = difference;
//                closestIndex = i;
//            }
//        }
//        // Return the name of the closest direction
//        return directions.get(closestIndex);
//    }
//
//    public static boolean isInNonFlyZone(Position position) {
//        for (Region nonFlyZone : noFlyZones) {
//            if(ImplementUtil.isInPolygon(position, nonFlyZone.getVertices())){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static  boolean leaveCentreArea(Position position) {
//        return !ImplementUtil.isInPolygon(position, centreArea.getVertices());
//    }
//}

    @PostMapping("/calcDeliveryPath")
    public ResponseEntity<List<Position>> calcDeliveryPath(@RequestBody Order order) {
        // Validate the order
        Order validOrder = ImplementUtil.validateOrder(order);
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
        // Open Set 用优先队列存储待处理节点
        //PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        Set<Position> closedSet = new HashSet<>(); // 已处理节点

        Map<Position, Position> cameFrom = new HashMap<>();
        Map<Position, Double> gScore = new HashMap<>();

        // 初始化起点
        openSet.add(new Node(start, 0, heuristic(start, goal)));
        gScore.put(start, 0.0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            Position currentPos = current.getPosition();
            System.out.printf("Polling Node: %f, %f with F: %f\n", currentPos.getLng(), currentPos.getLat(), current.getF());

            // 如果到达目标
            if (ImplementUtil.isCloseTo(new LngLatPairRequest(currentPos, goal))) {
                return reconstructPath(cameFrom, currentPos);
            }

            closedSet.add(currentPos);

            // 遍历当前节点的所有邻居
            for (double direction : COMPASS_DIRECTIONS) {
                Position neighbor = ImplementUtil.nextPosition(new NextPositionRequest(currentPos, direction));

                // 如果邻居在禁飞区或离开中央区域，跳过
                if (isInNonFlyZone(neighbor) || leaveCentreArea(neighbor, currentPos) || closedSet.contains(neighbor)) {
                    continue;
                }
                //System.out.printf("neighbor: Lng: %f, Lat: %f\n", neighbor.getLng(), neighbor.getLat());
                // 计算从当前节点到邻居的 g 值
                double tentativeGScore = gScore.getOrDefault(currentPos, Double.MAX_VALUE) + 0.00015; // new cost
                if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, currentPos);
                    gScore.put(neighbor, tentativeGScore);

                    double fScore = tentativeGScore + heuristic(neighbor, goal);

                    openSet.add(new Node(neighbor, tentativeGScore, fScore));
                }
            }
        }

        // 如果找不到路径，返回空
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


    public static boolean isPathCrossingNoFlyZone(Position currentPos, Position targetPos, List<Region> noFlyZones) {
        for (Region noFlyZone : noFlyZones) {
            List<Position> vertices = noFlyZone.getVertices();
            for (int i = 0; i < vertices.size() - 1; i++) {
                Position v1 = vertices.get(i);
                Position v2 = vertices.get(i + 1);

                if (doLineSegmentsIntersect(currentPos, targetPos, v1, v2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean doLineSegmentsIntersect(Position p1, Position p2, Position q1, Position q2) {
        return (ccw(p1, q1, q2) != ccw(p2, q1, q2)) && (ccw(p1, p2, q1) != ccw(p1, p2, q2));
    }

    public static boolean isInNonFlyZone(Position position) {
        for (Region nonFlyZone : noFlyZones) {
            if(ImplementUtil.isInPolygon(position, nonFlyZone.getVertices())){
                return true;
            }
        }
        return false;
    }

    private static boolean ccw(Position a, Position b, Position c) {
        return (c.getLat() - a.getLat()) * (b.getLng() - a.getLng()) > (b.getLat() - a.getLat()) * (c.getLng() - a.getLng());
    }

}