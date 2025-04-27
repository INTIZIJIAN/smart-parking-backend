package com.smartparking.parking_system.model;

import java.time.LocalDateTime;

public class BookingDTO {
    private String userId;
    private String spotId;
    private String parkingZone;
    private boolean success;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public BookingDTO(Booking booking) {
        this.userId = booking.getUser().getUserId();
        this.success = booking.isSuccess();
    }

    public String getUserId() { return userId; }
    public String getSpotId() { return spotId; }
    public String getParkingZone() { return parkingZone; }
    public boolean isSuccess() { return success; }
}
