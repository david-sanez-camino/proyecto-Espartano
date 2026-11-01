package com.espartano.app.ui.gimnasio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.espartano.app.domain.model.Rutina
import com.espartano.app.domain.model.Sesion
import com.espartano.app.ui.theme.Azul
import com.espartano.app.ui.theme.FondoOscuro
import com.espartano.app.ui.theme.GrisLinea
import com.espartano.app.ui.theme.Naranja
import com.espartano.app.ui.theme.Oro
import com.espartano.app.ui.theme.Rojo
import com.espartano.app.ui.theme.SuperficieMedia
import com.espartano.app.ui.theme.TextoSecundario
import com.espartano.app.ui.theme.Verde

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GimnasioScreen(
    onNuevaRutina: () -> Unit,
    onIniciarRutina: (Long) -> Unit,
    viewModel: GimnasioViewModel = hiltViewModel()
) {
    val rutinas by viewModel.rutinas.collectAsStateWithLifecycle()
    val sesiones by viewModel.sesiones.collectAsStateWithLifecycle()
    var tabSeleccionado by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gimnasio") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoOscuro, titleContentColor = Oro)
            )
        },
        floatingActionButton = {
            if (tabSeleccionado == 0) {
                FloatingActionButton(onClick = onNuevaRutina, containerColor = Oro) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva rutina")
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TabRow(
                selectedTabIndex = tabSeleccionado,
                containerColor = FondoOscuro,
                contentColor = Oro
            ) {
                Tab(
                    selected = tabSeleccionado == 0,
                    onClick = { tabSeleccionado = 0 },
                    text = { Text("Rutinas") }
                )
                Tab(
                    selected = tabSeleccionado == 1,
                    onClick = { tabSeleccionado = 1 },
                    text = { Text("Historial") }
                )
            }

            when (tabSeleccionado) {
                0 -> TabRutinas(rutinas, onIniciarRutina, viewModel::eliminarRutina)
                1 -> TabHistorial(sesiones)
            }
        }
    }
}

@Composable
private fun TabRutinas(
    rutinas: List<Rutina>,
    onIniciar: (Long) -> Unit,
    onEliminar: (Long) -> Unit
) {
    if (rutinas.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Crea tu primera rutina con el botón +",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
        }
        return
    }

    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(rutinas, key = { it.id }) { rutina ->
            RutinaCard(rutina, onIniciar = { onIniciar(rutina.id) }, onEliminar = { onEliminar(rutina.id) })
        }
    }
}

@Composable
private fun RutinaCard(rutina: Rutina, onIniciar: () -> Unit, onEliminar: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SuperficieMedia),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    rutina.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    color = Oro,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar rutina", tint = Rojo)
                }
            }

            Text(
                "${rutina.ejercicios.size} ejercicio${if (rutina.ejercicios.size != 1) "s" else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )

            rutina.ejercicios.take(4).forEach { e ->
                Text(
                    "· ${e.ejercicio}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorGrupo(e.grupoMuscular)
                )
            }
            if (rutina.ejercicios.size > 4) {
                Text(
                    "+ ${rutina.ejercicios.size - 4} más...",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }

            androidx.compose.material3.Button(
                onClick = onIniciar,
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Oro)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Text(
                    "  INICIAR ENTRENAMIENTO",
                    style = MaterialTheme.typography.labelLarge,
                    color = FondoOscuro
                )
            }
        }
    }
}

@Composable
private fun TabHistorial(sesiones: List<Sesion>) {
    if (sesiones.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Aún no hay sesiones registradas",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
        }
        return
    }

    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sesiones, key = { it.id }) { sesion ->
            SesionCard(sesion)
        }
    }
}

@Composable
private fun SesionCard(sesion: Sesion) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SuperficieMedia),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(sesion.nombre, style = MaterialTheme.typography.titleMedium, color = Oro, fontWeight = FontWeight.Bold)
                    Text(sesion.fecha.toString(), style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                }
                if (sesion.duracionSegundos > 0) {
                    Text(
                        formatDuracion(sesion.duracionSegundos),
                        style = MaterialTheme.typography.labelMedium,
                        color = Verde
                    )
                }
            }
            androidx.compose.material3.HorizontalDivider(color = GrisLinea)

            // Agrupa las series por ejercicio
            val porEjercicio = sesion.ejercicios.groupBy { it.ejercicio }
            porEjercicio.forEach { (nombreEj, series) ->
                val grupoMuscular = series.first().grupoMuscular
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(nombreEj, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            Text(grupoMuscular, style = MaterialTheme.typography.labelSmall, color = colorGrupo(grupoMuscular))
                        }
                    }
                    series.sortedBy { it.numeroSerie }.forEach { s ->
                        Text(
                            "  Serie ${s.numeroSerie}: ${if (s.pesoKg > 0f) "%.1f kg".format(s.pesoKg) else "--"} × ${s.repeticiones} reps",
                            style = MaterialTheme.typography.bodySmall,
                            color = Oro
                        )
                    }
                }
            }
        }
    }
}

private fun formatDuracion(segundos: Int): String {
    val h = segundos / 3600
    val m = (segundos % 3600) / 60
    val s = segundos % 60
    return if (h > 0) "%dh %02dm".format(h, m) else if (m > 0) "%dm %02ds".format(m, s) else "${s}s"
}

internal fun colorGrupo(grupo: String) = when (grupo) {
    "Pecho" -> Azul
    "Espalda" -> Verde
    "Hombros" -> Naranja
    "Bíceps", "Tríceps" -> Oro
    "Piernas" -> androidx.compose.ui.graphics.Color(0xFF9C6ADE)
    "Abdomen" -> Rojo
    "Cardio" -> androidx.compose.ui.graphics.Color(0xFF4CBFCF)
    else -> Oro
}
