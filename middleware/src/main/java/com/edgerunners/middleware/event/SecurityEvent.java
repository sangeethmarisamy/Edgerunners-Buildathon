package com.edgerunners.middleware.event;

public record SecurityEvent(
        String eventType,
        String sourceIp,
        String username,
        String timestamp
) {
}
