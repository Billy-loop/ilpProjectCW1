package uk.ac.ed.inf.pizzadronz.model;

public class IsInRegionRequest {
    private Position position;
    private Region region;

    public IsInRegionRequest(Position position, Region region) {
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
