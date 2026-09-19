package com.edgerunners.middleware.analysis;

import com.edgerunners.middleware.event.SecurityEvent;
import com.edgerunners.middleware.opa.OpaClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnomalyDetector {

    private final List<SecurityEvent> recentEvents = new ArrayList<>();
    private final OpaClient opaClient;

    public AnomalyDetector(OpaClient opaClient) {
        this.opaClient = opaClient;
    }

    public synchronized void analyze(SecurityEvent event) {

        recentEvents.add(event);

        long now = System.currentTimeMillis();

        recentEvents.removeIf(e -> {
            try {
                long eventTime = Instant.parse(e.timestamp()).toEpochMilli();
                return now - eventTime > 60_000;
            } catch (Exception ex) {
                return false;
            }
        });

        long sshFailures = recentEvents.stream()
                .filter(e -> "SSH_LOGIN_FAILURE".equals(e.eventType()))
                .filter(e -> e.sourceIp().equals(event.sourceIp()))
                .count();

        System.out.println("=== ANOMALY ANALYSIS ===");
        System.out.println("Source IP       : " + event.sourceIp());
        System.out.println("SSH failures/min: " + sshFailures);

        if (sshFailures >= 5) {

            System.out.println("[ALERT] Possible SSH brute-force attack!");
            System.out.println("[ALERT] Severity: HIGH");

            String decision = opaClient.decide(
                    true,
                    "HIGH",
                    event.sourceIp()
            );

            System.out.println("[OPA] Decision: " + decision);

            opaClient.triggerResponse(
                    decision,
                    event.sourceIp()
            );

        } else {

            System.out.println("[NORMAL] Activity within expected range");
        }
    }
}

