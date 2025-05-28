package com.example.citasfinalmedicas.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64 // Utilizado para codificar las credenciales en Base64.
import android.util.Log
import com.example.citasfinalmedicas.activities.LoginActivity // Para acceder a las constantes de preferencias.
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

// Clase que implementa un interceptor para añadir la cabecera de autenticación Basic Auth a las peticiones HTTP.
class AuthInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request() // Obtiene la petición original.

        // No aplicar Basic Auth a las peticiones de login o registro público.
        // Estas peticiones no requieren autenticación.
        if (originalRequest.url.encodedPath.startsWith("/api/auth/")) {
            Log.d("AuthInterceptor", "Petición a /api/auth, no se añade cabecera Basic Auth.")
            return chain.proceed(originalRequest) // Envía la petición original sin modificar.
        }

        // Obtiene las credenciales almacenadas en SharedPreferences.
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            LoginActivity.AUTH_PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val username = sharedPreferences.getString(LoginActivity.KEY_USERNAME_FOR_BASIC_AUTH, null)
        val password = sharedPreferences.getString(LoginActivity.KEY_PASSWORD_FOR_BASIC_AUTH, null)

        Log.d("AuthInterceptor", "Username para Basic Auth: $username")
        if (password != null) {
            Log.d("AuthInterceptor", "Password para Basic Auth: (presente)") // No loguear la contraseña directamente.
        } else {
            Log.d("AuthInterceptor", "Password para Basic Auth: (NO presente)")
        }

        // Si las credenciales son válidas, se añade la cabecera de autenticación.
        if (username != null && password != null && username.isNotBlank() && password.isNotBlank()) {
            val credentials = "$username:$password" // Combina el nombre de usuario y la contraseña.
            val basicAuthHeader = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP) // Codifica en Base64.

            Log.d("AuthInterceptor", "Añadiendo cabecera Basic Auth para: $username")

            // Crea una nueva petición con la cabecera de autenticación.
            val newRequest: Request = originalRequest.newBuilder()
                .header("Authorization", basicAuthHeader) // Añade o reemplaza la cabecera "Authorization".
                .build()
            return chain.proceed(newRequest) // Envía la nueva petición.
        } else {
            Log.d("AuthInterceptor", "No se encontraron credenciales válidas para Basic Auth. Enviando petición original sin cabecera Authorization.")
            // Si no hay credenciales, la petición se envía sin la cabecera "Authorization".
            return chain.proceed(originalRequest)
        }
    }
}