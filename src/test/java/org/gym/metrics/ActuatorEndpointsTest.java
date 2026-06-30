package org.gym.metrics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ActuatorEndpointsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void prometheusEndpointExposesCustomMetrics() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/prometheus", String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        String body = response.getBody();
        assertTrue(body.contains("gym_trainees_count"));
        assertTrue(body.contains("gym_trainers_count"));
        assertTrue(body.contains("gym_trainings_count"));
        assertTrue(body.contains("gym_authentication_attempts_total"));
    }

    @Test
    void healthEndpointReportsCustomIndicators() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);

        String body = response.getBody();
        assertTrue(body.contains("\"database\""));
        assertTrue(body.contains("\"trainingType\""));
    }
}
