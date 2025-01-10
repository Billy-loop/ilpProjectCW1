package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.model.IsInRegionRequest;
import uk.ac.ed.inf.pizzadronz.util.ImplementUtil;
import uk.ac.ed.inf.pizzadronz.util.SynSemCheck;

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

        if(!SynSemCheck.isValidPosition(request.getPosition())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(request.getRegion().getName() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(!SynSemCheck.isValidVertices(request.getRegion().getVertices())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(request.getRegion().getVertices().size() < 4){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(SynSemCheck.isOnStraightLine(request.getRegion().getVertices())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        boolean inside = ImplementUtil.isInPolygon(request.getPosition(),request.getRegion().getVertices());
        return ResponseEntity.ok(inside);
    }

}
