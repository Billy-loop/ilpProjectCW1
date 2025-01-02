package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsInRegionRequest {
    private Position position;
    private Region region;

    public IsInRegionRequest(Position position, Region region) {
        this.position = position;
        this.region = region;
    }

}
