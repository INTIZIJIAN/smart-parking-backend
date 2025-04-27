package com.smartparking.parking_system.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

public class ParkingSpot {
    private String id;
    private boolean isAvailable;
    private String parkingZone;
    private List<TimeSlot> durations;

    public ParkingSpot(String id, boolean isAvailable, String parkingZone, List<TimeSlot> durations) {
        this.id = id;
        this.isAvailable = isAvailable;
        this.parkingZone = parkingZone;
        this.durations = durations;
    }

    // Getter and Setter
    public String getId() { return id; }

    public String getParkingZone() { return parkingZone; }

    public boolean isAvailable() { return isAvailable; }

    public List<TimeSlot> getDurations() { return durations; }

    public void setAvailable(boolean available) { this.isAvailable = available; }

    public void setDurations(List<TimeSlot> durations) { this.durations = durations; }
}