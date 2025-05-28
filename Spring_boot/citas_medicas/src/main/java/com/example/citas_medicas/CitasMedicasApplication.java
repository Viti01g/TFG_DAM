package com.example.citas_medicas;

// Importa las clases necesarias para iniciar una aplicación Spring Boot
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Anotación que marca esta clase como la principal para iniciar la aplicación Spring Boot
@SpringBootApplication
// Habilita el uso de repositorios JPA en el paquete especificado
@EnableJpaRepositories("com.example.citas_medicas.repository")
public class CitasMedicasApplication {

    // Método principal que inicia la aplicación
    public static void main(String[] args) {
        // Ejecuta la aplicación Spring Boot utilizando la configuración definida
        SpringApplication.run(CitasMedicasApplication.class, args);
    }

}
