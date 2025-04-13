package com.smartparking.parking_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ParkingWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifySpotUpdate(String message) {
        messagingTemplate.convertAndSend("/topic/parking", message);
    }

    @MessageMapping("/hello")
    public void handleHello(String name) {
        // can also receive messages from client if needed
    }
}