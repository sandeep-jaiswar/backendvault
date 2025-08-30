package com.sevenblue.backendvault;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Trading Platform Application - High-Performance Spring Boot App
 * 
 * Features Enabled:
 * - Async processing for non-blocking operations
 * - Transaction management for data consistency
 * - Auto-configuration for rapid development
 * - Production-ready with Actuator endpoints
 */
@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class BackendvaultApplication {

	public static void main(String[] args) {
		// JVM optimizations for trading workloads
        System.setProperty("spring.jpa.properties.hibernate.jdbc.batch_size", "500");
        System.setProperty("spring.jpa.properties.hibernate.order_inserts", "true");
        System.setProperty("spring.jpa.properties.hibernate.order_updates", "true");
        
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
