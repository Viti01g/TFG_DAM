package com.example.citasfinalmedicas.model

import com.google.gson.annotations.SerializedName

// Clase de datos que representa un horario disponible para citas médicas.
// Contiene información sobre el médico, fecha, horas disponibles, duración de las citas y especialidad.
data class HorarioDisponible(
    @SerializedName("id") val id: Int?, // ID único del horario, puede ser nulo si aún no se ha creado en el backend.
    @SerializedName("medico") val medico: Usuario, // Información del médico asociado al horario.
    @SerializedName("fechaInicio") val fechaInicio: String, // Fecha de inicio del horario en formato String (YYYY-MM-DD).
    @SerializedName("horaInicio") val horaInicio: String, // Hora de inicio del horario en formato String (HH:MM:SS).
    @SerializedName("horaFin") val horaFin: String, // Hora de fin del horario en formato String (HH:MM:SS).
    @SerializedName("duracionCita") val duracionCita: Int, // Duración de cada cita en minutos.
    @SerializedName("especialidad") val especialidad: String? // Especialidad del médico, puede ser nulo si no se especifica.
)