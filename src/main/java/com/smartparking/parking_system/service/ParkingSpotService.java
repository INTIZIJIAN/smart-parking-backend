package com.smartparking.parking_system.service;

import com.smartparking.parking_system.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ParkingSpotService {

    private final Map<String, ParkingSpot> spots = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final SimpMessagingTemplate messagingTemplate;
    private final Queue<ReservationRequest> bufferQueue = new LinkedBlockingQueue<>();

    private static final int MAX_RETRY = 3;
    private static final long LOCK_TIMEOUT_MS = 3000;

    @Autowired
    public ParkingSpotService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        initializeSpots();
    }

    private void initializeSpots() {
        for (char zone = 'A'; zone <= 'C'; zone++) {
            String zoneId = "ZONE-" + zone;

            for (int i = 1; i <= 10; i++) {
                String spotId = zoneId + "-SPOT-" + i;

                boolean spotAvailable = (i == 10);
                List<TimeSlot> durations = new ArrayList<>();

                for (int h = 0; h < 12; h++) {
                    int start = 10 + h;
                    int end = start + 1;
                    boolean slotAvailable = spotAvailable && (h >= 0 && h <= 2);

                    durations.add(new TimeSlot(
                            String.format("%02d:00 - %02d:00", start, end),
                            slotAvailable,
                            null
                    ));
                }

                spots.put(spotId, new ParkingSpot(spotId, spotAvailable, zoneId, durations));
            }
        }
    }

    public Optional<Booking> reserveSpot(String userId, String spotId, String[] selectedDurations) {
        int attempt = 0;

        while (attempt < MAX_RETRY) {
            attempt++;
            boolean gotLock = false;
            try {
                gotLock = lock.tryLock(LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                if (!gotLock) {
                    System.out.println("retrying...: attempt " + attempt);
                    continue;
                }
                ParkingSpot spot = spots.get(spotId);
                if (spot == null) {
                    return Optional.empty();
                }
                List<TimeSlot> matchedSlots = new ArrayList<>();
                for (TimeSlot slot : spot.getDurations()) {
                    for (String selected : selectedDurations) {
                        if (slot.getDuration().equals(selected) && slot.isAvailable()) {
                            matchedSlots.add(slot);
                        }
                    }
                }
                if (matchedSlots.size() != selectedDurations.length) {
                    return Optional.empty();
                }
                for (TimeSlot slot : matchedSlots) {
                    Booking booking = new Booking(new User(userId), true, "BOOKED");
                    slot.setBooking(booking);
                    slot.setAvailable(false);
                }
                boolean anyAvailable = spot.getDurations().stream().anyMatch(TimeSlot::isAvailable);
                spot.setAvailable(anyAvailable);
                messagingTemplate.convertAndSend("/topic/parking", spot);
                return matchedSlots.isEmpty() ? Optional.empty() : Optional.of(matchedSlots.get(0).getBooking());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return Optional.empty();
            } finally {
                if (gotLock) {
                    lock.unlock();
                }
            }
        }
        return Optional.empty();
    }

    public boolean releaseSpot(String spotId, String selectedDuration) {
        try {
            if (!lock.tryLock(LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                System.out.println("Release operation timeout.");
                return false;
            }

            ParkingSpot spot = spots.get(spotId);
            if (spot == null) {
                return false;
            }

            for (TimeSlot slot : spot.getDurations()) {
                if (slot.getDuration().equals(selectedDuration) && !slot.isAvailable() && slot.getBooking() != null) {
                    slot.setAvailable(true);
                    slot.setBooking(null);

                    messagingTemplate.convertAndSend("/topic/parking", spot);
                    return true;
                }
            }

            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean enterSpot(String spotId, String duration) {
        try {
            if (!lock.tryLock(LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                System.out.println("Enter operation timeout.");
                return false;
            }

            ParkingSpot spot = spots.get(spotId);
            if (spot == null) {
                return false;
            }

            for (TimeSlot slot : spot.getDurations()) {
                if (slot.getDuration().equals(duration)) {
                    if (slot.getBooking() != null) {
                        slot.getBooking().setStatus("OCCUPIED");
                        messagingTemplate.convertAndSend("/topic/parking", spot);
                        return true;
                    }
                }
            }
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }
    }

    public List<ParkingSpot> getAllSpots() {
        return new ArrayList<>(spots.values());
    }

    private static class ReservationRequest {
        private final String userId;
        private final String spotId;
        private final String[] selectedDurations;

        public ReservationRequest(String userId, String spotId, String[] selectedDurations) {
            this.userId = userId;
            this.spotId = spotId;
            this.selectedDurations = selectedDurations;
        }
    }
}