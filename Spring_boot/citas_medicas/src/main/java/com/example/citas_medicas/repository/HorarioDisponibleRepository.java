package com.example.citas_medicas.repository;

import com.example.citas_medicas.model.HorarioDisponible;
import com.example.citas_medicas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

// Marca esta interfaz como un repositorio de Spring
@Repository
public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Integer> {

    // Busca horarios disponibles de un médico en una fecha específica
    List<HorarioDisponible> findByMedicoAndFechaInicio(Usuario medico, LocalDate fechaInicio);

    // Busca todos los horarios disponibles de un médico
    List<HorarioDisponible> findByMedico(Usuario medico);

    // Podríamos añadir más métodos de búsqueda según las necesidades (ej: por especialidad, rango de fechas, etc.)
}