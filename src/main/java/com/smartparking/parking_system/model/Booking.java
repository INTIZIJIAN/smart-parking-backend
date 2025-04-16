package com.smartparking.parking_system.model;

public class Booking {
    private ParkingSpot spot;
    private User user;
    private boolean isSuccess;


    public Booking(ParkingSpot spot, User user, boolean isSuccess) {
        this.spot = spot;
        this.user = user;
        this.isSuccess = isSuccess;
    }

    public ParkingSpot getSpot() { return spot; }
    public User getUser() { return user; }
    public boolean isSuccess() { return isSuccess; }
}