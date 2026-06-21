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
class TrainingTypeHealthIndicatorTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeHealthIndicator indicator;

    @Test
    void reportsUpWhenReferenceDataPresent() {
        when(trainingTypeRepository.count()).thenReturn(4L);

        Health health = indicator.health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals(4L, health.getDetails().get("trainingTypeCount"));
    }

    @Test
    void reportsDownWhenReferenceDataMissing() {
        when(trainingTypeRepository.count()).thenReturn(0L);

        Health health = indicator.health();

        assertEquals(Status.DOWN, health.getStatus());
    }
}
