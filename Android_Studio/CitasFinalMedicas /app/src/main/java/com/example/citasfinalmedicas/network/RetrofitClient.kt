package com.example.citasfinalmedicas.network

import android.content.Context
import com.example.citasfinalmedicas.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Objeto singleton que gestiona la configuración de Retrofit para realizar solicitudes HTTP.
object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/" // URL base del backend. Cambia a tu IP si usas un dispositivo físico.
    private var apiServiceInstance: ApiService? = null // Instancia única de ApiService.

    // Interceptor para registrar las solicitudes y respuestas HTTP en el log.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Nivel de detalle: BODY muestra el cuerpo de las solicitudes/respuestas.
    }

    private lateinit var okHttpClient: OkHttpClient // Cliente HTTP configurado con interceptores.

    // Método para inicializar Retrofit con el contexto de la aplicación.
    fun initialize(context: Context) {
        if (!::okHttpClient.isInitialized) { // Verifica si el cliente ya fue inicializado.
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // Añade el interceptor de logging.
                .addInterceptor(AuthInterceptor(context.applicationContext)) // Añade el interceptor de autenticación.
                .connectTimeout(30, TimeUnit.SECONDS) // Tiempo máximo para establecer conexión.
                .readTimeout(30, TimeUnit.SECONDS) // Tiempo máximo para leer datos.
                .writeTimeout(30, TimeUnit.SECONDS) // Tiempo máximo para enviar datos.
                .build()

            // Configuración de Retrofit con la URL base y el cliente HTTP.
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL) // URL base del backend.
                .client(okHttpClient) // Usa el cliente HTTP configurado.
                .addConverterFactory(GsonConverterFactory.create()) // Usa Gson para convertir JSON a objetos Kotlin.
                .build()

            apiServiceInstance = retrofit.create(ApiService::class.java) // Crea la instancia de ApiService.
        }
    }

    // Propiedad para acceder a la instancia de ApiService.
    val apiService: ApiService
        get() {
            if (apiServiceInstance == null) {
                // Lanza una excepción si RetrofitClient no ha sido inicializado.
                throw IllegalStateException("RetrofitClient no ha sido inicializado. Llama a RetrofitClient.initialize(context) en tu clase Application.")
            }
            return apiServiceInstance!! // Retorna la instancia de ApiService.
        }
}