package com.sevenblue.backendvault.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * HikariCP Configuration optimized for high-frequency trading scenarios.
 * 
 * Key optimizations:
 * - Sub-millisecond connection acquisition
 * - Leak detection for production monitoring
 * - All ClickHouse-specific driver properties are now configured directly in the JDBC URL in application.yml
 */

@Configuration
public class DataSourceConfig {
	
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        
        // Basic Connection Settings
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.clickhouse.jdbc.ClickHouseDriver");

        // === PERFORMANCE CRITICAL SETTINGS ===
        
        // Pool Sizing - Tuned for trading workloads
        config.setMaximumPoolSize(50);           // Max concurrent connections
        config.setMinimumIdle(10);               // Always-ready connections
        
        // Timeout Settings - Optimized for low latency
        config.setConnectionTimeout(250);        // 250ms - CRITICAL for HFT
        config.setValidationTimeout(250);        // Fast connection validation
        config.setIdleTimeout(300_000);          // 5 minutes idle timeout
        config.setMaxLifetime(600_000);          // 10 minutes max connection life
        
        // Connection Validation
        config.setConnectionTestQuery("SELECT 1");
        
        // Leak Detection (important for production)
        config.setLeakDetectionThreshold(30_000); // 30 seconds
        
        // All ClickHouse-specific optimizations are now configured directly in the JDBC URL.
        // The following addDataSourceProperty calls are removed to avoid redundancy and warnings.
        
        // Connection pool naming for monitoring
        config.setPoolName("TradingHikariPool");
        
        return new HikariDataSource(config);
    }

}
