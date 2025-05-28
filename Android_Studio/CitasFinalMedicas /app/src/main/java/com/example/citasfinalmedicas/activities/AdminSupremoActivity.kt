package com.example.citasfinalmedicas.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.citasfinalmedicas.R
import com.google.android.material.navigation.NavigationView

// Esta clase representa la actividad principal para el administrador supremo.
// Permite gestionar administradores, médicos y pacientes, además de implementar un menú de navegación.
class AdminSupremoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Declaración de variables para el diseño del DrawerLayout y botones.
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnManageAdmins: Button
    private lateinit var btnManageMedicosSupremo: Button
    private lateinit var btnManagePacientesSupremo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_supremo) // Establece el diseño de la actividad.

        // Configuración de la barra de herramientas (Toolbar).
        val toolbar: Toolbar = findViewById(R.id.toolbar_admin_supremo)
        setSupportActionBar(toolbar)

        // Inicialización del DrawerLayout y NavigationView.
        drawerLayout = findViewById(R.id.drawer_layout_admin_supremo)
        val navigationView: NavigationView = findViewById(R.id.nav_view_admin_supremo)
        navigationView.setNavigationItemSelectedListener(this)

        // Configuración del botón de alternancia para abrir/cerrar el menú lateral.
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Inicialización de los botones para gestionar administradores, médicos y pacientes.
        btnManageAdmins = findViewById(R.id.btn_manage_admins)
        btnManageMedicosSupremo = findViewById(R.id.btn_manage_medicos_supremo)
        btnManagePacientesSupremo = findViewById(R.id.btn_manage_pacientes_supremo)

        // Configuración de los listeners para los botones.
        btnManageAdmins.setOnClickListener {
            launchUserListActivity("ADMIN") // Lanza la actividad para gestionar administradores.
        }
        btnManageMedicosSupremo.setOnClickListener {
            launchUserListActivity("MEDICO") // Lanza la actividad para gestionar médicos.
        }
        btnManagePacientesSupremo.setOnClickListener {
            launchUserListActivity("PACIENTE") // Lanza la actividad para gestionar pacientes.
        }
    }

    // Método para lanzar la actividad de lista de usuarios con un rol específico.
    private fun launchUserListActivity(role: String) {
        val intent = Intent(this, UserListActivity::class.java)
        intent.putExtra("USER_ROLE", role) // Pasa el rol como parámetro.
        startActivity(intent)
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
        drawerLayout.closeDrawer(GravityCompat.START) // Cierra el menú lateral.
        return true
    }

    // Método para cerrar sesión del usuario.
    private fun performLogout() {
        Log.d("AdminSupremoActivity", "Iniciando performLogout().")
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            LoginActivity.AUTH_PREFS_NAME,
            Context.MODE_PRIVATE
        )
        with(sharedPreferences.edit()) {
            clear() // Limpia las preferencias compartidas.
            apply()
        }
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent) // Lanza la actividad de inicio de sesión.
        finish() // Finaliza la actividad actual.
    }
}