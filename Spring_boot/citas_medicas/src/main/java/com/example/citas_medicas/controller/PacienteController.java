package com.example.citas_medicas.controller;

import com.example.citas_medicas.exception.AgendaConflictException;
import com.example.citas_medicas.model.Cita;
import com.example.citas_medicas.model.HorarioDisponible;
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
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private HorarioDisponibleService horarioDisponibleService;

    // Obtiene todas las citas de un paciente por su ID
    @GetMapping("/{pacienteId}/citas")
    public ResponseEntity<List<Cita>> obtenerCitas(@PathVariable Integer pacienteId) {
        List<Cita> citas = citaService.obtenerCitasPorPaciente(pacienteId);
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    // Obtiene los horarios disponibles de un médico por su ID
    @GetMapping("/medicos/{medicoId}/horarios")
    public ResponseEntity<List<HorarioDisponible>> obtenerHorariosMedico(@PathVariable Integer medicoId) {
        List<HorarioDisponible> horarios = horarioDisponibleService.obtenerHorariosDisponiblesPorMedico(medicoId);
        return new ResponseEntity<>(horarios, HttpStatus.OK);
    }

    // Reserva una cita para un paciente
    @PostMapping("/{pacienteId}/citas")
    public ResponseEntity<?> reservarCita(@PathVariable Integer pacienteId, @RequestBody Map<String, Object> citaData) {
        Integer horarioDisponibleId = (Integer) citaData.get("horarioDisponibleId");
        String motivo = (String) citaData.get("motivo");

        if (horarioDisponibleId == null) {
            return new ResponseEntity<>("Se requiere el ID del horario disponible", HttpStatus.BAD_REQUEST);
        }

        Optional<Cita> nuevaCitaOptional = citaService.reservarCita(pacienteId, horarioDisponibleId, motivo);

        if (nuevaCitaOptional.isPresent()) {
            return new ResponseEntity<>(nuevaCitaOptional.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No se pudo reservar la cita. Verifica el ID del horario.", HttpStatus.BAD_REQUEST);
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
}