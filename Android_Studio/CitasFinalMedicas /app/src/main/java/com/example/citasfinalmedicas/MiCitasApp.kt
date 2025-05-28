package com.example.citasfinalmedicas // O tu paquete raíz

import android.app.Application
import com.example.citasfinalmedicas.network.RetrofitClient

// Clase principal de la aplicación que extiende Application.
// Se utiliza para inicializar componentes globales como RetrofitClient.
class MiCitasApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializamos RetrofitClient aquí para que esté disponible en toda la aplicación.
        RetrofitClient.initialize(applicationContext)
    }
}