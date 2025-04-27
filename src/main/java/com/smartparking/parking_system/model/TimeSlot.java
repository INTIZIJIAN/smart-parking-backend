package com.smartparking.parking_system.model;

public class TimeSlot {
    private String duration;
    private boolean available;
    private Booking booking;

    public TimeSlot(String duration, boolean available, Booking booking) {
        this.duration = duration;
        this.available = available;
        this.booking = booking;
    }

    // Getter and Setter
    public String getDuration() { return duration; }

    public boolean isAvailable() { return available; }

    public void setDuration(String duration) { this.duration = duration; }

    public void setAvailable(boolean available) { this.available = available; }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
