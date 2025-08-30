package com.sevenblue.backendvault.config;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test") // Use a test profile if needed, or default
@DisplayName("DataSourceConfig - HikariCP Configuration Tests")
public class DataSourceConfigTest {

    @Autowired
    private HikariDataSource hikariDataSource;

    @Test
    @DisplayName("1. HikariDataSource Bean should be loaded")
    void hikariDataSourceBeanShouldBeLoaded() {
        assertNotNull(hikariDataSource, "HikariDataSource bean should not be null");
    }

    @Test
    @DisplayName("2. Performance Critical Settings - Pool Sizing")
    void poolSizingSettingsShouldBeCorrect() {
        assertEquals(50, hikariDataSource.getMaximumPoolSize(), "MaximumPoolSize should be 50");
        assertEquals(10, hikariDataSource.getMinimumIdle(), "MinimumIdle should be 10");
    }

    @Test
    @DisplayName("3. Performance Critical Settings - Timeout Settings")
    void timeoutSettingsShouldBeCorrect() {
        assertEquals(250, hikariDataSource.getConnectionTimeout(), "ConnectionTimeout should be 250ms");
        assertEquals(250, hikariDataSource.getValidationTimeout(), "ValidationTimeout should be 250ms");
        assertEquals(300_000, hikariDataSource.getIdleTimeout(), "IdleTimeout should be 300_000ms (5 minutes)");
        assertEquals(600_000, hikariDataSource.getMaxLifetime(), "MaxLifetime should be 600_000ms (10 minutes)");
    }

    @Test
    @DisplayName("4. Connection Validation Settings")
    void connectionValidationSettingsShouldBeCorrect() {
        assertEquals("SELECT 1", hikariDataSource.getConnectionTestQuery(), "ConnectionTestQuery should be 'SELECT 1'");
    }

    @Test
    @DisplayName("5. Leak Detection Settings")
    void leakDetectionSettingsShouldBeCorrect() {
        assertEquals(30_000, hikariDataSource.getLeakDetectionThreshold(), "LeakDetectionThreshold should be 30_000ms (30 seconds)");
    }

    @Test
    @DisplayName("6. ClickHouse Specific Optimizations - Socket Level")
    void clickhouseSocketOptimizationsShouldBeCorrect() {
        assertTrue(hikariDataSource.getDataSourceProperties().containsKey("socket_timeout"), "Should contain socket_timeout property");
        assertEquals("300000", hikariDataSource.getDataSourceProperties().getProperty("socket_timeout"), "socket_timeout should be 300000");

        assertTrue(hikariDataSource.getDataSourceProperties().containsKey("tcp_keep_alive"), "Should contain tcp_keep_alive property");
        assertEquals("true", hikariDataSource.getDataSourceProperties().getProperty("tcp_keep_alive"), "tcp_keep_alive should be true");

        assertTrue(hikariDataSource.getDataSourceProperties().containsKey("tcp_no_delay"), "Should contain tcp_no_delay property");
        assertEquals("true", hikariDataSource.getDataSourceProperties().getProperty("tcp_no_delay"), "tcp_no_delay should be true");
    }

    @Test
    @DisplayName("7. ClickHouse Specific Optimizations - Query Optimizations")
    void clickhouseQueryOptimizationsShouldBeCorrect() {
        assertTrue(hikariDataSource.getDataSourceProperties().containsKey("max_execution_time"), "Should contain max_execution_time property");
        assertEquals("60", hikariDataSource.getDataSourceProperties().getProperty("max_execution_time"), "max_execution_time should be 60");

        assertTrue(hikariDataSource.getDataSourceProperties().containsKey("max_threads"), "Should contain max_threads property");
        assertEquals("4", hikariDataSource.getDataSourceProperties().getProperty("max_threads"), "max_threads should be 4");

        assertTrue(hikariDataSource.getDataSourceProperties().containsKey("compression"), "Should contain compression property");
        assertEquals("true", hikariDataSource.getDataSourceProperties().getProperty("compression"), "compression should be true");
    }

    @Test
    @DisplayName("8. ClickHouse Specific Optimizations - Buffer Size")
    void clickhouseBufferSizeOptimizationsShouldBeCorrect() {
        assertTrue(hikariDataSource.getDataSourceProperties().containsKey("max_insert_block_size"), "Should contain max_insert_block_size property");
        assertEquals("1048576", hikariDataSource.getDataSourceProperties().getProperty("max_insert_block_size"), "max_insert_block_size should be 1048576");

        assertTrue(hikariDataSource.getDataSourceProperties().containsKey("min_insert_block_size_rows"), "Should contain min_insert_block_size_rows property");
        assertEquals("500", hikariDataSource.getDataSourceProperties().getProperty("min_insert_block_size_rows"), "min_insert_block_size_rows should be 500");
    }

    @Test
    @DisplayName("9. Connection Pool Naming")
    void connectionPoolNamingShouldBeCorrect() {
        assertEquals("TradingHikariPool", hikariDataSource.getPoolName(), "PoolName should be 'TradingHikariPool'");
    }
}
