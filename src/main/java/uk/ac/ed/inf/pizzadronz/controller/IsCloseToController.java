package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.pizzadronz.model.LngLatPairRequest;
import uk.ac.ed.inf.pizzadronz.util.*;

@RestController
public class IsCloseToController {
    @PostMapping("/isCloseTo")
    public ResponseEntity<Boolean> isCloseTo(@RequestBody LngLatPairRequest lnglat1){

        if(lnglat1 == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        //If checkLngLat is true, the if below will not run, vice visa
        if (!SemanticChecker.checkLngLatPair(lnglat1)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        boolean isCloseTo = ImplementUtil.isCloseTo(lnglat1);
        return ResponseEntity.ok(isCloseTo);

    }
}
