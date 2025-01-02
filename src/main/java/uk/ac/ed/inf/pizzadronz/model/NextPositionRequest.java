package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NextPositionRequest {
    private Position start;
    private Double angle;

    public NextPositionRequest(Position start, Double angle){
        this.start = start;
        this.angle = angle;
    }

}
