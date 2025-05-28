package com.example.citas_medicas.service;

import com.example.citas_medicas.model.HorarioDisponible;
import com.example.citas_medicas.model.Usuario;
import com.example.citas_medicas.repository.HorarioDisponibleRepository;
import com.example.citas_medicas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

// Marca esta clase como un servicio de Spring
@Service
public class HorarioDisponibleService {

    // Inyección de dependencias para interactuar con la base de datos
    @Autowired
    private HorarioDisponibleRepository horarioDisponibleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para crear un horario disponible para un médico
    public Optional<HorarioDisponible> crearHorarioDisponible(Integer medicoId, LocalDate fechaInicio, LocalTime horaInicio, LocalTime horaFin, Integer duracionCita, String especialidad) {
        // Busca al médico en la base de datos por su ID
        Optional<Usuario> medicoOptional = usuarioRepository.findById(medicoId);
        if (medicoOptional.isEmpty()) {
            return Optional.empty(); // Retorna vacío si el médico no existe
        }

        // Validaciones de los datos proporcionados
        if (fechaInicio == null || horaInicio == null || horaFin == null || duracionCita == null || duracionCita <= 0) {
            return Optional.empty(); // Retorna vacío si los datos son inválidos
        }
        if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
            return Optional.empty(); // La hora de fin debe ser posterior a la hora de inicio
        }

        // Crea un nuevo horario disponible y lo guarda en la base de datos
        Usuario medico = medicoOptional.get();
        HorarioDisponible nuevoHorario = new HorarioDisponible();
        nuevoHorario.setMedico(medico);
        nuevoHorario.setFechaInicio(fechaInicio);
        nuevoHorario.setHoraInicio(horaInicio);
        nuevoHorario.setHoraFin(horaFin);
        nuevoHorario.setDuracionCita(duracionCita);
        nuevoHorario.setEspecialidad(especialidad);
        return Optional.of(horarioDisponibleRepository.save(nuevoHorario));
    }

    // Método para obtener horarios disponibles de un médico en una fecha específica
    public List<HorarioDisponible> obtenerHorariosDisponiblesPorMedicoYFecha(Integer medicoId, LocalDate fecha) {
        // Busca al médico en la base de datos por su ID
        Optional<Usuario> medicoOptional = usuarioRepository.findById(medicoId);
        // Si el médico existe, busca los horarios disponibles para la fecha proporcionada
        return medicoOptional.map(medico -> horarioDisponibleRepository.findByMedicoAndFechaInicio(medico, fecha)).orElse(java.util.List.of());
    }

    // Método para obtener todos los horarios disponibles de un médico
    public List<HorarioDisponible> obtenerHorariosDisponiblesPorMedico(Integer medicoId) {
        // Busca al médico en la base de datos por su ID
        Optional<Usuario> medicoOptional = usuarioRepository.findById(medicoId);
        // Si el médico existe, busca todos sus horarios disponibles
        return medicoOptional.map(horarioDisponibleRepository::findByMedico).orElse(java.util.List.of());
    }

    // Método para obtener un horario disponible por su ID
    public Optional<HorarioDisponible> obtenerHorarioDisponiblePorId(Integer horarioId) {
        return horarioDisponibleRepository.findById(horarioId);
    }

    // Método para eliminar un horario disponible por su ID
    public void eliminarHorarioDisponible(Integer horarioId) {
        horarioDisponibleRepository.deleteById(horarioId);
    }

    // Podríamos añadir métodos para manejar horarios recurrentes aquí
}