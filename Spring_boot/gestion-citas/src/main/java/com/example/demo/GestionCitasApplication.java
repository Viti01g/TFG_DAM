/*
 * package com.example.demo;
 * 
 * import org.springframework.boot.SpringApplication; import
 * org.springframework.boot.autoconfigure.SpringBootApplication;
 * 
 * @SpringBootApplication public class GestionCitasApplication {
 * 
 * public static void main(String[] args) {
 * SpringApplication.run(GestionCitasApplication.class, args); }
 * 
 * }
 */

package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@EntityScan(basePackages = "com.example.demo.model")
public class GestionCitasApplication {
    public static void main(String[] args) {
        SpringApplication.run(GestionCitasApplication.class, args);
    }
}

