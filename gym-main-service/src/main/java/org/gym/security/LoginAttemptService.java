package org.gym.security;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(5);

    private final ConcurrentHashMap<String, Attempt> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String username) {
        Attempt attempt = attempts.get(username);
        if (attempt == null || attempt.blockedUntil == null) {
            return false;
        }
        if (Instant.now().isAfter(attempt.blockedUntil)) {
            attempts.remove(username);
            return false;
        }
        return true;
    }

    public void loginFailed(String username) {
        Attempt attempt = attempts.computeIfAbsent(username, key -> new Attempt());
        attempt.count++;
        if (attempt.count >= MAX_ATTEMPTS) {
            attempt.blockedUntil = Instant.now().plus(BLOCK_DURATION);
        }
    }

    public void loginSucceeded(String username) {
        attempts.remove(username);
    }

    private static class Attempt {
        private int count;
        private Instant blockedUntil;
    }
}
