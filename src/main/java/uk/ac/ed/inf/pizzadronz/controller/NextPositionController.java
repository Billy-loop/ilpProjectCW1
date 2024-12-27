package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.model.NextPositionRequest;
import uk.ac.ed.inf.pizzadronz.model.Position;
import uk.ac.ed.inf.pizzadronz.util.*;

@RestController
public class NextPositionController {
    @PostMapping("/nextPosition")
    public ResponseEntity<Position> nextPosition(@RequestBody NextPositionRequest nextPosition){

        if (nextPosition == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(!SemanticChecker.isValidPosition(nextPosition.getStart())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(nextPosition.getAngle() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(nextPosition.getAngle() >360 || nextPosition.getAngle() < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Position res = ImplementUtil.nextPosition(nextPosition);

        return ResponseEntity.ok(res);
    }
}
