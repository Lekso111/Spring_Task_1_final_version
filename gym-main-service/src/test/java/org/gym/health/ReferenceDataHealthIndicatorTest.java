package org.gym.health;

import org.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReferenceDataHealthIndicatorTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private ReferenceDataHealthIndicator healthIndicator;

    @Test
    void reportsUpWhenTrainingTypesExist() {
        when(trainingTypeRepository.count()).thenReturn(5L);

        Health health = healthIndicator.health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals(5L, health.getDetails().get("trainingTypeCount"));
    }

    @Test
    void reportsDownWhenNoTrainingTypes() {
        when(trainingTypeRepository.count()).thenReturn(0L);

        Health health = healthIndicator.health();

        assertEquals(Status.DOWN, health.getStatus());
    }

    @Test
    void reportsDownWhenRepositoryThrows() {
        when(trainingTypeRepository.count()).thenThrow(new RuntimeException("db down"));

        Health health = healthIndicator.health();

        assertEquals(Status.DOWN, health.getStatus());
    }
}
