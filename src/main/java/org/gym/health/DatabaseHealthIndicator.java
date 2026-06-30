package org.gym.health;

import org.gym.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHealthIndicator.class);

    private final UsersRepository usersRepository;

    public DatabaseHealthIndicator(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public Health health() {
        try {
            long users = usersRepository.count();
            return Health.up()
                    .withDetail("database", "reachable")
                    .withDetail("userCount", users)
                    .build();
        } catch (Exception ex) {
            log.error("Database health check failed", ex);
            return Health.down(ex)
                    .withDetail("database", "unreachable")
                    .build();
        }
    }
}
