package uk.ac.ed.inf.pizzadronz.model;

public class Position {

    private double lng;
    private double lat;

//    public Position(){
//        this.lng = Double.POSITIVE_INFINITY;
//        this.lat = Double.POSITIVE_INFINITY;
//    }

    public Position(Double lng, Double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
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