package com.example.citas_medicas.repository;

import com.example.citas_medicas.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// Marca esta interfaz como un repositorio de Spring
@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    // Busca todas las citas de un médico, ordenadas por fecha y hora ascendente
    List<Cita> findByMedicoIdOrderByFechaHoraAsc(Integer medicoId);

    // Busca todas las citas de un paciente, ordenadas por fecha y hora ascendente
    List<Cita> findByPacienteIdOrderByFechaHoraAsc(Integer pacienteId);

    // Busca citas de un médico en una fecha y hora específica
    List<Cita> findByMedicoIdAndFechaHora(Integer medicoId, LocalDateTime fechaHora);

    // Busca todas las citas de un médico
    List<Cita> findByMedicoId(Integer medicoId);
}