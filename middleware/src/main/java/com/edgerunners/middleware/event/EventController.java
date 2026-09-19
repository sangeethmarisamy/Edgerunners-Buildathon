package com.edgerunners.middleware.event;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    @PostMapping
    public ResponseEntity<String> receiveEvent(@RequestBody SecurityEvent event) {

        System.out.println("=== SECURITY EVENT RECEIVED ===");
        System.out.println("Event Type : " + event.eventType());
        System.out.println("Source IP  : " + event.sourceIp());
        System.out.println("Username   : " + event.username());
        System.out.println("Timestamp  : " + event.timestamp());

        return ResponseEntity.ok("Event received successfully");
    }
}
