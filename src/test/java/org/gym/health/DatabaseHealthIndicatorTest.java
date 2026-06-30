package org.gym.health;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseHealthIndicatorTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private DatabaseMetaData metaData;

    @Test
    void reportsUpWhenConnectionIsValid() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(anyInt())).thenReturn(true);
        lenient().when(connection.getMetaData()).thenReturn(metaData);
        lenient().when(metaData.getDatabaseProductName()).thenReturn("PostgreSQL");

        Health health = new DatabaseHealthIndicator(dataSource).health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals("PostgreSQL", health.getDetails().get("database"));
    }

    @Test
    void reportsDownWhenConnectionInvalid() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(anyInt())).thenReturn(false);

        Health health = new DatabaseHealthIndicator(dataSource).health();

        assertEquals(Status.DOWN, health.getStatus());
    }

    @Test
    void reportsDownWhenConnectionThrows() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException("boom"));

        Health health = new DatabaseHealthIndicator(dataSource).health();

        assertEquals(Status.DOWN, health.getStatus());
    }
}
