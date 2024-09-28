package uk.ac.ed.inf.pizzadronz.model;

public class Position {

    private double lng;
    private double lat;

//    public Position(){
//        this.lng = Double.POSITIVE_INFINITY;
//        this.lat = Double.POSITIVE_INFINITY;
//    }

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

//    public boolean isValidPosition(){
//        if(lat == null || lng == null){
//            return false;
//        }
//        if(lat > 90 || lat < -90){
//            return false;
//        }
//        if(lng > 180 || lng < -180){
//            return false;
//        }
//        return true;
//    }

//    @Override
//    public String toString() {
//        return "Position{lng:" + lng + ", lat:" + lat + "}";
//    }

}