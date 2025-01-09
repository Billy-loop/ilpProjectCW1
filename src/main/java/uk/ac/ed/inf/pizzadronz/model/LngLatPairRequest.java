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
}
