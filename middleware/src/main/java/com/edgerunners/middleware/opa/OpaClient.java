package com.edgerunners.middleware.opa;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OpaClient {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("http://localhost:8181")
            .build();

    public String decide(boolean anomaly, String severity, String sourceIp) {

        Map<String, Object> input = Map.of(
                "input", Map.of(
                        "anomaly", anomaly,
                        "severity", severity,
                        "sourceIp", sourceIp
                )
        );

        Map<?, ?> response = restClient.post()
                .uri("/v1/data/edgerunners/security/action")
                .body(input)
                .retrieve()
                .body(Map.class);

        return String.valueOf(response.get("result"));
    }
}
