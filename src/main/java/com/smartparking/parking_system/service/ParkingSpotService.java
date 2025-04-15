package com.smartparking.parking_system.service;

import com.smartparking.parking_system.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ParkingSpotService {

    private final Map<String, Booking> activeBookings = new ConcurrentHashMap<>();
    private final Map<String, ParkingSpot> spots = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ParkingSpotService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        for (char zone = 'A'; zone <= 'C'; zone++) {
            String zoneId = "ZONE_" + zone;
            for (int i = 1; i <= 12; i++) {
                String spotId = zoneId + "_SPOT_" + i;
                spots.put(spotId, new ParkingSpot(spotId, null, null, zoneId));
            }
        }
    }

    public Optional<Booking> reserveSpot(String userId) {
        lock.lock();
        try {
            for (Map.Entry<String, ParkingSpot> entry : spots.entrySet()) {
                ParkingSpot spot = entry.getValue();
                if (spot.isAvailable()) {
                    spot.setAvailable(false);
                    spot.setReserveBy(userId);

                    Booking booking = new Booking(spot, new User(userId));
                    activeBookings.put(entry.getKey(), booking);
                    messagingTemplate.convertAndSend("/topic/parking", spot);
                    return Optional.of(booking);
                }
            }
            return Optional.empty(); // No spots available
        } finally {
            lock.unlock();
        }
    }

    public boolean releaseSpot(String spotId) {
        lock.lock();
        try {
            Booking booking = activeBookings.remove(spotId);
            if (booking != null) {
                ParkingSpot spot = booking.getSpot();
                spot.setAvailable(true);
                messagingTemplate.convertAndSend("/topic/parking", spot);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public List<ParkingSpot> getAllSpots() {
        return new ArrayList<>(spots.values());
    }
}
