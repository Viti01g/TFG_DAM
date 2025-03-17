package com.example.gestioncitasapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun AgendarCitaScreen(context: Context, modifier: Modifier = Modifier) {
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var medicoSeleccionado by remember { mutableStateOf("") }

    val listaMedicos = listOf("Dr. Pérez", "Dra. Gómez", "Dr. López") // Lista de médicos de ejemplo

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Agendar Cita", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Selección de Fecha
        Button(onClick = {
            val calendario = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    fecha = "$dayOfMonth/${month + 1}/$year"
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        }) {
            Text(text = if (fecha.isEmpty()) "Seleccionar Fecha" else "Fecha: $fecha")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Selección de Hora
        Button(onClick = {
            val calendario = Calendar.getInstance()
            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    hora = String.format("%02d:%02d", hourOfDay, minute)
                },
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE),
                true
            ).show()
        }) {
            Text(text = if (hora.isEmpty()) "Seleccionar Hora" else "Hora: $hora")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Selección de Médico
        DropdownMenuDemo(
            listaMedicos = listaMedicos,
            onMedicoSeleccionado = { medicoSeleccionado = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para confirmar la cita
        Button(
            onClick = { /* Guardar la cita en la base de datos */ },
            enabled = fecha.isNotEmpty() && hora.isNotEmpty() && medicoSeleccionado.isNotEmpty()
        ) {
            Text(text = "Confirmar Cita")
        }
    }
}

// Menú desplegable para seleccionar médico
@Composable
fun DropdownMenuDemo(listaMedicos: List<String>, onMedicoSeleccionado: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedMedico by remember { mutableStateOf("") }

    Column {
        Button(onClick = { expanded = true }) {
            Text(text = if (selectedMedico.isEmpty()) "Seleccionar Médico" else selectedMedico)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listaMedicos.forEach { medico ->
                DropdownMenuItem(
                    text = { Text(medico) },
                    onClick = {
                        selectedMedico = medico
                        onMedicoSeleccionado(medico)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgendarCitaScreenPreview() {
    AgendarCitaScreen(context = androidx.compose.ui.platform.LocalContext.current)
}
