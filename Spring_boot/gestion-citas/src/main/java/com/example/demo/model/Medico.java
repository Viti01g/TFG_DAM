package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "medicos")
public class Medico extends Usuario {

    @Column(nullable = false)
    private String especialidad;

    public Medico() {}

    public Medico(String nombre, String email, String password, String especialidad) {
        super(nombre, email, password);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
