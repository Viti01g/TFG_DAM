package com.example.citas_medicas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Marca esta clase como una excepción personalizada que se lanzará cuando ocurra un conflicto de datos
@ResponseStatus(HttpStatus.CONFLICT) // Asocia esta excepción con el código de estado HTTP 409 (Conflict)
public class DataConflictException extends RuntimeException {

    // Constructor que permite especificar un mensaje de error al lanzar la excepción
    public DataConflictException(String message) {
        super(message); // Llama al constructor de la clase base RuntimeException con el mensaje proporcionado
    }
}