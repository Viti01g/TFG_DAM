package com.example.citas_medicas.exception;

// Define una excepción personalizada para conflictos en la agenda
public class AgendaConflictException extends RuntimeException {

    // Constructor que permite especificar un mensaje de error al lanzar la excepción
    public AgendaConflictException(String message) {
        super(message); // Llama al constructor de la clase base RuntimeException con el mensaje proporcionado
    }
}