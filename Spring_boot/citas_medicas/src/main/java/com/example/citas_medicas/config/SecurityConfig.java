package com.example.citas_medicas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita @PreAuthorize en tus controladores
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF
            .authorizeHttpRequests(authorize -> authorize
                // --- REGLAS PÚBLICAS ---
                .requestMatchers("/api/auth/registrar").permitAll() // Registro público
                .requestMatchers("/api/auth/login").permitAll()

                // --- REGLAS PARA ADMIN ---
                .requestMatchers(HttpMethod.POST, "/api/admin/users/crear").hasRole("ADMIN_SUPREMO")
                .requestMatchers(HttpMethod.GET, "/api/admin/users", "/api/admin/users/**").hasAnyRole("ADMIN", "ADMIN_SUPREMO")
                .requestMatchers(HttpMethod.PUT, "/api/admin/users/**").hasAnyRole("ADMIN", "ADMIN_SUPREMO")
                .requestMatchers(HttpMethod.DELETE, "/api/admin/users/**").hasRole("ADMIN_SUPREMO")

                // --- REGLAS PARA MÉDICOS ---
                .requestMatchers(HttpMethod.POST, "/api/medicos/**/horarios").hasRole("MEDICO")
                .requestMatchers(HttpMethod.GET, "/api/medicos/**/citas").hasRole("MEDICO")
                .requestMatchers(HttpMethod.GET, "/api/medicos/**/pacientes").hasRole("MEDICO")
                .requestMatchers(HttpMethod.DELETE, "/api/medicos/citas/**").hasRole("MEDICO")
                .requestMatchers(HttpMethod.PUT, "/api/medicos/citas/**").hasRole("MEDICO")

                // --- REGLAS PARA PACIENTES ---
                .requestMatchers(HttpMethod.GET, "/api/pacientes/{pacienteId}/citas").hasRole("PACIENTE")
                .requestMatchers(HttpMethod.GET, "/api/pacientes/medicos/**/horarios").hasRole("PACIENTE")
                .requestMatchers(HttpMethod.POST, "/api/pacientes/**/citas").hasRole("PACIENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/pacientes/citas/**").hasRole("PACIENTE")
                .requestMatchers(HttpMethod.PUT, "/api/pacientes/citas/**").hasRole("PACIENTE")

                // --- REGLA GENERAL ---
                .anyRequest().authenticated() // Todas las demás peticiones requieren autenticación
            )
            .httpBasic(withDefaults()); // Configuración básica de autenticación

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCrypt para encriptar contraseñas
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}