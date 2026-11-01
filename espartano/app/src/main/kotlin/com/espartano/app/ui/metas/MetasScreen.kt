package com.espartano.app.ui.metas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.espartano.app.ui.components.EspartanoTextField
import com.espartano.app.ui.components.GoalProgressBar
import com.espartano.app.ui.components.SectionTitle
import com.espartano.app.ui.theme.FondoOscuro
import com.espartano.app.ui.theme.Naranja
import com.espartano.app.ui.theme.Oro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetasScreen(viewModel: MetasViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.snackMsg) {
        state.snackMsg?.let { snackbarHostState.showSnackbar(it); viewModel.dismissSnack() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Metas") },
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
            SectionTitle("Define tus objetivos")

            EspartanoTextField(
                value = state.pesoObjetivo,
                onValueChange = viewModel::onPesoObjetivoChange,
                label = "Peso objetivo",
                suffix = "kg"
            )

            EspartanoTextField(
                value = state.grasaObjetivo,
                onValueChange = viewModel::onGrasaObjetivoChange,
                label = "% Grasa objetivo",
                suffix = "%"
            )

            Button(
                onClick = viewModel::guardarMetas,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Oro)
            ) {
                Text("GUARDAR METAS", style = MaterialTheme.typography.labelLarge, color = FondoOscuro)
            }

            val pesoActual = state.ultimoPeso
            val pesoObj = state.pesoObjetivo.toFloatOrNull()
            val grasaActual = state.ultimaGrasa
            val grasaObj = state.grasaObjetivo.toFloatOrNull()

            if (pesoActual != null && pesoObj != null) {
                SectionTitle("Progreso actual")
                val progresoPeso = if (pesoActual <= pesoObj) 1f
                else (1f - (pesoActual - pesoObj) / pesoActual).coerceIn(0f, 1f)
                GoalProgressBar(
                    label = "Peso",
                    progreso = progresoPeso,
                    valorActual = "%.1f kg".format(pesoActual),
                    valorObjetivo = "%.1f kg".format(pesoObj),
                    color = Oro,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (grasaActual != null && grasaObj != null) {
                val progresoGrasa = if (grasaActual <= grasaObj) 1f
                else (1f - (grasaActual - grasaObj) / grasaActual).coerceIn(0f, 1f)
                GoalProgressBar(
                    label = "% Grasa",
                    progreso = progresoGrasa,
                    valorActual = "%.1f%%".format(grasaActual),
                    valorObjetivo = "%.1f%%".format(grasaObj),
                    color = Naranja,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}
