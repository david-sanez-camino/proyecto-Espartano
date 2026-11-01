package com.espartano.app.ui.gimnasio

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.espartano.app.domain.model.EjerciciosCatalogo
import com.espartano.app.domain.model.RutinaEjercicio
import com.espartano.app.ui.components.EspartanoTextField
import com.espartano.app.ui.theme.FondoOscuro
import com.espartano.app.ui.theme.GrisLinea
import com.espartano.app.ui.theme.Oro
import com.espartano.app.ui.theme.Rojo
import com.espartano.app.ui.theme.SuperficieMedia
import com.espartano.app.ui.theme.TextoSecundario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRutinaScreen(
    onNavigateBack: () -> Unit,
    viewModel: CrearRutinaViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetGrupo = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetEjercicio = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(state.guardado) {
        if (state.guardado) onNavigateBack()
    }

    if (state.mostrarSelectorGrupo) {
        ModalBottomSheet(
            onDismissRequest = viewModel::cerrarSelectorGrupo,
            sheetState = sheetGrupo,
            containerColor = SuperficieMedia
        ) {
            Text("Grupo muscular", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
            EjerciciosCatalogo.grupos.forEach { grupo ->
                ListItem(
                    headlineContent = { Text(grupo) },
                    leadingContent = { Icon(Icons.Default.FitnessCenter, null, tint = colorGrupo(grupo)) },
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
            sheetState = sheetEjercicio,
            containerColor = SuperficieMedia
        ) {
            Text(grupo, style = MaterialTheme.typography.titleLarge, color = colorGrupo(grupo), modifier = Modifier.padding(16.dp))
            EjerciciosCatalogo.porGrupo(grupo).forEach { ejercicio ->
                val yaAgregado = state.ejerciciosSeleccionados.any { it.ejercicio == ejercicio.nombre }
                ListItem(
                    headlineContent = {
                        Text(
                            ejercicio.nombre,
                            color = if (yaAgregado) TextoSecundario else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    trailingContent = { if (yaAgregado) Text("✓", color = Oro) },
                    modifier = Modifier.clickable { viewModel.agregarEjercicio(ejercicio) },
                    colors = ListItemDefaults.colors(containerColor = SuperficieMedia)
                )
                HorizontalDivider(color = GrisLinea)
            }
            Box(Modifier.padding(bottom = 32.dp))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva rutina") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Oro)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoOscuro, titleContentColor = Oro)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                EspartanoTextField(
                    value = state.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = "Nombre de la rutina",
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                    isError = state.errorNombre,
                    errorMsg = if (state.errorNombre) "Introduce un nombre" else null,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Ejercicios (${state.ejerciciosSeleccionados.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = Oro
                    )
                    Button(
                        onClick = viewModel::abrirSelectorGrupo,
                        colors = ButtonDefaults.buttonColors(containerColor = SuperficieMedia)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Oro)
                        Text("  Añadir", color = Oro)
                    }
                }
            }

            if (state.ejerciciosSeleccionados.isEmpty()) {
                item {
                    Text(
                        "Añade al menos un ejercicio para crear la rutina",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            } else {
                items(state.ejerciciosSeleccionados, key = { it.ejercicio }) { e ->
                    EjercicioSeleccionadoItem(e, onQuitar = { viewModel.quitarEjercicio(e) })
                }
            }

            item {
                Button(
                    onClick = viewModel::guardar,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.nombre.isNotBlank() && state.ejerciciosSeleccionados.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Oro)
                ) {
                    Text("GUARDAR RUTINA", style = MaterialTheme.typography.labelLarge, color = FondoOscuro)
                }
            }
        }
    }
}

@Composable
private fun EjercicioSeleccionadoItem(ejercicio: RutinaEjercicio, onQuitar: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SuperficieMedia),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(ejercicio.ejercicio, style = MaterialTheme.typography.bodyMedium)
                Text(ejercicio.grupoMuscular, style = MaterialTheme.typography.labelSmall, color = colorGrupo(ejercicio.grupoMuscular))
            }
            IconButton(onClick = onQuitar) {
                Icon(Icons.Default.Close, contentDescription = "Quitar", tint = Rojo)
            }
        }
    }
}
