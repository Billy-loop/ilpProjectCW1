package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Position {

    private Double lng;
    private Double lat;

    public Position(Double lng, Double lat) {
        this.lng = lng;
        this.lat = lat;
    }


    /**
     * Checks if this Position is equal to another object.
     * Two Position objects are considered equal if both their longitude
     * and latitude values are equal within double precision.
     *
     * @param o The object to compare to this Position.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        // Check if comparing with itself
        if (this == o) return true;

        // Check if the object is an instance of Position
        if (!(o instanceof Position)) return false;

        // Cast the object to Position and compare longitude and latitude
        Position other = (Position) o;
        return Double.compare(this.lng, other.lng) == 0 && Double.compare(this.lat, other.lat) == 0;
    }

    /**
     * Generates a hash code for this Position.
     * The hash code is computed using the longitude and latitude values,
     * ensuring that two equal Position objects have the same hash code.
     *
     * @return The hash code for this Position.
     */
    @Override
    public int hashCode() {
        // Convert the longitude and latitude to long bits and combine them
        long lngBits = Double.doubleToLongBits(lng);
        long latBits = Double.doubleToLongBits(lat);

        // XOR the bits to generate a unique hash code
        return (int) (lngBits ^ (lngBits >>> 32) ^ latBits ^ (latBits >>> 32));
    }
}