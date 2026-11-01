package com.espartano.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.espartano.app.ui.ajustes.AjustesScreen
import com.espartano.app.ui.dashboard.DashboardScreen
import com.espartano.app.ui.gimnasio.CrearRutinaScreen
import com.espartano.app.ui.gimnasio.EntrenamientoActivoScreen
import com.espartano.app.ui.gimnasio.GimnasioScreen
import com.espartano.app.ui.graficos.GraficosScreen
import com.espartano.app.ui.historial.HistorialScreen
import com.espartano.app.ui.metas.MetasScreen
import com.espartano.app.ui.medicion.NuevaMedicionScreen
import com.espartano.app.ui.theme.FondoOscuro
import com.espartano.app.ui.theme.Oro
import com.espartano.app.ui.theme.SuperficieOscura
import com.espartano.app.ui.theme.TextoSecundario

private data class NavItem(val screen: Screen, val label: String, val icon: ImageVector)

private val bottomNavItems = listOf(
    NavItem(Screen.Dashboard, "Inicio", Icons.Default.Home),
    NavItem(Screen.Gimnasio, "Gimnasio", Icons.Default.FitnessCenter),
    NavItem(Screen.Graficos, "Gráficos", Icons.Default.BarChart),
    NavItem(Screen.Metas, "Metas", Icons.Default.TrackChanges),
    NavItem(Screen.Ajustes, "Ajustes", Icons.Default.Settings)
)

private val screensWithBottomBar = bottomNavItems.map { it.screen.route }.toSet()

@Composable
fun EspartanoNavHost() {
    val navController = rememberNavController()
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in screensWithBottomBar) {
                NavigationBar(containerColor = SuperficieOscura) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.screen.route,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Oro,
                                selectedTextColor = Oro,
                                indicatorColor = FondoOscuro,
                                unselectedIconColor = TextoSecundario,
                                unselectedTextColor = TextoSecundario
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNuevaMedicion = { navController.navigate(Screen.NuevaMedicion.route) },
                    onVerHistorial = { navController.navigate(Screen.Historial.route) }
                )
            }
            composable(Screen.NuevaMedicion.route) {
                NuevaMedicionScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Historial.route) { HistorialScreen() }
            composable(Screen.Gimnasio.route) {
                GimnasioScreen(
                    onNuevaRutina = { navController.navigate(Screen.CrearRutina.route) },
                    onIniciarRutina = { rutinaId ->
                        navController.navigate(Screen.EntrenamientoActivo.conId(rutinaId))
                    }
                )
            }
            composable(Screen.CrearRutina.route) {
                CrearRutinaScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(
                route = Screen.EntrenamientoActivo.route,
                arguments = listOf(navArgument("rutinaId") { type = NavType.LongType })
            ) {
                EntrenamientoActivoScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Graficos.route) { GraficosScreen() }
            composable(Screen.Metas.route) { MetasScreen() }
            composable(Screen.Ajustes.route) { AjustesScreen() }
        }
    }
}
