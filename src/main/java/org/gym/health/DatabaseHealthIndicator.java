package org.gym.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Custom health indicator that verifies the application can obtain a live
 * connection from the configured datasource. Exposed under the "database" key
 * of the /actuator/health endpoint.
 */
@Component("database")
public class DatabaseHealthIndicator implements HealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHealthIndicator.class);
    private static final int VALIDATION_TIMEOUT_SECONDS = 2;

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            boolean valid = connection.isValid(VALIDATION_TIMEOUT_SECONDS);
            if (valid) {
                return Health.up()
                        .withDetail("database", connection.getMetaData().getDatabaseProductName())
                        .build();
            }
            log.warn("Datasource connection reported as invalid during health check");
            return Health.down().withDetail("reason", "Connection is not valid").build();
        } catch (Exception ex) {
            log.error("Database health check failed", ex);
            return Health.down(ex).build();
        }
    }
}
