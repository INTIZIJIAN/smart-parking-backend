package com.smartparking.parking_system.model;

public class Booking {
    private ParkingSpot spot;
    private User user;

    public Booking(ParkingSpot spot, User user) {
        this.spot = spot;
        this.user = user;
    }

    public ParkingSpot getSpot() { return spot; }
    public User getUser() { return user; }
}