package com.example.citasfinalmedicas.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // Necesario para el diálogo de confirmación
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citasfinalmedicas.R
import com.example.citasfinalmedicas.adapters.UserAdapter
import com.example.citasfinalmedicas.api.ApiService
import com.example.citasfinalmedicas.model.Usuario // Tu modelo Usuario
import com.example.citasfinalmedicas.network.RetrofitClient
import kotlinx.coroutines.launch
import java.util.Locale // Importación necesaria para Locale

// Esta clase muestra una lista de usuarios según su rol.
// Permite editar o eliminar usuarios desde la lista.
class UserListActivity : AppCompatActivity() {

    // Declaración de variables para los elementos de la interfaz.
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var apiService: ApiService
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyListMessage: TextView
    private lateinit var toolbar: Toolbar

    private var roleToFetch: String? = null // Rol de los usuarios que se deben listar.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list) // Establece el diseño de la actividad.

        // Inicialización de la barra de herramientas.
        toolbar = findViewById(R.id.toolbar_user_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Habilita el botón "Atrás".

        // Inicialización del servicio API.
        apiService = RetrofitClient.apiService

        // Inicialización de los elementos de la interfaz.
        recyclerViewUsers = findViewById(R.id.recyclerView_users)
        progressBar = findViewById(R.id.progressBar_user_list)
        tvEmptyListMessage = findViewById(R.id.tv_empty_list_message)

        // Configuración del RecyclerView.
        recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(
            emptyList(),
            onEditClick = { usuario ->
                Log.d("UserListActivity", "Editar usuario: ${usuario.username} (ID: ${usuario.id})")
                if (usuario.id == null) {
                    Toast.makeText(this, "ID de usuario inválido para editar.", Toast.LENGTH_SHORT).show()
                    return@UserAdapter
                }
                val intent = Intent(this, UserEditActivity::class.java) // Lanza la actividad de edición.
                intent.putExtra("USER_ID", usuario.id.toLong()) // Pasa el ID del usuario.
                startActivity(intent)
            },
            onDeleteClick = { usuario ->
                Log.d("UserListActivity", "Intentando borrar usuario: ${usuario.username} (ID: ${usuario.id})")
                mostrarDialogoConfirmacionBorrado(usuario) // Muestra un diálogo de confirmación.
            }
        )
        recyclerViewUsers.adapter = userAdapter

        // Obtiene el rol de los usuarios desde el Intent.
        roleToFetch = intent.getStringExtra("USER_ROLE")
        if (roleToFetch == null) {
            Toast.makeText(this, "Rol no especificado para listar.", Toast.LENGTH_LONG).show()
            Log.e("UserListActivity", "USER_ROLE extra no encontrado en el Intent.")
            finish()
            return
        }

        // Configura el título de la barra de herramientas.
        val roleDisplayName = roleToFetch!!.toLowerCase(Locale.getDefault()).capitalize(Locale.getDefault())
        supportActionBar?.title = "Lista de ${roleDisplayName}s"

        // Carga la lista de usuarios según el rol.
        fetchUsersByRole(roleToFetch!!)
    }

    // Método para obtener usuarios según su rol desde el backend.
    private fun fetchUsersByRole(role: String) {
        progressBar.visibility = View.VISIBLE
        tvEmptyListMessage.visibility = View.GONE
        recyclerViewUsers.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = apiService.getUsersByRole(role)
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null && users.isNotEmpty()) {
                        userAdapter.updateData(users) // Actualiza la lista de usuarios en el adaptador.
                        recyclerViewUsers.visibility = View.VISIBLE
                    } else {
                        tvEmptyListMessage.text = "No se encontraron usuarios para el rol: $role"
                        tvEmptyListMessage.visibility = View.VISIBLE
                        Log.d("UserListActivity", "No hay usuarios para el rol: $role")
                    }
                } else {
                    tvEmptyListMessage.text = "Error al obtener usuarios: ${response.code()}"
                    tvEmptyListMessage.visibility = View.VISIBLE
                    val errorBody = response.errorBody()?.string()
                    Log.e("UserListActivity", "Error API ${response.code()}: $errorBody")
                    Toast.makeText(this@UserListActivity, "Error ${response.code()}: ${errorBody ?: "Error desconocido"}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                tvEmptyListMessage.text = "Excepción al obtener usuarios."
                tvEmptyListMessage.visibility = View.VISIBLE
                Log.e("UserListActivity", "Excepción al obtener usuarios", e)
                Toast.makeText(this@UserListActivity, "Excepción: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    // Muestra un diálogo de confirmación antes de eliminar un usuario.
    private fun mostrarDialogoConfirmacionBorrado(usuario: Usuario) {
        if (usuario.id == null) {
            Toast.makeText(this, "ID de usuario inválido para borrar.", Toast.LENGTH_SHORT).show()
            Log.e("UserListActivity", "Intento de borrar usuario con ID nulo: ${usuario.username}")
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar al usuario '${usuario.username}' (ID: ${usuario.id})?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                borrarUsuarioEnBackend(usuario) // Llama al método para eliminar el usuario.
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    // Método para eliminar un usuario en el backend.
    private fun borrarUsuarioEnBackend(usuario: Usuario) {
        val userId = usuario.id!!.toLong() // Convierte el ID a Long.

        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = apiService.deleteUser(userId)
                if (response.isSuccessful) {
                    Toast.makeText(this@UserListActivity, "Usuario '${usuario.username}' eliminado exitosamente.", Toast.LENGTH_SHORT).show()
                    roleToFetch?.let { fetchUsersByRole(it) } // Refresca la lista de usuarios.
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@UserListActivity, "Error al eliminar (${response.code()}): $errorBody", Toast.LENGTH_LONG).show()
                    Log.e("UserListActivity", "Error API al eliminar: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Toast.makeText(this@UserListActivity, "Excepción al eliminar usuario: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("UserListActivity", "Excepción al eliminar", e)
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    // Manejo del botón "Atrás" en la barra de herramientas.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}