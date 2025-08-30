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
 * - ClickHouse-specific driver properties
 * - Leak detection for production monitoring
 * - TCP keep-alive for persistent connections
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
        
        // === CLICKHOUSE-SPECIFIC OPTIMIZATIONS ===
        
        // Socket-level optimizations
        config.addDataSourceProperty("socket_timeout", "300000");
        config.addDataSourceProperty("tcp_keep_alive", "true");
        config.addDataSourceProperty("tcp_no_delay", "true");
        
        // ClickHouse query optimizations
        config.addDataSourceProperty("max_execution_time", "60");
        config.addDataSourceProperty("max_threads", "4");
        config.addDataSourceProperty("compression", "true");
        
        // Buffer size optimizations for bulk operations
        config.addDataSourceProperty("max_insert_block_size", "1048576");
        config.addDataSourceProperty("min_insert_block_size_rows", "500");
        
        // Connection pool naming for monitoring
        config.setPoolName("TradingHikariPool");
        
        return new HikariDataSource(config);
    }

}
