package com.edgerunners.middleware.event;

import com.edgerunners.middleware.analysis.AnomalyDetector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    private final AnomalyDetector anomalyDetector;

    public EventController(AnomalyDetector anomalyDetector) {
        this.anomalyDetector = anomalyDetector;
    }

    @PostMapping
    public ResponseEntity<String> receiveEvent(
            @RequestBody SecurityEvent event) {

        System.out.println("=== SECURITY EVENT RECEIVED ===");
        System.out.println("Event Type : " + event.eventType());
        System.out.println("Source IP  : " + event.sourceIp());
        System.out.println("Username   : " + event.username());
        System.out.println("Timestamp  : " + event.timestamp());

        anomalyDetector.analyze(event);

        return ResponseEntity.ok("Event received successfully");
    }
}
