package com.healthlinkteam.healthlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EntityScan("com.healthlinkteam.healthlink.entity")
@EnableJpaRepositories("com.healthlinkteam.healthlink.repository")
@SpringBootApplication
public class HealthlinkApplication {
    public static void main(String[] args) {
        System.out.println("Starting HealthLink Backend Application...");
        SpringApplication.run(HealthlinkApplication.class, args);
        System.out.println("HealthLink Backend Application started successfully!");
    }
}
