package com.healthlinkteam.healthlink.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA Configuration class.
 * Enables JPA auditing and repositories for the application.
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.healthlinkteam.healthlink.repository")
public class JpaConfig {
    // JPA configuration is handled by Spring Boot auto-configuration
    // This class enables additional JPA features like auditing
} 