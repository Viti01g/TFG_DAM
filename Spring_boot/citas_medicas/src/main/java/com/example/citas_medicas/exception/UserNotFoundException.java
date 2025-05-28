package com.example.citas_medicas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Marca esta clase como una excepción personalizada que se lanzará cuando un usuario no sea encontrado
@ResponseStatus(HttpStatus.NOT_FOUND) // Asocia esta excepción con el código de estado HTTP 404 (Not Found)
public class UserNotFoundException extends RuntimeException {

    // Constructor que permite especificar un mensaje de error al lanzar la excepción
    public UserNotFoundException(String message) {
        super(message); // Llama al constructor de la clase base RuntimeException con el mensaje proporcionado
    }
}