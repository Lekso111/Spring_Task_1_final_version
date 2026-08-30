package org.gym.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymMetricsTest {

    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private GymMetrics gymMetrics;

    @Test
    void registersGaugesWithCurrentCounts() {
        when(traineeRepository.count()).thenReturn(3L);
        when(trainerRepository.count()).thenReturn(2L);
        when(trainingRepository.count()).thenReturn(7L);

        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        gymMetrics.bindTo(registry);

        assertNotNull(registry.find("gym_trainees_total").gauge());
        assertEquals(3.0, registry.get("gym_trainees_total").gauge().value());
        assertEquals(2.0, registry.get("gym_trainers_total").gauge().value());
        assertEquals(7.0, registry.get("gym_trainings_total").gauge().value());
    }
}
