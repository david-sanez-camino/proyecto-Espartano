package com.espartano.app.ui.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object NuevaMedicion : Screen("nueva_medicion")
    data object Historial : Screen("historial")
    data object Graficos : Screen("graficos")
    data object Metas : Screen("metas")
    data object Ajustes : Screen("ajustes")
    data object Gimnasio : Screen("gimnasio")
    data object CrearRutina : Screen("crear_rutina")
    data object EntrenamientoActivo : Screen("entrenamiento_activo/{rutinaId}") {
        fun conId(rutinaId: Long) = "entrenamiento_activo/$rutinaId"
    }
}
