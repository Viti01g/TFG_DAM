package com.example.citas_medicas.service;

import com.example.citas_medicas.exception.AgendaConflictException;
import com.example.citas_medicas.model.Cita;
import com.example.citas_medicas.model.HorarioDisponible;
import com.example.citas_medicas.model.Usuario;
import com.example.citas_medicas.repository.CitaRepository;
import com.example.citas_medicas.repository.HorarioDisponibleRepository;
import com.example.citas_medicas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private HorarioDisponibleRepository horarioDisponibleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Cita> obtenerCitasPorMedico(Integer medicoId) {
        return citaRepository.findByMedicoIdOrderByFechaHoraAsc(medicoId);
    }

    public List<Cita> obtenerCitasPorPaciente(Integer pacienteId) {
        return citaRepository.findByPacienteIdOrderByFechaHoraAsc(pacienteId);
    }

    public Optional<Cita> reservarCita(Integer pacienteId, Integer horarioDisponibleId, String motivo) {
        Optional<Usuario> pacienteOptional = usuarioRepository.findById(pacienteId);
        Optional<HorarioDisponible> horarioOptional = horarioDisponibleRepository.findById(horarioDisponibleId);

        if (pacienteOptional.isEmpty()) {
            return Optional.empty(); // Paciente no encontrado
        }
        if (horarioOptional.isEmpty()) {
            return Optional.empty(); // Horario disponible no encontrado
        }

        Usuario paciente = pacienteOptional.get();
        HorarioDisponible horario = horarioOptional.get();

        // Verificación adicional para asegurar que el horario todavía existe
        Optional<HorarioDisponible> horarioExistente = horarioDisponibleRepository.findById(horarioDisponibleId);
        if (horarioExistente.isEmpty()) {
            return Optional.empty(); // El horario ya no existe (posiblemente ya reservado)
        }

        // Crear la nueva cita
        Cita nuevaCita = new Cita();
        nuevaCita.setMedico(horario.getMedico());
        nuevaCita.setPaciente(paciente);
        nuevaCita.setFechaHora(LocalDateTime.of(horario.getFechaInicio(), horario.getHoraInicio()));
        nuevaCita.setMotivo(motivo);

        // Guardar la cita
        Cita citaGuardada = citaRepository.save(nuevaCita);

        // Eliminar el horario disponible después de la reserva
        horarioDisponibleRepository.delete(horario);

        return Optional.of(citaGuardada);
    }

    public Optional<Cita> cancelarCita(Integer citaId) {
        Optional<Cita> citaOptional = citaRepository.findById(citaId);
        if (citaOptional.isPresent()) {
            Cita cita = citaOptional.get();
            cita.setEstado(Cita.EstadoCita.cancelada); // Cambia el estado de la cita a "cancelada"
            return Optional.of(citaRepository.save(cita));
            // Aquí podríamos añadir lógica para notificar al médico/paciente
        }
        return Optional.empty(); // Cita no encontrada
    }

    public Optional<Cita> editarHorarioCita(Integer citaId, LocalDateTime nuevaFechaHora) {
        Optional<Cita> citaOptional = citaRepository.findById(citaId);
        if (citaOptional.isPresent()) {
            Cita citaAEditar = citaOptional.get();
            Integer medicoId = citaAEditar.getMedico().getId();

            // Validación 1: Asegurarse de que la nueva fecha/hora no esté en el pasado
            if (nuevaFechaHora.isBefore(LocalDateTime.now())) {
                return Optional.empty(); // La nueva fecha/hora está en el pasado
            }

            // Validación 2: Verificar si el médico tiene otra cita programada para la nueva fecha/hora
            List<Cita> citasDelMedicoEnNuevaHora = citaRepository.findByMedicoIdAndFechaHora(medicoId, nuevaFechaHora);
            // Excluimos la cita que estamos editando de la verificación de conflictos
            boolean existeConflicto = citasDelMedicoEnNuevaHora.stream()
                    .anyMatch(cita -> !cita.getId().equals(citaId));

            if (existeConflicto) {
                throw new AgendaConflictException("El médico ya tiene una cita programada para esa fecha y hora.");
            }

            citaAEditar.setFechaHora(nuevaFechaHora);
            return Optional.of(citaRepository.save(citaAEditar));
        }
        return Optional.empty(); // Cita no encontrada
    }

    public List<Usuario> obtenerPacientesPorMedico(Integer medicoId) {
        List<Cita> citasDelMedico = citaRepository.findByMedicoId(medicoId);
        // Extraemos los pacientes de las citas y eliminamos duplicados usando un Set
        Set<Usuario> pacientesUnicos = citasDelMedico.stream()
                .map(Cita::getPaciente)
                .collect(Collectors.toSet());
        return new java.util.ArrayList<>(pacientesUnicos);
    }
}