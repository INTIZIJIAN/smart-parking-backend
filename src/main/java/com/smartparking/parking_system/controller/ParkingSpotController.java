package com.smartparking.parking_system.controller;

import com.smartparking.parking_system.model.*;
import com.smartparking.parking_system.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/parking")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService service;

    @PostMapping("/reserve")
    public Booking reserve(@RequestParam String userId) {
        Optional<Booking> booking = service.reserveSpot(userId);
        if (booking.isEmpty()) throw new RuntimeException("No available spot");
        return booking.orElse(null);
    }

    @PostMapping("/release")
    public String release(@RequestParam String spotId) {
        boolean success = service.releaseSpot(spotId);
        return success ? "Released" : "Invalid spot or already free";
    }

    @GetMapping("/all")
    public List<ParkingSpot> all() {
        return service.getAllSpots();
    }
}