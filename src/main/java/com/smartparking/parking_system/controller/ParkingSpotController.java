package com.smartparking.parking_system.controller;

import com.smartparking.parking_system.model.*;
import com.smartparking.parking_system.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/api/parking")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService service;

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveSpot(
            @RequestParam String userId,
            @RequestParam String spotId,
            @RequestParam String[] selectedDurations) {

        Optional<Booking> booking = service.reserveSpot(userId, spotId, selectedDurations);
        return booking.isPresent() ? ResponseEntity.ok(booking.get()) : ResponseEntity.status(HttpStatus.CONFLICT).body("Slot already booked or invalid");
    }

    @PostMapping("/release")
    public ResponseEntity<?> releaseSpot(
            @RequestParam String spotId,
            @RequestParam String duration) {

        boolean success = service.releaseSpot(spotId, duration);
        return success ? ResponseEntity.ok("Slot released") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to release slot");
    }

    @GetMapping("/all")
    public List<ParkingSpot> all() {
        return service.getAllSpots();
    }

    @PostMapping("/enter")
    public ResponseEntity<?> enterSpot(
            @RequestParam String spotId,
            @RequestParam String duration){
        boolean success = service.enterSpot(spotId, duration);
        return success ? ResponseEntity.ok("Slot Occupied") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to enter slot");
    }

}