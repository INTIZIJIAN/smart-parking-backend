package com.smartparking.parking_system.model;

import java.time.Duration;

public class ParkingSpot {
    private String id;
    private boolean isAvailable;
    private String reservedBy;
    private Duration duration;
    private String parkingZone;

    public ParkingSpot(String id, String reserveBy, boolean isAvailable, Duration duration, String parkingZone) {
        this.id = id;
        this.isAvailable = isAvailable;
        this.reservedBy = reserveBy;
        this.duration = duration;
        this.parkingZone = parkingZone;
    }

    public String getId() { return id; }
    public String getReservedBy() { return reservedBy;}
    public Duration getDuration() { return duration;}
    public String getParkingZone() { return parkingZone;}

    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }
    public void setReservedBy(String userId) {reservedBy = userId;}
    public void setDuration(Duration duration) {this.duration = duration;}

}