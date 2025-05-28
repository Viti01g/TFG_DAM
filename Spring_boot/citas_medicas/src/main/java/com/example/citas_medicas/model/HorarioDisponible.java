package com.example.citas_medicas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

// Marca esta clase como una entidad JPA que se mapeará a una tabla en la base de datos
@Entity
@Table(name = "horarios_disponibles")
@Data // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor // Genera un constructor vacío
@AllArgsConstructor // Genera un constructor con todos los atributos
public class HorarioDisponible {

    // Define el campo 'id' como clave primaria con generación automática
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación muchos-a-uno con la entidad Usuario (médico)
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false) // Define la columna que almacena la relación
    private Usuario medico;

    // Campo para la fecha de inicio del horario
    @Column(nullable = false)
    private LocalDate fechaInicio;

    // Campo para la hora de inicio del horario
    @Column(nullable = false)
    private LocalTime horaInicio;

    // Campo para la hora de fin del horario
    @Column(nullable = false)
    private LocalTime horaFin;

    // Duración de cada cita en minutos
    @Column(nullable = false)
    private Integer duracionCita;

    // Especialidad asociada al horario (puede ser texto o una referencia a otra tabla)
    private String especialidad;
}
