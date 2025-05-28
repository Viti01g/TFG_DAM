package com.example.citas_medicas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

// Marca esta clase como una entidad JPA que se mapeará a una tabla en la base de datos
@Entity
@Table(name = "citas")
@Data // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor // Genera un constructor vacío
@AllArgsConstructor // Genera un constructor con todos los atributos
public class Cita {

    // Define el campo 'id' como clave primaria con generación automática
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación muchos-a-uno con la entidad Usuario (médico)
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false) // Define la columna que almacena la relación
    private Usuario medico;

    // Relación muchos-a-uno con la entidad Usuario (paciente)
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false) // Define la columna que almacena la relación
    private Usuario paciente;

    // Campo para la fecha y hora de la cita
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    // Campo para el motivo de la cita (opcional)
    private String motivo;

    // Estado de la cita, almacenado como texto en la base de datos
    @Enumerated(EnumType.STRING)
    private EstadoCita estado = EstadoCita.programada; // Valor predeterminado: 'programada'

    // Enumeración para representar los posibles estados de una cita
    public enum EstadoCita {
        programada,
        cancelada
        // Podríamos añadir 'finalizada' si es necesario en el futuro
    }
}