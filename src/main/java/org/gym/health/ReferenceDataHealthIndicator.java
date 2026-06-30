package org.gym.health;

import org.gym.repository.TrainingTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Custom health indicator that reports the application as DOWN when the
 * mandatory training-type reference data has not been seeded, since most of the
 * domain features depend on it. Exposed under the "referenceData" key of the
 * /actuator/health endpoint.
 */
@Component("referenceData")
public class ReferenceDataHealthIndicator implements HealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(ReferenceDataHealthIndicator.class);

    private final TrainingTypeRepository trainingTypeRepository;

    public ReferenceDataHealthIndicator(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public Health health() {
        try {
            long trainingTypeCount = trainingTypeRepository.count();
            if (trainingTypeCount > 0) {
                return Health.up()
                        .withDetail("trainingTypeCount", trainingTypeCount)
                        .build();
            }
            log.warn("Reference data health check: no training types found");
            return Health.down()
                    .withDetail("reason", "No training types configured")
                    .build();
        } catch (Exception ex) {
            log.error("Reference data health check failed", ex);
            return Health.down(ex).build();
        }
    }
}
