package org.gym.health;

import org.gym.repository.UsersRepository;
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
class DatabaseHealthIndicatorTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private DatabaseHealthIndicator indicator;

    @Test
    void reportsUpWhenDatabaseReachable() {
        when(usersRepository.count()).thenReturn(5L);

        Health health = indicator.health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals(5L, health.getDetails().get("userCount"));
    }

    @Test
    void reportsDownWhenDatabaseFails() {
        when(usersRepository.count()).thenThrow(new RuntimeException("connection refused"));

        Health health = indicator.health();

        assertEquals(Status.DOWN, health.getStatus());
    }
}
