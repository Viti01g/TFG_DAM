package com.example.citasfinalmedicas.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem // Importar MenuItem para manejar las opciones del menú
import android.widget.TextView // Importar TextView para mostrar mensajes en la interfaz
import android.util.Log // Importar Log para depuración

import androidx.appcompat.app.ActionBarDrawerToggle // Para el botón de hamburguesa en el menú lateral
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar // Para la barra de herramientas
import androidx.core.view.GravityCompat // Para manejar la apertura/cierre del menú lateral
import androidx.drawerlayout.widget.DrawerLayout // Para el diseño del menú lateral
import com.example.citasfinalmedicas.R // Importar recursos como layouts y strings
import com.google.android.material.navigation.NavigationView // Para manejar el menú lateral

// Esta clase representa la actividad principal para los pacientes.
// Permite navegar por el menú lateral y realizar acciones como cerrar sesión.
class PacienteActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Declaración de variables para el menú lateral y elementos de la interfaz.
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var welcomeText: TextView // Mensaje de bienvenida para el paciente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente) // Establece el diseño de la actividad.

        // Inicialización del menú lateral y la barra de herramientas.
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // Configura la barra de herramientas como ActionBar.

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this) // Configura el listener para manejar las opciones del menú.

        // Configuración del botón de hamburguesa para abrir/cerrar el menú lateral.
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar, // Vincula el botón de hamburguesa con la barra de herramientas.
            R.string.navigation_drawer_open, // Texto para abrir el menú.
            R.string.navigation_drawer_close // Texto para cerrar el menú.
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState() // Sincroniza el estado del botón de hamburguesa.

        // Configuración del mensaje de bienvenida.
        welcomeText = findViewById(R.id.welcomeText)
        welcomeText.text = "¡Bienvenido, Paciente!" // Mensaje personalizado para el paciente.

        // Opcional: Puedes cargar y mostrar información adicional del paciente en el menú.
        // Por ejemplo, su correo electrónico o nombre desde SharedPreferences o una respuesta del servidor.
    }

    // Manejo del botón "Atrás" para cerrar el menú lateral si está abierto.
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START) // Cierra el menú lateral.
        } else {
            super.onBackPressed() // Comportamiento normal del botón "Atrás".
        }
    }

    // Manejo de las opciones seleccionadas en el menú lateral.
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                Log.d("PacienteActivity", "Botón Cerrar Sesión presionado.") // Log para depuración.
                performLogout() // Llama al método para cerrar sesión.
            }
            // Puedes añadir lógica para otros ítems del menú aquí.
            // Ejemplo:
            // R.id.nav_agendar_cita -> {
            //     Toast.makeText(this, "Agendar Cita", Toast.LENGTH_SHORT).show()
            // }
            // R.id.nav_ver_citas -> {
            //     Toast.makeText(this, "Ver Mis Citas", Toast.LENGTH_SHORT).show()
            // }
        }
        drawerLayout.closeDrawer(GravityCompat.START) // Cierra el menú lateral después de seleccionar una opción.
        return true // Indica que la opción ha sido manejada.
    }

    // Método para cerrar sesión del usuario.
    private fun performLogout() {
        Log.d("PacienteActivity", "Iniciando performLogout().")

        // Limpia los datos de sesión almacenados en SharedPreferences.
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear() // Borra todos los datos almacenados.
            apply() // Aplica los cambios.
        }
        Log.d("PacienteActivity", "SharedPreferences limpiadas.")

        // Redirige al usuario a la actividad de inicio de sesión.
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Limpia la pila de actividades.
        startActivity(intent)
        Log.d("PacienteActivity", "Redirigiendo a LoginActivity.")

        // Finaliza la actividad actual.
        finish()
        Log.d("PacienteActivity", "PacienteActivity finalizada.")
    }
}
