package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "citas") // Asegúrate de que coincida con el nombre de la tabla en la BD
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String fecha;

    // Constructor vacío requerido por JPA
    public Cita() {
    }

    // Constructor con parámetros
    public Cita(String nombre, String fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    // toString() para depuración
    @Override
    public String toString() {
        return "Cita{id=" + id + ", nombre='" + nombre + "', fecha='" + fecha + "'}";
    }
}

