//package uk.ac.ed.inf.pizzadronz;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import uk.ac.ed.inf.pizzadronz.model.LngLatPairRequest;
//import uk.ac.ed.inf.pizzadronz.model.NextPositionRequest;
//import uk.ac.ed.inf.pizzadronz.model.Position;
//import uk.ac.ed.inf.pizzadronz.model.IsInRegionRequest;
//
//import java.util.List;
//import java.util.Objects;
//
//
//@RestController
//public class Controller {
//
//    @GetMapping("/uuid")
//    public String uuid(){
//        return "s" + "2292976";
//    }
//
//
//    @PostMapping("/distanceTo")
//    public ResponseEntity<Double> distanceTo(@RequestBody LngLatPairRequest lnglat1){
//
//        if (!checkLngLatPair(lnglat1)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        double lng1 = lnglat1.getPosition1().getLng();
//        double lng2 = lnglat1.getPosition2().getLng();
//
//        double lat1 = lnglat1.getPosition1().getLat();
//        double lat2 = lnglat1.getPosition2().getLat();
//
//        double distance = Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2));
//
//        return ResponseEntity.ok(distance);
//    }
//
//
//    @PostMapping("/isCloseTo")
//    public ResponseEntity<Boolean> isCloseTo(@RequestBody LngLatPairRequest lnglat1){
//
//        if(lnglat1 == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        //If checkLngLat is true, the if below will not run, vice visa
//        if (!checkLngLatPair(lnglat1)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        double lng1 = lnglat1.getPosition1().getLng();
//        double lng2 = lnglat1.getPosition2().getLng();
//
//        double lat1 = lnglat1.getPosition1().getLat();
//        double lat2 = lnglat1.getPosition2().getLat();
//
//        double distance = Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2));
//
//        if (distance < 0.00015) {
//            return ResponseEntity.ok(true);
//        }else{
//            return ResponseEntity.ok(false);
//        }
//    }
//
//
//    @PostMapping("/nextPosition")
//    public ResponseEntity<Position> nextPosition(@RequestBody NextPositionRequest nextPosition){
//
//        if (nextPosition == null ) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if(!isValidPosition(nextPosition.getStart())){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if(nextPosition.getAngle() == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if(nextPosition.getAngle() >360 || nextPosition.getAngle() < 0){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        double radian = Math.toRadians(nextPosition.getAngle());
//        Position res = new Position(nextPosition.getStart().getLng()+Math.cos(radian)*0.00015,
//               nextPosition.getStart().getLat()+Math.sin(radian)*0.00015);
//
//       return ResponseEntity.ok(res);
//    }
//
//
//    @PostMapping("/isInRegion")
//    public ResponseEntity<Boolean> isInRegion(@RequestBody IsInRegionRequest request){
//
//        if (request == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if (request.getRegion() == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if(!isValidPosition(request.getPosition())){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if(request.getRegion().getName() == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if(!checkVertices(request.getRegion().getVertices())){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if(request.getRegion().getVertices().size() < 4){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if(isOnStraightLine(request.getRegion().getVertices())){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        boolean inside = isInPolygon(request.getPosition(),request.getRegion().getVertices());
//        return ResponseEntity.ok(inside);
//    }
//
//    public boolean isInPolygon(Position position, List<Position> vertices) {
//        int intersects = 0;
////        for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++) {
//        for (int i =0,j =1 ; i < vertices.size()-1; i++, j = i+1){
//
//            // Check if the point lies on the border (using collinearity and boundary check)
//            if (isOnLine(position, vertices.get(i), vertices.get(j))) {
//                return true; // Point is on the border
//            }
//
//            //Check whether y of target position in the range of line
//            boolean inRange = (vertices.get(i).getLat() > position.getLat())
//                    != (vertices.get(j).getLat() > position.getLat());
//
//            //Line to right, check whether there is interaction. Xt <= (Xj - Xi)*(Yt - Yi)/(Yj - Yi)+Xi
//            boolean intersection = (position.getLng() <= (vertices.get(j).getLng() - vertices.get(i).getLng())
//                    * (position.getLat() - vertices.get(i).getLat()) / (vertices.get(j).getLat() - vertices.get(i).getLat())
//                    + vertices.get(i).getLng());
//
//            if (inRange && intersection) {
//                intersects++;
//            }
//        }
//        return intersects % 2 == 1;  // odd count inside, even count outside
//    }
//
//    // On border return ture, not return false
//    public boolean isOnLine(Position p, Position v1, Position v2) {
//        // Calculate the cross product (to check for collinearity)
//        double crossProduct = (p.getLat() - v1.getLat()) * (v2.getLng() - v1.getLng())
//                - (p.getLng() - v1.getLng()) * (v2.getLat() - v1.getLat());
//
//        // If crossProduct is not zero, the point is not collinear
//        if (crossProduct != 0) {
//            return false;
//        }
//
//        // Check if the point lies within the bounds of the line segment
//        boolean lngBounds = Math.min(v1.getLng(), v2.getLng()) <= p.getLng() &&
//                p.getLng() <= Math.max(v1.getLng(), v2.getLng());
//        boolean latBounds = Math.min(v1.getLat(), v2.getLat()) <= p.getLat() &&
//                p.getLat() <= Math.max(v1.getLat(), v2.getLat());
//
//        return lngBounds && latBounds;
//    }
//
//
//    // valid return ture, invalid return false.
//    public boolean isValidPosition( Position position){
//
//        if(position == null){
//            return false;
//        }
//
//        if(position.getLng() == null  || position.getLat() == null){
//            return false;
//        }
//        if(position.getLat() > 90 || position.getLat() < -90){
//            return false;
//        }
//        if(position.getLng() > 180 || position.getLng() < -180){
//            return false;
//        }
//        return true;
//    }
//
//    // valid return true, invalid return false;
//    public boolean checkLngLatPair(LngLatPairRequest lnglat1 ) {
//        if (lnglat1 == null){
//            return false;
//        }
//        if(lnglat1.getPosition1() == null || lnglat1.getPosition2() == null) {
//            return false;
//        }
//        // IF isValidPosition is true, the if below will not run, vice visa
//        if (!isValidPosition(lnglat1.getPosition1()) ||
//                !isValidPosition(lnglat1.getPosition2())) {
//            return false;
//        }
//        return true;
//    }
//
//    // Valid return true, invalid return false.
//    public boolean checkVertices(List<Position> vertices){
//
//        if (vertices == null){
//            return false;
//        }
//        //check the position in vertices is valid.
//        for (int i = 0; i < vertices.size(); i++) {
//            if(!isValidPosition(vertices.get(i))){
//                return false;
//            }
//        }
//        //check whether this polygon is close
//        if (!Objects.equals(vertices.get(0).getLng(), vertices.get(vertices.size() - 1).getLng()) ||
//                !Objects.equals(vertices.get(0).getLat(), vertices.get(vertices.size() - 1).getLat())) {
//            return false;
//        }
//        return true;
//    }
//
//    public boolean isOnStraightLine(List<Position> vertices) {
//        double x1 = vertices.get(0).getLng();
//        double y1 = vertices.get(0).getLat();
//        double x2 = vertices.get(1).getLng();
//        double y2 = vertices.get(1).getLat();
//
//        for (int i = 2; i < vertices.size(); i++) {
//            double x3 = vertices.get(i).getLng();
//            double y3 = vertices.get(i).getLat();
//
//            boolean notSameGrad = (((x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1))  != 0);
//
//            if (notSameGrad) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//}
