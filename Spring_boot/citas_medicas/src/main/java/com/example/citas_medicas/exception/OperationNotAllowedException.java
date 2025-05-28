package com.example.citas_medicas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Marca esta clase como una excepción personalizada que se lanzará cuando una operación no esté permitida
@ResponseStatus(HttpStatus.FORBIDDEN) // Asocia esta excepción con el código de estado HTTP 403 (Forbidden)
public class OperationNotAllowedException extends RuntimeException {

    // Constructor que permite especificar un mensaje de error al lanzar la excepción
    public OperationNotAllowedException(String message) {
        super(message); // Llama al constructor de la clase base RuntimeException con el mensaje proporcionado
    }
}