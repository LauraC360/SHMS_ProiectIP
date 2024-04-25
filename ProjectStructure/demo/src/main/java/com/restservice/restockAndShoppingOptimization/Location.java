package com.restservice.restockAndShoppingOptimization;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Location {
    private double latitude;
    private double longitude;
    public double getLatitude() {
        return this.latitude;
    }
    public double getLongitude() {
        return this.longitude;
    }
}
