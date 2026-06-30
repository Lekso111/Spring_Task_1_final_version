package org.gym.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Registers custom application metrics that are scraped by Prometheus via the
 * /actuator/prometheus endpoint. The gauges expose the current number of
 * trainees, trainers and trainings stored in the system.
 */
@Component
public class GymMetrics implements MeterBinder {

    private static final Logger log = LoggerFactory.getLogger(GymMetrics.class);

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;

    public GymMetrics(TraineeRepository traineeRepository,
                      TrainerRepository trainerRepository,
                      TrainingRepository trainingRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        log.info("Registering custom gym metrics for Prometheus");

        Gauge.builder("gym_trainees_total", traineeRepository, this::safeCount)
                .description("Current number of registered trainees")
                .register(registry);

        Gauge.builder("gym_trainers_total", trainerRepository, this::safeCount)
                .description("Current number of registered trainers")
                .register(registry);

        Gauge.builder("gym_trainings_total", trainingRepository, this::safeCount)
                .description("Current number of recorded trainings")
                .register(registry);
    }

    private double safeCount(org.springframework.data.repository.CrudRepository<?, ?> repository) {
        try {
            return repository.count();
        } catch (Exception ex) {
            log.warn("Failed to read entity count for metrics: {}", ex.getMessage());
            return Double.NaN;
        }
    }
}
