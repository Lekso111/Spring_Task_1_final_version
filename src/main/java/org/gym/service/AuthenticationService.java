package org.gym.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.core.entities.User;
import org.gym.exception.AuthenticationException;
import org.gym.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UsersRepository usersRepository;
    private final Counter successCounter;
    private final Counter failureCounter;

    public AuthenticationService(UsersRepository usersRepository, MeterRegistry meterRegistry) {
        this.usersRepository = usersRepository;
        this.successCounter = meterRegistry.counter("gym.authentication.attempts", "result", "success");
        this.failureCounter = meterRegistry.counter("gym.authentication.attempts", "result", "failure");
    }

    @Transactional(readOnly = true)
    public void authenticate(String username, String password) {
        if (username == null || password == null) {
            failureCounter.increment();
            log.warn("Authentication failed: missing credentials");
            throw new AuthenticationException("Username and password are required");
        }
        User user = usersRepository.findByUsername(username)
                .orElseThrow(() -> {
                    failureCounter.increment();
                    log.warn("Authentication failed for username {}", username);
                    return new AuthenticationException("Invalid username or password");
                });
        if (!user.getPassword().equals(password)) {
            failureCounter.increment();
            log.warn("Authentication failed for username {}", username);
            throw new AuthenticationException("Invalid username or password");
        }
        successCounter.increment();
        log.debug("Authentication succeeded for username {}", username);
    }
}
