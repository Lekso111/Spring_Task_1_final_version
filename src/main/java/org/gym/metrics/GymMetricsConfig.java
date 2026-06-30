package org.gym.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GymMetricsConfig {

    @Bean
    public Gauge traineeCountGauge(MeterRegistry registry, TraineeRepository traineeRepository) {
        return Gauge.builder("gym.trainees.count", traineeRepository, repo -> safeCount(repo::count))
                .description("Current number of registered trainees")
                .register(registry);
    }

    @Bean
    public Gauge trainerCountGauge(MeterRegistry registry, TrainerRepository trainerRepository) {
        return Gauge.builder("gym.trainers.count", trainerRepository, repo -> safeCount(repo::count))
                .description("Current number of registered trainers")
                .register(registry);
    }

    @Bean
    public Gauge trainingCountGauge(MeterRegistry registry, TrainingRepository trainingRepository) {
        return Gauge.builder("gym.trainings.count", trainingRepository, repo -> safeCount(repo::count))
                .description("Current number of recorded trainings")
                .register(registry);
    }

    private static double safeCount(CountSupplier supplier) {
        try {
            return supplier.count();
        } catch (Exception ex) {
            return 0d;
        }
    }

    @FunctionalInterface
    private interface CountSupplier {
        long count();
    }
}
