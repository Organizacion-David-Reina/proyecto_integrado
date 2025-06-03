package com.proyectointegrado.reina_cabrera_david;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Reina Cabrera David Spring Boot application.
 *
 * <p>This class is annotated with {@link SpringBootApplication}, which marks it as a configuration class
 * and triggers auto-configuration, component scanning, and allows you to define extra configuration
 * on your application class.
 *
 * <p>The {@code main} method delegates to {@link SpringApplication#run(Class, String[])} to launch
 * the application.
 *
 * @author David Reina Cabrera 2ºDAM 2024/2025
 */
@SpringBootApplication
public class ReinaCabreraDavidApplication {

    /**
     * Main method that serves as the entry point of the Spring Boot application.
     *
     * @param args an array of command-line arguments passed to the application (can be empty)
     */
    public static void main(String[] args) {
        SpringApplication.run(ReinaCabreraDavidApplication.class, args);
    }

}

