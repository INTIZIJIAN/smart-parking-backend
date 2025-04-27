package com.smartparking.parking_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;

public class Booking {
    private boolean isSuccess;
    private User user;
    private String status;

    public Booking(User user, boolean isSuccess, String status) {
        this.user = user;
        this.isSuccess = isSuccess;
        this.status = status;
    }

    public User getUser() { return user; }
    public boolean isSuccess() { return isSuccess; };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}