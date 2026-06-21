package org.gym.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.core.entities.User;
import org.gym.exception.AuthenticationException;
import org.gym.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    private UsersRepository usersRepository;
    private MeterRegistry meterRegistry;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        meterRegistry = new SimpleMeterRegistry();
        authenticationService = new AuthenticationService(usersRepository, meterRegistry);
    }

    @Test
    void authenticatesValidCredentialsAndCountsSuccess() {
        User user = new User();
        user.setUserName("john.doe1");
        user.setPassword("secret12345");
        when(usersRepository.findByUsername("john.doe1")).thenReturn(Optional.of(user));

        authenticationService.authenticate("john.doe1", "secret12345");

        assertEquals(1.0, meterRegistry.counter("gym.authentication.attempts", "result", "success").count());
    }

    @Test
    void rejectsWrongPasswordAndCountsFailure() {
        User user = new User();
        user.setUserName("john.doe1");
        user.setPassword("secret12345");
        when(usersRepository.findByUsername("john.doe1")).thenReturn(Optional.of(user));

        assertThrows(AuthenticationException.class,
                () -> authenticationService.authenticate("john.doe1", "wrong"));
        assertEquals(1.0, meterRegistry.counter("gym.authentication.attempts", "result", "failure").count());
    }

    @Test
    void rejectsMissingCredentials() {
        assertThrows(AuthenticationException.class,
                () -> authenticationService.authenticate(null, null));
        assertEquals(1.0, meterRegistry.counter("gym.authentication.attempts", "result", "failure").count());
    }
}
