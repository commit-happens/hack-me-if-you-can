package com.hackme.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Hack Me If You Can backend.
 * 
 * This is the entry point of our Spring Boot application. When you run this class,
 * it starts the entire web application with all its components.
 * 
 * KEY CONCEPTS FOR BEGINNERS:
 * 
 * 1. @SpringBootApplication annotation does three important things:
 *    - @Configuration: Tells Spring this class can define beans (objects managed by Spring)
 *    - @EnableAutoConfiguration: Automatically configures Spring based on dependencies in classpath
 *    - @ComponentScan: Tells Spring to scan for components (Controllers, Services, etc.) in this package and subpackages
 * 
 * 2. main() method: Standard Java entry point - this is where your program starts executing
 * 
 * 3. SpringApplication.run(): Starts the Spring Boot application
 *    - Creates the application context (container for all Spring components)
 *    - Starts the embedded web server (Tomcat by default)
 *    - Sets up all auto-configurations
 * 
 * WHAT HAPPENS WHEN YOU RUN THIS:
 * 1. Spring Boot scans all packages under com.hackme.backend
 * 2. It finds and registers all @Controller, @Service, @Repository classes
 * 3. It starts an embedded Tomcat server on port 8080
 * 4. It sets up database connections, security, etc. automatically
 * 5. Your REST API becomes available at http://localhost:8080
 * 
 * @author Hack Me If You Can Team
 * @version 1.0
 * @since Java 21
 */
@SpringBootApplication // This single annotation replaces @Configuration + @EnableAutoConfiguration + @ComponentScan
public class HackMeBackendApplication {

    /**
     * Main method - the entry point of our Java application.
     * 
     * This method is called when you run the application. It uses SpringApplication.run()
     * to launch the Spring Boot application with all its features.
     * 
     * @param args Command line arguments (optional parameters passed when starting the app)
     */
    public static void main(String[] args) {
        // Start the Spring Boot application
        // This single line does a lot of work behind the scenes!
        SpringApplication.run(HackMeBackendApplication.class, args);
    }
}