package com.smartparking.parking_system.model;

import java.time.Duration;

public class ParkingSpot {
    private String id;
    private boolean isAvailable;
    private String reserveBy;
    private Duration duration;
    private String parkingZone;

    public ParkingSpot(String id, String reserveBy, Duration duration, String parkingZone) {
        this.id = id;
        this.isAvailable = true;
        this.reserveBy = reserveBy;
        this.duration = duration;
        this.parkingZone = parkingZone;
    }

    public String getId() { return id; }
    public String getReserveBy() { return reserveBy;}
    public Duration getDuration() { return duration;}
    public String getParkingZone() { return parkingZone;}

    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }
    public void setReserveBy(String reserveBy) {reserveBy = reserveBy;}
    public void setDuration(Duration duration) {this.duration = duration;}

}