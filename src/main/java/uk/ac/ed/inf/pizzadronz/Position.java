package uk.ac.ed.inf.pizzadronz;

public class Position {
    private double lng;
    private double lat;

    public Position(){
        this.lng = Double.POSITIVE_INFINITY;
        this.lat = Double.POSITIVE_INFINITY;
    }

    public Position(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

//    @Override
//    public String toString() {
//        return "Position{lng:" + lng + ", lat:" + lat + "}";
//    }

}