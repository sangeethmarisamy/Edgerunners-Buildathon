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

    public void triggerResponse(String action, String sourceIp) {

        if (!"BLOCK".equals(action)) {
            System.out.println("[RESPONSE] No action required");
            return;
        }

        try {
            System.out.println("[RESPONSE] Triggering Response Agent...");

            Process process = new ProcessBuilder(
                    "bash",
                    "../company-agent/response-agent/response_agent.sh",
                    action,
                    sourceIp
            )
                    .inheritIO()
                    .start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("[RESPONSE] Response Agent completed successfully");
            } else {
                System.out.println(
                        "[RESPONSE FAILED] Response Agent exited with code: "
                                + exitCode
                );
            }

        } catch (Exception e) {
            System.out.println("[RESPONSE FAILED] " + e.getMessage());
        }
    }
}
