package com.example.citasfinalmedicas.adapters // O tu paquete de adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.citasfinalmedicas.R
import com.example.citasfinalmedicas.model.Usuario // Tu modelo Usuario

// Adaptador para mostrar una lista de usuarios en un RecyclerView.
// Permite realizar acciones como editar y eliminar usuarios.
class UserAdapter(
    private var users: List<Usuario>, // Lista de usuarios a mostrar.
    private val onEditClick: (Usuario) -> Unit, // Callback para manejar la acción de editar.
    private val onDeleteClick: (Usuario) -> Unit // Callback para manejar la acción de eliminar.
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // ViewHolder para mantener las vistas de cada elemento de la lista.
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.tv_user_username) // Muestra el nombre de usuario.
        val emailTextView: TextView = itemView.findViewById(R.id.tv_user_email) // Muestra el correo electrónico.
        val roleTextView: TextView = itemView.findViewById(R.id.tv_user_role) // Muestra el rol del usuario.
        val editButton: Button = itemView.findViewById(R.id.btn_edit_user) // Botón para editar el usuario.
        val deleteButton: Button = itemView.findViewById(R.id.btn_delete_user) // Botón para eliminar el usuario.
    }

    // Método para crear un nuevo ViewHolder cuando el RecyclerView lo necesita.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false) // Infla el layout del elemento.
        return UserViewHolder(view) // Retorna el ViewHolder con las vistas inicializadas.
    }

    // Método para enlazar los datos de un usuario con las vistas del ViewHolder.
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position] // Obtiene el usuario en la posición actual.
        holder.usernameTextView.text = user.username // Asigna el nombre de usuario.
        holder.emailTextView.text = user.email // Asigna el correo electrónico.
        holder.roleTextView.text = "Rol: ${user.rol?.name ?: "No especificado"}" // Asigna el rol del usuario.

        // Configura los listeners para los botones de editar y eliminar.
        holder.editButton.setOnClickListener { onEditClick(user) } // Llama al callback de edición.
        holder.deleteButton.setOnClickListener { onDeleteClick(user) } // Llama al callback de eliminación.
    }

    // Método para obtener el número de elementos en la lista.
    override fun getItemCount(): Int = users.size

    // Método para actualizar la lista de usuarios y notificar al RecyclerView.
    @SuppressLint("NotifyDataSetChanged") // Usar con precaución, idealmente usar DiffUtil para mejor rendimiento.
    fun updateData(newUsers: List<Usuario>) {
        users = newUsers // Actualiza la lista de usuarios.
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado.
    }
}