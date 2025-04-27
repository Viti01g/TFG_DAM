package com.example.demo.controller;

import com.example.demo.model.Cita;
import com.example.demo.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/citas")
public class CitaController {
    @Autowired
    private CitaService citaService;

    @GetMapping
    public List<Cita> listarCitas() {
        return citaService.listarCitas();
    }

    @GetMapping("/{id}")
    public Optional<Cita> obtenerCita(@PathVariable Long id) {
        return citaService.obtenerCitaPorId(id);
    }

    @PostMapping
    public Cita guardarCita(@RequestBody Cita cita) {
        return citaService.guardarCita(cita);
    }

    @DeleteMapping("/{id}")
    public void eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
    }
}

