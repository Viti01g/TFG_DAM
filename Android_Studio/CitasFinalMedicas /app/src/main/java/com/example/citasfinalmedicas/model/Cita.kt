package com.example.citasfinalmedicas.model

import com.google.gson.annotations.SerializedName

// Clase de datos que representa una cita médica.
// Contiene información sobre el médico, paciente, fecha, motivo y estado de la cita.
data class Cita(
    @SerializedName("id") val id: Int?, // ID único de la cita, puede ser nulo si aún no se ha creado en el backend.
    @SerializedName("medico") val medico: Usuario, // Información del médico asociado a la cita.
    @SerializedName("paciente") val paciente: Usuario, // Información del paciente asociado a la cita.
    @SerializedName("fechaHora") val fechaHora: String, // Fecha y hora de la cita en formato String (ej. "2025-05-28T14:00:00").
    @SerializedName("motivo") val motivo: String?, // Motivo de la cita, puede ser nulo si no se especifica.
    @SerializedName("estado") val estado: EstadoCita // Estado actual de la cita (ej. "PENDIENTE", "CONFIRMADA", "CANCELADA").
)