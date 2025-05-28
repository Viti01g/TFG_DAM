package com.example.citas_medicas.controller;

import com.example.citas_medicas.exception.AgendaConflictException;
import com.example.citas_medicas.model.Cita;
import com.example.citas_medicas.model.HorarioDisponible;
import com.example.citas_medicas.model.Usuario;
import com.example.citas_medicas.service.CitaService;
import com.example.citas_medicas.service.HorarioDisponibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private HorarioDisponibleService horarioDisponibleService;

    // Obtiene todas las citas de un médico por su ID
    @GetMapping("/{medicoId}/citas")
    public ResponseEntity<List<Cita>> obtenerCitas(@PathVariable Integer medicoId) {
        List<Cita> citas = citaService.obtenerCitasPorMedico(medicoId);
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    // Crea un nuevo horario disponible para un médico
    @PostMapping("/{medicoId}/horarios")
    public ResponseEntity<?> crearHorario(@PathVariable Integer medicoId, @RequestBody Map<String, String> horarioData) {
        try {
            LocalDate fechaInicio = LocalDate.parse(horarioData.get("fechaInicio"));
            LocalTime horaInicio = LocalTime.parse(horarioData.get("horaInicio"));
            LocalTime horaFin = LocalTime.parse(horarioData.get("horaFin"));
            Integer duracionCita = Integer.parseInt(horarioData.get("duracionCita"));
            String especialidad = horarioData.get("especialidad");

            Optional<HorarioDisponible> nuevoHorario = horarioDisponibleService.crearHorarioDisponible(medicoId, fechaInicio, horaInicio, horaFin, duracionCita, especialidad);
            return nuevoHorario.map(horario -> new ResponseEntity<>(horario, HttpStatus.CREATED))
                    .orElseGet(() -> ResponseEntity.badRequest().build());

        } catch (Exception e) {
            return new ResponseEntity<>("Formato de datos inválido", HttpStatus.BAD_REQUEST);
        }
    }

    // Cancela una cita por su ID
    @DeleteMapping("/citas/{citaId}")
    public ResponseEntity<?> cancelarCita(@PathVariable Integer citaId) {
        Optional<Cita> citaCancelada = citaService.cancelarCita(citaId);
        return citaCancelada.map(cita -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Edita el horario de una cita
    @PutMapping("/citas/{citaId}")
    public ResponseEntity<?> editarHorarioCita(@PathVariable Integer citaId, @RequestBody Map<String, String> horarioData) {
        try {
            LocalDate nuevaFecha = LocalDate.parse(horarioData.get("nuevaFecha"));
            LocalTime nuevaHora = LocalTime.parse(horarioData.get("nuevaHora"));
            Optional<Cita> citaActualizadaOptional = citaService.editarHorarioCita(citaId, nuevaFecha.atTime(nuevaHora));
            if (citaActualizadaOptional.isPresent()) {
                return new ResponseEntity<>(citaActualizadaOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se pudo editar la cita. Verifica la fecha y hora.", HttpStatus.BAD_REQUEST);
            }
        } catch (AgendaConflictException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Formato de fecha/hora inválido", HttpStatus.BAD_REQUEST);
        }
    }

    // Maneja excepciones de conflictos en la agenda
    @ExceptionHandler(AgendaConflictException.class)
    public ResponseEntity<String> handleAgendaConflict(AgendaConflictException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    // Obtiene la lista de pacientes de un médico
    @GetMapping("/{medicoId}/pacientes")
    public ResponseEntity<List<Usuario>> obtenerPacientes(@PathVariable Integer medicoId) {
        List<Usuario> pacientes = citaService.obtenerPacientesPorMedico(medicoId);
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }
}