package com.espartano.app.ui.gimnasio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.espartano.app.ui.components.EspartanoTextField
import com.espartano.app.ui.theme.FondoOscuro
import com.espartano.app.ui.theme.GrisLinea
import com.espartano.app.ui.theme.Oro
import com.espartano.app.ui.theme.SuperficieMedia
import com.espartano.app.ui.theme.TextoSecundario
import com.espartano.app.ui.theme.Verde

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrenamientoActivoScreen(
    onNavigateBack: () -> Unit,
    viewModel: EntrenamientoActivoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.finalizado) {
        if (state.finalizado) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Entrenamiento activo", style = MaterialTheme.typography.titleMedium, color = Oro)
                        }
                        // Cronómetro
                        Text(
                            formatTiempo(state.tiempoSegundos),
                            style = MaterialTheme.typography.titleLarge,
                            color = Verde,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Oro)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoOscuro)
            )
        }
    ) { innerPadding ->
        if (state.cargando) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Oro)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Nombre del entrenamiento editable
            item {
                EspartanoTextField(
                    value = state.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = "Nombre del entrenamiento",
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Una tarjeta por ejercicio
            itemsIndexed(state.ejercicios) { ejIdx, ejercicio ->
                EjercicioActivoCard(
                    ejercicioIdx = ejIdx,
                    ejercicioState = ejercicio,
                    onAddSerie = { viewModel.addSerie(ejIdx) },
                    onRemoveSerie = { serieIdx -> viewModel.removeSerie(ejIdx, serieIdx) },
                    onPesoChange = { serieIdx, v -> viewModel.onPesoChange(ejIdx, serieIdx, v) },
                    onRepsChange = { serieIdx, v -> viewModel.onRepsChange(ejIdx, serieIdx, v) },
                    onToggleCompletada = { serieIdx -> viewModel.toggleCompletada(ejIdx, serieIdx) }
                )
            }

            // Botón finalizar
            item {
                Button(
                    onClick = viewModel::finalizar,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Verde)
                ) {
                    Text(
                        "FINALIZAR — ${formatTiempo(state.tiempoSegundos)}",
                        style = MaterialTheme.typography.labelLarge,
                        color = FondoOscuro,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun EjercicioActivoCard(
    ejercicioIdx: Int,
    ejercicioState: EjercicioActivoState,
    onAddSerie: () -> Unit,
    onRemoveSerie: (Int) -> Unit,
    onPesoChange: (Int, String) -> Unit,
    onRepsChange: (Int, String) -> Unit,
    onToggleCompletada: (Int) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SuperficieMedia),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Cabecera del ejercicio
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${ejercicioIdx + 1}.",
                    style = MaterialTheme.typography.titleMedium,
                    color = Oro,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(ejercicioState.ejercicio, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(ejercicioState.grupoMuscular, style = MaterialTheme.typography.labelSmall, color = colorGrupo(ejercicioState.grupoMuscular))
                }
            }

            androidx.compose.material3.HorizontalDivider(color = GrisLinea)

            // Cabecera columnas
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Serie", style = MaterialTheme.typography.labelSmall, color = TextoSecundario, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                Text("Peso (kg)", style = MaterialTheme.typography.labelSmall, color = TextoSecundario, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text("Reps", style = MaterialTheme.typography.labelSmall, color = TextoSecundario, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Box(modifier = Modifier.size(40.dp))  // espacio para botón ✓
                Box(modifier = Modifier.size(40.dp))  // espacio para borrar
            }

            // Filas de series
            ejercicioState.series.forEachIndexed { serieIdx, serie ->
                SerieRow(
                    serie = serie,
                    onPesoChange = { onPesoChange(serieIdx, it) },
                    onRepsChange = { onRepsChange(serieIdx, it) },
                    onToggleCompletada = { onToggleCompletada(serieIdx) },
                    onRemove = { onRemoveSerie(serieIdx) },
                    canRemove = ejercicioState.series.size > 1
                )
            }

            // Botón añadir serie
            OutlinedButton(
                onClick = onAddSerie,
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(contentColor = Oro),
                border = androidx.compose.foundation.BorderStroke(1.dp, GrisLinea)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                Text("  Añadir serie", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun SerieRow(
    serie: SerieActivaState,
    onPesoChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onToggleCompletada: () -> Unit,
    onRemove: () -> Unit,
    canRemove: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Número de serie
        Text(
            "${serie.numero}",
            style = MaterialTheme.typography.titleSmall,
            color = if (serie.completada) Verde else Oro,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        // Campo peso
        EspartanoTextField(
            value = serie.pesoKg,
            onValueChange = onPesoChange,
            label = "kg",
            keyboardType = KeyboardType.Decimal,
            modifier = Modifier.weight(1f),
            enabled = !serie.completada
        )

        // Campo reps
        EspartanoTextField(
            value = serie.repeticiones,
            onValueChange = onRepsChange,
            label = "reps",
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f),
            enabled = !serie.completada
        )

        // Botón completar serie
        IconButton(onClick = onToggleCompletada, modifier = Modifier.size(40.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = if (serie.completada) Verde else GrisLinea,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (serie.completada) {
                    Icon(Icons.Default.Check, contentDescription = "Completada", tint = Color.Black, modifier = Modifier.size(18.dp))
                }
            }
        }

        // Botón eliminar serie
        if (canRemove) {
            IconButton(onClick = onRemove, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = TextoSecundario, modifier = Modifier.size(18.dp))
            }
        } else {
            Box(modifier = Modifier.size(40.dp))
        }
    }
}

private fun formatTiempo(segundos: Int): String {
    val h = segundos / 3600
    val m = (segundos % 3600) / 60
    val s = segundos % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}
