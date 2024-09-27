package uk.ac.ed.inf.pizzadronz;

public class LngLatPairRequest {
    private Position position1;
    private Position position2;

    public LngLatPairRequest(Position position1, Position position2) {
        this.position1 = position1;
        this.position2 = position2;
    }

    public Position getPosition1() {
        return position1;
    }
    public void setPosition1(Position position1) {
        this.position1 = position1;
    }
    public Position getPosition2() {
        return position2;
    }
    public void setPosition2(Position position2) {
        this.position2 = position2;
    }
}
