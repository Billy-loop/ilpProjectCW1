package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LngLatPairRequest {
    private Position position1;
    private Position position2;

    public LngLatPairRequest(Position position1, Position position2) {
        this.position1 = position1;
        this.position2 = position2;
    }

//    public void setPosition1(Position position1) {
//        this.position1 = position1;
//    }
//
//    public void setPosition2(Position position2) {
//        this.position2 = position2;
//    }

//    public boolean checkLngLatPair() {
//        if(position1 == null || position2 == null) {
//            return false;
//        }
//        if(!position1.isValidPosition()){
//            return false;
//        }
//        if(!position2.isValidPosition()){
//            return false;
//        }
//        return true;
//    }
}
