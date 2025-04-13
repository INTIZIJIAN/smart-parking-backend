package com.smartparking.parking_system.model;

public class ParkingSpot {
    private String id;
    private boolean isAvailable;
    private String reserveBy;

    public ParkingSpot(String id, String reserveBy) {
        this.id = id;
        this.isAvailable = true;
        this.reserveBy = null;
    }

    public String getId() { return id; }
    public String getReserveBy() { return reserveBy;}

    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }
    public void setReserveBy(String reserveBy) {reserveBy = reserveBy;}
}