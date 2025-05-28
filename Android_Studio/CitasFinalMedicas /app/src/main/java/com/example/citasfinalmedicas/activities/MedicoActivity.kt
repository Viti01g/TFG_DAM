package com.example.citasfinalmedicas.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View // Para controlar la visibilidad de elementos
import android.widget.ArrayAdapter // Para el spinner de especialización
import android.widget.AutoCompleteTextView // Para el campo de especialización
import android.widget.Button
import android.widget.TextView
import android.widget.Toast // Para mostrar mensajes temporales
import android.util.Log // Para depuración

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView // Para el formulario de citas
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.citasfinalmedicas.R
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText // Para los campos de texto

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Esta clase representa la actividad principal para los médicos.
// Permite gestionar horarios de citas y navegar por el menú lateral.
class MedicoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Variables para el menú lateral y elementos de la interfaz.
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var welcomeText: TextView

    // Variables para los elementos del formulario de citas.
    private lateinit var openAppointmentButton: Button
    private lateinit var newAppointmentCard: CardView
    private lateinit var editTextDate: TextInputEditText
    private lateinit var editTextStartTime: TextInputEditText
    private lateinit var editTextEndTime: TextInputEditText
    private lateinit var autoCompleteSpecialization: AutoCompleteTextView
    private lateinit var saveAppointmentButton: Button

    private val calendar = Calendar.getInstance() // Para los selectores de fecha y hora.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medico) // Establece el diseño de la actividad.

        // Inicialización del menú lateral y la barra de herramientas.
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        welcomeText = findViewById(R.id.welcomeText)
        welcomeText.text = "¡Bienvenido, Médico!" // Mensaje de bienvenida.

        // Inicialización de los elementos del formulario de citas.
        openAppointmentButton = findViewById(R.id.openAppointmentButton)
        newAppointmentCard = findViewById(R.id.newAppointmentCard)
        editTextDate = findViewById(R.id.editTextDate)
        editTextStartTime = findViewById(R.id.editTextStartTime)
        editTextEndTime = findViewById(R.id.editTextEndTime)
        autoCompleteSpecialization = findViewById(R.id.autoCompleteSpecialization)
        saveAppointmentButton = findViewById(R.id.saveAppointmentButton)

        // Configuración del botón para abrir/cerrar el formulario de citas.
        openAppointmentButton.setOnClickListener {
            if (newAppointmentCard.visibility == View.GONE) {
                newAppointmentCard.visibility = View.VISIBLE
                openAppointmentButton.text = "Cerrar Horario de Cita"
            } else {
                newAppointmentCard.visibility = View.GONE
                openAppointmentButton.text = "Abrir Horario de Cita"
            }
        }

        // Configuración del selector de fecha.
        editTextDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    editTextDate.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Configuración del selector de hora de inicio.
        editTextStartTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    editTextStartTime.setText(timeFormat.format(calendar.time))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // Formato 24 horas.
            )
            timePickerDialog.show()
        }

        // Configuración del selector de hora de fin.
        editTextEndTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    editTextEndTime.setText(timeFormat.format(calendar.time))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // Formato 24 horas.
            )
            timePickerDialog.show()
        }

        // Configuración del campo de especialización con opciones predefinidas.
        val specializations = arrayOf("Cardiología", "Pediatría", "Dermatología", "Medicina General", "Neurología", "Oftalmología", "Otorrinolaringología", "Psicología", "Urología")
        val specializationAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, specializations)
        autoCompleteSpecialization.setAdapter(specializationAdapter)

        // Configuración del botón para guardar el horario de citas.
        saveAppointmentButton.setOnClickListener {
            val date = editTextDate.text.toString()
            val startTime = editTextStartTime.text.toString()
            val endTime = editTextEndTime.text.toString()
            val specialization = autoCompleteSpecialization.text.toString()

            if (date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || specialization.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                val message = "Horario a guardar:\nFecha: $date\nHora Inicio: $startTime\nHora Fin: $endTime\nEspecialización: $specialization"
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                Log.d("MedicoActivity", message)
            }
        }
    }

    // Manejo del botón "Atrás" para cerrar el menú lateral si está abierto.
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Manejo de las opciones seleccionadas en el menú de navegación.
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                performLogout() // Llama al método para cerrar sesión.
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Método para cerrar sesión del usuario.
    private fun performLogout() {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}