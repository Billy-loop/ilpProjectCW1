package uk.ac.ed.inf.pizzadronz;

public class NextPositionRequest {
    private Position start;
    private double angle;

    public NextPositionRequest(){
        this.start = new Position();
        this.angle = Double.POSITIVE_INFINITY;
    }
    public NextPositionRequest(Position start, double angle){
        this.start = start;
        this.angle = angle;
    }

    public Position getStart(){
        return this.start;
    }

    public double getAngle(){
        return angle;
    }

//    @Override
//    public String toString(){
//        return "NextPosition{position: " + this.start + ",angle: " + angle+"}";
//    }

}
