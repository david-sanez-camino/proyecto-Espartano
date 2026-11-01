package com.espartano.app.ui.medicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.espartano.app.ui.components.EspartanoTextField
import com.espartano.app.ui.components.MotivadorDialog
import com.espartano.app.ui.components.SectionTitle
import com.espartano.app.ui.theme.FondoOscuro
import com.espartano.app.ui.theme.Oro
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaMedicionScreen(
    onNavigateBack: () -> Unit,
    viewModel: NuevaMedicionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.guardadoExitoso) {
        if (state.guardadoExitoso) {
            viewModel.resetGuardado()
            onNavigateBack()
        }
    }

    state.mensajeMotivador?.let { mensaje ->
        MotivadorDialog(
            mensaje = mensaje,
            tipo = state.tipoMensaje,
            onDismiss = viewModel::dismissMensaje
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva medición") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
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
            SectionTitle("Fecha: ${LocalDate.now()}")

            EspartanoTextField(
                value = state.peso,
                onValueChange = viewModel::onPesoChange,
                label = "Peso",
                suffix = "kg",
                isError = state.errorPeso != null,
                errorMsg = state.errorPeso
            )

            EspartanoTextField(
                value = state.grasa,
                onValueChange = viewModel::onGrasaChange,
                label = "% Grasa corporal",
                suffix = "%",
                isError = state.errorGrasa != null,
                errorMsg = state.errorGrasa
            )

            EspartanoTextField(
                value = state.musculo,
                onValueChange = viewModel::onMusculoChange,
                label = "% Masa muscular",
                suffix = "%",
                isError = state.errorMusculo != null,
                errorMsg = state.errorMusculo
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = viewModel::guardar,
                enabled = !state.guardando,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Oro)
            ) {
                if (state.guardando) {
                    CircularProgressIndicator(color = FondoOscuro, strokeWidth = 2.dp)
                } else {
                    Text(
                        "GUARDAR MEDICIÓN",
                        style = MaterialTheme.typography.labelLarge,
                        color = FondoOscuro
                    )
                }
            }
        }
    }
}
