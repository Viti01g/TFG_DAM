package com.example.citasfinalmedicas.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.citasfinalmedicas.R
import com.example.citasfinalmedicas.api.ApiService
import com.example.citasfinalmedicas.model.Rol
import com.example.citasfinalmedicas.model.Usuario
import com.example.citasfinalmedicas.model.UserUpdateRequest
import com.example.citasfinalmedicas.network.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.Locale

// Esta clase permite editar los datos de un usuario existente.
// Incluye la edición de nombre de usuario, correo electrónico, rol y contraseña.
class UserEditActivity : AppCompatActivity() {

    // Declaración de variables para los elementos de la interfaz.
    private lateinit var toolbar: Toolbar
    private lateinit var tilUsername: TextInputLayout
    private lateinit var etUsername: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var tilRole: TextInputLayout
    private lateinit var actvRole: AutoCompleteTextView
    private lateinit var tilNewPassword: TextInputLayout
    private lateinit var etNewPassword: TextInputEditText
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnSaveChanges: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var apiService: ApiService
    private var currentUserId: Long? = null // ID del usuario actual.
    private var currentUserData: Usuario? = null // Datos actuales del usuario.

    // Obtiene los nombres de tu enum Rol para el desplegable.
    private val rolesDisponibles = Rol.values().map { it.name }.toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit) // Establece el diseño de la actividad.

        // Inicialización de la barra de herramientas.
        toolbar = findViewById(R.id.toolbar_user_edit)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Habilita el botón "Atrás".
        supportActionBar?.title = "Cargando Datos..." // Título inicial.

        // Inicialización del servicio API.
        apiService = RetrofitClient.apiService

        // Inicialización de los elementos de la interfaz.
        tilUsername = findViewById(R.id.til_edit_username)
        etUsername = findViewById(R.id.et_edit_username)
        tilEmail = findViewById(R.id.til_edit_email)
        etEmail = findViewById(R.id.et_edit_email)
        tilRole = findViewById(R.id.til_edit_role)
        actvRole = findViewById(R.id.actv_edit_role)
        tilNewPassword = findViewById(R.id.til_edit_new_password)
        etNewPassword = findViewById(R.id.et_edit_new_password)
        tilConfirmPassword = findViewById(R.id.til_edit_confirm_password)
        etConfirmPassword = findViewById(R.id.et_edit_confirm_password)
        btnSaveChanges = findViewById(R.id.btn_save_user_changes)
        progressBar = findViewById(R.id.progressBar_user_edit)

        // Configuración del adaptador para el campo de rol.
        val roleAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, rolesDisponibles)
        actvRole.setAdapter(roleAdapter)

        // Obtiene el ID del usuario desde el Intent.
        currentUserId = intent.getLongExtra("USER_ID", -1L)
        if (currentUserId == null || currentUserId == -1L) {
            Toast.makeText(this, "Error: ID de usuario no proporcionado.", Toast.LENGTH_LONG).show()
            Log.e("UserEditActivity", "USER_ID no encontrado en el Intent.")
            finish()
            return
        }

        // Carga los datos del usuario.
        loadUserData(currentUserId!!)

        // Configuración del listener para el botón de guardar cambios.
        btnSaveChanges.setOnClickListener {
            performUpdateUser() // Llama al método para actualizar los datos del usuario.
        }
    }

    // Método para cargar los datos del usuario desde el backend.
    private fun loadUserData(userId: Long) {
        progressBar.visibility = View.VISIBLE
        setFormEnabled(false) // Desactiva el formulario mientras se cargan los datos.
        lifecycleScope.launch {
            try {
                val response = apiService.getUserById(userId)
                if (response.isSuccessful) {
                    currentUserData = response.body()
                    currentUserData?.let { user ->
                        supportActionBar?.title = "Editar: ${user.username}" // Actualiza el título.
                        etUsername.setText(user.username)
                        etEmail.setText(user.email)
                        actvRole.setText(user.rol?.name, false) // Asigna el rol actual.
                        etNewPassword.setText("") // Limpia el campo de nueva contraseña.
                        etConfirmPassword.setText("") // Limpia el campo de confirmación.
                    } ?: run {
                        Toast.makeText(this@UserEditActivity, "No se pudieron obtener los datos del usuario.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@UserEditActivity, "Error al cargar usuario (${response.code()}): ${errorBody ?: "Error desconocido"}", Toast.LENGTH_LONG).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@UserEditActivity, "Excepción al cargar usuario: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            } finally {
                progressBar.visibility = View.GONE
                setFormEnabled(true) // Reactiva el formulario.
            }
        }
    }

    // Método para actualizar los datos del usuario.
    private fun performUpdateUser() {
        if (currentUserId == null || currentUserData == null) {
            Toast.makeText(this, "Error: Datos de usuario originales no cargados.", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoUsernameStr = etUsername.text.toString().trim()
        val nuevoEmailStr = etEmail.text.toString().trim()
        val nuevoRolStr = actvRole.text.toString().trim().toUpperCase(Locale.ROOT)
        val nuevaPasswordStr = etNewPassword.text.toString()
        val confirmarPasswordStr = etConfirmPassword.text.toString()

        // Validación de campos obligatorios.
        if (nuevoUsernameStr.isEmpty() || nuevoEmailStr.isEmpty() || nuevoRolStr.isEmpty()) {
            Toast.makeText(this, "Nombre de usuario, Email y Rol no pueden estar vacíos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de contraseñas coincidentes.
        if (nuevaPasswordStr.isNotEmpty() && nuevaPasswordStr != confirmarPasswordStr) {
            tilConfirmPassword.error = "Las nuevas contraseñas no coinciden."
            etConfirmPassword.requestFocus()
            return
        } else {
            tilConfirmPassword.error = null // Limpia el error.
        }

        // Validación de rol válido.
        if (!rolesDisponibles.contains(nuevoRolStr)) {
            Toast.makeText(this, "Rol seleccionado no válido: $nuevoRolStr", Toast.LENGTH_LONG).show()
            actvRole.requestFocus()
            return
        }

        // Construcción del objeto de actualización.
        val updateRequest = UserUpdateRequest(
            username = if (nuevoUsernameStr != currentUserData!!.username) nuevoUsernameStr else null,
            email = if (nuevoEmailStr != currentUserData!!.email) nuevoEmailStr else null,
            rol = if (nuevoRolStr != currentUserData!!.rol?.name) nuevoRolStr else null,
            nuevaPassword = if (nuevaPasswordStr.isNotEmpty()) nuevaPasswordStr else null
        )

        // Validación de cambios.
        if (updateRequest.username == null && updateRequest.email == null && updateRequest.rol == null && updateRequest.nuevaPassword == null) {
            Toast.makeText(this, "No se han detectado cambios para guardar.", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        setFormEnabled(false)

        lifecycleScope.launch {
            try {
                val response = apiService.updateUser(currentUserId!!, updateRequest)
                if (response.isSuccessful) {
                    Toast.makeText(this@UserEditActivity, "Usuario actualizado exitosamente.", Toast.LENGTH_LONG).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@UserEditActivity, "Error al actualizar (${response.code()}): $errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@UserEditActivity, "Excepción al actualizar usuario: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
                setFormEnabled(true)
            }
        }
    }

    // Método para habilitar/deshabilitar el formulario.
    private fun setFormEnabled(enabled: Boolean) {
        etUsername.isEnabled = enabled
        etEmail.isEnabled = enabled
        actvRole.isEnabled = enabled
        tilRole.isEnabled = enabled
        etNewPassword.isEnabled = enabled
        etConfirmPassword.isEnabled = enabled
        btnSaveChanges.isEnabled = enabled
    }

    // Manejo del botón "Atrás" en la barra de herramientas.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}