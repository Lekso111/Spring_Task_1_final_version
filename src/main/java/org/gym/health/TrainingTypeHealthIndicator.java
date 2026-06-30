package org.gym.health;

import org.gym.repository.TrainingTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeHealthIndicator implements HealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(TrainingTypeHealthIndicator.class);

    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeHealthIndicator(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public Health health() {
        try {
            long count = trainingTypeRepository.count();
            if (count == 0) {
                log.warn("No training types are configured");
                return Health.down()
                        .withDetail("trainingTypes", "missing reference data")
                        .build();
            }
            return Health.up()
                    .withDetail("trainingTypeCount", count)
                    .build();
        } catch (Exception ex) {
            log.error("Training type health check failed", ex);
            return Health.down(ex).build();
        }
    }
}
