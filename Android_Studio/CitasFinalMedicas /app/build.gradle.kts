plugins {
    alias(libs.plugins.android.application) // Plugin para aplicaciones Android.
    alias(libs.plugins.kotlin.android) // Plugin para usar Kotlin en Android.
    alias(libs.plugins.kotlin.compose) // Plugin para habilitar Jetpack Compose.
}

android {
    namespace = "com.example.citasfinalmedicas" // Espacio de nombres único para la aplicación.
    compileSdk = 35 // Versión del SDK de compilación.

    defaultConfig {
        applicationId = "com.example.citasfinalmedicas" // ID único de la aplicación.
        minSdk = 29 // Versión mínima del SDK requerida para ejecutar la app.
        targetSdk = 35 // Versión del SDK objetivo para la app.
        versionCode = 1 // Código de versión de la app.
        versionName = "1.0" // Nombre de la versión de la app.
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Runner para pruebas instrumentadas.
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Desactiva la minificación en el modo release.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Archivo ProGuard predeterminado.
                "proguard-rules.pro" // Archivo personalizado de reglas ProGuard.
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11 // Compatibilidad con Java 11.
        targetCompatibility = JavaVersion.VERSION_11 // Compatibilidad con Java 11.
    }

    kotlinOptions {
        jvmTarget = "11" // Configuración del target JVM para Kotlin.
    }

    buildFeatures {
        compose = true // Habilita Jetpack Compose.
    }
}

dependencies {
    // Dependencias básicas de AndroidX y Lifecycle.
    implementation(libs.androidx.core.ktx) // Extensiones de Kotlin para Android.
    implementation(libs.androidx.lifecycle.runtime.ktx) // Extensiones para manejar el ciclo de vida.

    // Dependencias para Jetpack Compose.
    implementation(libs.androidx.activity.compose) // Integración de Compose con actividades tradicionales.
    implementation(platform(libs.androidx.compose.bom)) // BOM para asegurar versiones compatibles de Compose.
    implementation(libs.androidx.ui) // Componentes básicos de UI en Compose.
    implementation(libs.androidx.ui.graphics) // Herramientas para gráficos en Compose.
    implementation(libs.androidx.ui.tooling.preview) // Herramientas para previsualizar componentes de Compose.
    implementation(libs.androidx.material3) // Implementación de Material Design 3 para Compose.

    // Dependencias adicionales para Compose.
    implementation(platform("androidx.compose:compose-bom:2023.08.00")) // BOM para Compose.
    implementation("androidx.compose.ui:ui") // Componentes de UI en Compose.
    implementation("androidx.compose.material3:material3") // Material Design 3 para Compose.
    implementation("androidx.compose.material:material-icons-extended:1.6.7") // Iconos extendidos para Compose.

    // Dependencias para Views tradicionales (XML).
    implementation("com.google.android.material:material:1.12.0") // Componentes de Material Design para XML.
    implementation("androidx.cardview:cardview:1.0.0") // CardView para diseños XML.

    // Dependencias para la red (API REST).
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Biblioteca para solicitudes HTTP.
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Conversión de JSON a objetos Kotlin usando Gson.
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Interceptor para registrar solicitudes y respuestas HTTP.

    // Kotlin Coroutines.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1") // Coroutines para manejar tareas asincrónicas.

    // Dependencias para Testing.
    testImplementation(libs.junit) // Framework para pruebas unitarias.
    androidTestImplementation(libs.androidx.junit) // Extensiones de JUnit para pruebas en Android.
    androidTestImplementation(libs.androidx.espresso.core) // Framework para pruebas de UI en Android.
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM para pruebas de Compose.
    androidTestImplementation(libs.androidx.ui.test.junit4) // Herramientas para probar componentes de Compose.
    debugImplementation(libs.androidx.ui.tooling) // Herramientas de depuración para Compose.
    debugImplementation(libs.androidx.ui.test.manifest) // Herramientas para probar configuraciones de manifiesto en Compose.
}