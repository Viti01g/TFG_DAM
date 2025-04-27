package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pacientes")
public class Paciente extends Usuario {

    @Column(nullable = false)
    private String telefono;

    public Paciente() {}

    public Paciente(String nombre, String email, String password, String telefono) {
        super(nombre, email, password);
        this.telefono = telefono;
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
