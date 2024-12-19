package uk.ac.ed.inf.pizzadronz.model;

public class Position {

    private Double lng;
    private Double lat;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position other = (Position) o;

        return Double.compare(this.lng, other.lng) == 0 && Double.compare(this.lat, other.lat) == 0;
    }

    @Override
    public int hashCode() {

        long lngBits = Double.doubleToLongBits(lng);
        long latBits = Double.doubleToLongBits(lat);
        return (int) (lngBits ^ (lngBits >>> 32) ^ latBits ^ (latBits >>> 32));
    }

}