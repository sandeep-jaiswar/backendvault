package com.sevenblue.backendvault;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.JpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
// Removed @EnableTransactionManagement as JPA is no longer used for transactions

/**
 * Trading Platform Application - High-Performance Spring Boot App
 * 
 * Features Enabled:
 * - Async processing for non-blocking operations
 * - Auto-configuration for rapid development (excluding JPA)
 * - Production-ready with Actuator endpoints
 */
@SpringBootApplication(exclude = {JpaAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableAsync
// @EnableTransactionManagement removed
public class BackendvaultApplication {

	public static void main(String[] args) {
		// Removed JVM optimizations for trading workloads related to JPA
        
        SpringApplication app = new SpringApplication(BackendvaultApplication.class);
        
        // Additional startup optimizations
        app.setDefaultProperties(Map.of(
            "server.tomcat.threads.max", "200",
            "server.tomcat.threads.min-spare", "50",
            "server.tomcat.accept-count", "1000"
        ));
        
        app.run(args);
	}

}
