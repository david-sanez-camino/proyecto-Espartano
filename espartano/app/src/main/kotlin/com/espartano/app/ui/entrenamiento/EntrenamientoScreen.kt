package com.espartano.app.ui.entrenamiento

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.espartano.app.domain.model.EjerciciosCatalogo
import com.espartano.app.domain.model.Entrenamiento
import com.espartano.app.ui.components.EspartanoTextField
import com.espartano.app.ui.components.SectionTitle
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
fun EntrenamientoScreen(viewModel: EntrenamientoViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetStateGrupo = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetStateEjercicio = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(state.snackMsg) {
        state.snackMsg?.let { snackbarHostState.showSnackbar(it); viewModel.dismissSnack() }
    }

    if (state.mostrarSelectorGrupo) {
        ModalBottomSheet(
            onDismissRequest = viewModel::cerrarSelectorGrupo,
            sheetState = sheetStateGrupo,
            containerColor = SuperficieMedia
        ) {
            Text(
                "Selecciona grupo muscular",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            EjerciciosCatalogo.grupos.forEach { grupo ->
                ListItem(
                    headlineContent = { Text(grupo) },
                    leadingContent = {
                        Icon(Icons.Default.FitnessCenter, null, tint = colorGrupo(grupo))
                    },
                    modifier = Modifier.clickable { viewModel.seleccionarGrupo(grupo) },
                    colors = ListItemDefaults.colors(containerColor = SuperficieMedia)
                )
                HorizontalDivider(color = GrisLinea)
            }
            Box(Modifier.padding(bottom = 32.dp))
        }
    }

    if (state.mostrarSelectorEjercicio) {
        val grupo = state.grupoSeleccionado ?: ""
        ModalBottomSheet(
            onDismissRequest = viewModel::cerrarSelectorEjercicio,
            sheetState = sheetStateEjercicio,
            containerColor = SuperficieMedia
        ) {
            Text(
                grupo,
                style = MaterialTheme.typography.titleLarge,
                color = colorGrupo(grupo),
                modifier = Modifier.padding(16.dp)
            )
            EjerciciosCatalogo.porGrupo(grupo).forEach { ejercicio ->
                ListItem(
                    headlineContent = { Text(ejercicio.nombre) },
                    modifier = Modifier.clickable { viewModel.seleccionarEjercicio(ejercicio) },
                    colors = ListItemDefaults.colors(containerColor = SuperficieMedia)
                )
                HorizontalDivider(color = GrisLinea)
            }
            Box(Modifier.padding(bottom = 32.dp))
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Gimnasio") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoOscuro,
                    titleContentColor = Oro
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionTitle("Nuevo registro")

                Card(
                    colors = CardDefaults.cardColors(containerColor = SuperficieMedia),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = viewModel::abrirSelectorGrupo,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.ejercicioSeleccionado != null) FondoOscuro else SuperficieMedia
                            )
                        ) {
                            Icon(Icons.Default.ExpandMore, null, tint = Oro)
                            Text(
                                text = state.ejercicioSeleccionado?.nombre ?: "Seleccionar ejercicio",
                                color = if (state.ejercicioSeleccionado != null) Oro else TextoSecundario,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        state.ejercicioSeleccionado?.let {
                            Text(
                                it.grupoMuscular,
                                style = MaterialTheme.typography.labelSmall,
                                color = colorGrupo(it.grupoMuscular)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            EspartanoTextField(
                                value = state.pesoKg,
                                onValueChange = viewModel::onPesoChange,
                                label = "Peso",
                                suffix = "kg",
                                keyboardType = KeyboardType.Decimal,
                                isError = state.errorPeso != null,
                                errorMsg = state.errorPeso,
                                modifier = Modifier.weight(1f)
                            )
                            EspartanoTextField(
                                value = state.series,
                                onValueChange = viewModel::onSeriesChange,
                                label = "Series",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                            EspartanoTextField(
                                value = state.repeticiones,
                                onValueChange = viewModel::onRepeticionesChange,
                                label = "Reps",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Button(
                            onClick = viewModel::guardar,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Oro)
                        ) {
                            Text(
                                "GUARDAR REGISTRO",
                                style = MaterialTheme.typography.labelLarge,
                                color = FondoOscuro
                            )
                        }
                    }
                }
            }

            if (state.historial.isNotEmpty()) {
                item { SectionTitle("Historial de entrenamientos") }

                val agrupado = state.historial.groupBy { it.fecha }
                agrupado.forEach { (fecha, entrenamientos) ->
                    item {
                        Text(
                            fecha.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = Oro,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(entrenamientos, key = { it.id }) { e ->
                        EntrenamientoItem(e, onEliminar = { viewModel.eliminar(e) })
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Aún no hay registros de entrenamiento.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EntrenamientoItem(entrenamiento: Entrenamiento, onEliminar: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SuperficieMedia),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    entrenamiento.ejercicio,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    entrenamiento.grupoMuscular,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorGrupo(entrenamiento.grupoMuscular)
                )
                Text(
                    "${entrenamiento.pesoKg} kg · ${entrenamiento.series} series · ${entrenamiento.repeticiones} reps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Oro
                )
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Rojo)
            }
        }
    }
}

private fun colorGrupo(grupo: String) = when (grupo) {
    "Pecho" -> Azul
    "Espalda" -> Verde
    "Hombros" -> Naranja
    "Bíceps", "Tríceps" -> Oro
    "Piernas" -> androidx.compose.ui.graphics.Color(0xFF9C6ADE)
    "Abdomen" -> Rojo
    "Cardio" -> androidx.compose.ui.graphics.Color(0xFF4CBFCF)
    else -> Oro
}
