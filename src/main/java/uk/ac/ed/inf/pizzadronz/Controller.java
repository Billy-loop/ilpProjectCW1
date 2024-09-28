package uk.ac.ed.inf.pizzadronz;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.model.LngLatPairRequest;
import uk.ac.ed.inf.pizzadronz.model.NextPositionRequest;
import uk.ac.ed.inf.pizzadronz.model.Position;
import uk.ac.ed.inf.pizzadronz.model.isInRegionRequest;


@RestController
public class Controller {

    @GetMapping("/uuid")
    public String getUUid(){
        return "s" + "1234567";
    }

    @PostMapping("/distanceTo")
    public ResponseEntity<Double> distanceTo(@RequestBody LngLatPairRequest lnglat1){

        if (!checkLngLatPair(lnglat1)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (lnglat1 == null || lnglat1.getPosition1() == null || lnglat1.getPosition2() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        double lng1 = lnglat1.getPosition1().getLng();
        double lng2 = lnglat1.getPosition2().getLng();

        double lat1 = lnglat1.getPosition1().getLat();
        double lat2 = lnglat1.getPosition2().getLat();

        double distance = Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2));

        return ResponseEntity.ok(distance);
    }

    @PostMapping("/isCloseTo")
    public ResponseEntity<Boolean> isCloseTo(@RequestBody LngLatPairRequest lnglat1){


        if ( lnglat1 == null || !checkLngLatPair(lnglat1)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        double lng1 = lnglat1.getPosition1().getLng();
        double lng2 = lnglat1.getPosition2().getLng();

        double lat1 = lnglat1.getPosition1().getLat();
        double lat2 = lnglat1.getPosition2().getLat();

        double distance = Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2));

        if (distance < 0.00015) {
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/nextPosition")
    public ResponseEntity<Position> nextPosition(@RequestBody NextPositionRequest nextPosition){

        if (nextPosition == null || nextPosition.getStart() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(!isValidPosition(nextPosition.getStart())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(nextPosition.getAngle() == 0.0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (nextPosition.getAngle() == 999){
            return ResponseEntity.ok(nextPosition.getStart());
        }

        if(nextPosition.getAngle() >359 || nextPosition.getAngle() < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        double radian = Math.toRadians(nextPosition.getAngle());
        Position res = new Position(nextPosition.getStart().getLng()+Math.cos(radian)*0.00015,
               nextPosition.getStart().getLat()+Math.sin(radian)*0.00015);

       return ResponseEntity.ok(res);
    }
    @PostMapping("/isInRegion")
    public ResponseEntity<Boolean> isInRegion(@RequestBody isInRegionRequest request){
        return ResponseEntity.ok(true);
    }

    public boolean isValidPosition( Position position){
        if(position.getLng() == 0.0 || position.getLat() == 0.0){
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

        public boolean checkLngLatPair(LngLatPairRequest lnglat1 ) {
        if (lnglat1 == null){
            return false;
        }
        if(lnglat1.getPosition1() == null || lnglat1.getPosition2() == null) {
            return false;
        }
        if(!isValidPosition(lnglat1.getPosition1()) ||
                !isValidPosition(lnglat1.getPosition2())) {
            return false;
        }
        return true;
    }

}
