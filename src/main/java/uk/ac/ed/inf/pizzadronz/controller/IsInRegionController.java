package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.model.IsInRegionRequest;
import uk.ac.ed.inf.pizzadronz.model.Position;
import uk.ac.ed.inf.pizzadronz.util.CommonFunction;
import java.util.List;

@RestController
public class IsInRegionController {
    @PostMapping("/isInRegion")
    public ResponseEntity<Boolean> isInRegion(@RequestBody IsInRegionRequest request){

        if (request == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (request.getRegion() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(!CommonFunction.isValidPosition(request.getPosition())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(request.getRegion().getName() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(!CommonFunction.checkVertices(request.getRegion().getVertices())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(request.getRegion().getVertices().size() < 4){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(CommonFunction.isOnStraightLine(request.getRegion().getVertices())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        boolean inside = isInPolygon(request.getPosition(),request.getRegion().getVertices());
        return ResponseEntity.ok(inside);
    }

    public boolean isInPolygon(Position position, List<Position> vertices) {
        int intersects = 0;
//        for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++) {
        for (int i =0,j =1 ; i < vertices.size()-1; i++, j = i+1){

            // Check if the point lies on the border (using collinearity and boundary check)
            if (CommonFunction.isOnLine(position, vertices.get(i), vertices.get(j))) {
                return true; // Point is on the border
            }

            //Check whether y of target position in the range of line
            boolean inRange = (vertices.get(i).getLat() > position.getLat())
                    != (vertices.get(j).getLat() > position.getLat());

            //Line to right, check whether there is interaction. Xt <= (Xj - Xi)*(Yt - Yi)/(Yj - Yi)+Xi
            boolean intersection = (position.getLng() <= (vertices.get(j).getLng() - vertices.get(i).getLng())
                    * (position.getLat() - vertices.get(i).getLat()) / (vertices.get(j).getLat() - vertices.get(i).getLat())
                    + vertices.get(i).getLng());

            if (inRange && intersection) {
                intersects++;
            }
        }
        return intersects % 2 == 1;  // odd count inside, even count outside
    }

}
