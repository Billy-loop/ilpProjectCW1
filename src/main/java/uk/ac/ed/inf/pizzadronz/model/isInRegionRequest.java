package uk.ac.ed.inf.pizzadronz.model;

public class isInRegionRequest {
    private Position position;
    private Region region;

    public isInRegionRequest(Position position, Region region) {
        this.position = position;
        this.region = region;
    }
    public Position getPosition() {
        return position;
    }
    public Region getRegion() {
        return region;
    }

}
