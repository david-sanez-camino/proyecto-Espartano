package com.espartano.app.ui.ajustes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.espartano.app.ui.components.EspartanoTextField
import com.espartano.app.ui.components.SectionTitle
import com.espartano.app.ui.theme.FondoOscuro
import com.espartano.app.ui.theme.GrisLinea
import com.espartano.app.ui.theme.Oro
import com.espartano.app.ui.theme.SuperficieMedia
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(viewModel: AjustesViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarDatePicker by remember { mutableStateOf(false) }
    val fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    LaunchedEffect(state.snackMsg) {
        state.snackMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissSnack()
        }
    }

    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.fechaNacimiento
                ?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val fecha = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                        viewModel.onFechaNacimientoChange(fecha)
                    }
                    mostrarDatePicker = false
                }) { Text("Aceptar", color = Oro) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) {
                    Text("Cancelar", color = Oro)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoOscuro,
                    titleContentColor = Oro
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionTitle("Perfil")

            EspartanoTextField(
                value = state.altura,
                onValueChange = viewModel::onAlturaChange,
                label = "Altura",
                suffix = "cm",
                keyboardType = KeyboardType.Decimal
            )

            OutlinedButton(
                onClick = { mostrarDatePicker = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GrisLinea),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = SuperficieMedia)
            ) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Oro,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = state.fechaNacimiento?.format(fmt) ?: "Seleccionar fecha de nacimiento",
                    color = if (state.fechaNacimiento != null) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            state.perfil?.let {
                Text("Edad: ${it.edad} años", style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick = viewModel::guardarPerfil,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Oro)
            ) {
                Text("GUARDAR PERFIL", style = MaterialTheme.typography.labelLarge, color = FondoOscuro)
            }

            SectionTitle("Recordatorios")
            Text(
                "Frecuencia: cada ${state.frecuenciaRecordatorio} días",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = state.frecuenciaRecordatorio.toFloat(),
                onValueChange = { viewModel.onFrecuenciaChange(it.toInt()) },
                valueRange = 1f..30f,
                steps = 28,
                colors = SliderDefaults.colors(
                    thumbColor = Oro,
                    activeTrackColor = Oro,
                    inactiveTrackColor = GrisLinea
                ),
                modifier = Modifier.fillMaxWidth()
            )

            SectionTitle("Exportar datos")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.exportarCsv(context) },
                    modifier = Modifier.weight(1f).height(52.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Oro)
                ) { Text("Exportar CSV", color = Oro) }

                Button(
                    onClick = { viewModel.exportarPdf(context) },
                    modifier = Modifier.weight(1f).height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Oro)
                ) { Text("Exportar PDF", color = FondoOscuro) }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}
