package br.com.healthgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitorBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorBackendApplication.class, args);
    }
} 