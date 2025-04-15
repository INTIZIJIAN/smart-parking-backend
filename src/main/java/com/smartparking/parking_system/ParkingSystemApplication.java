package com.smartparking.parking_system;

import com.smartparking.parking_system.model.Booking;
import com.smartparking.parking_system.service.ParkingSpotService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class ParkingSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(ParkingSystemApplication.class, args);
	}
}

//	@Bean
//	CommandLineRunner run(ParkingSpotService service) {
//		return args -> {
//			Runnable task = () -> {
//				String threadName = Thread.currentThread().getName();
//				Optional<Booking> booking = service.reserveSpot(threadName);
//				if (booking.isPresent()) {
//					System.out.println(threadName + " reserved spot " + booking.get().getSpot().getId());
//				} else {
//					System.out.println(threadName + " failed to reserve a spot");
//				}
//			};
//
//			for (int i = 0; i < 12; i++) {
//				new Thread(task, "User-" + i).start();
//			}
//		};
//	}