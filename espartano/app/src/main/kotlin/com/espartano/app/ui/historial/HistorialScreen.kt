package com.espartano.app.ui.historial

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.espartano.app.domain.model.Medicion
import com.espartano.app.domain.model.Perfil
import com.espartano.app.ui.theme.FondoOscuro
import com.espartano.app.ui.theme.Naranja
import com.espartano.app.ui.theme.Oro
import com.espartano.app.ui.theme.Rojo
import com.espartano.app.ui.theme.SuperficieMedia
import com.espartano.app.ui.theme.TextoSecundario
import com.espartano.app.ui.theme.Verde

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(viewModel: HistorialViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoOscuro,
                    titleContentColor = Oro
                )
            )
        }
    ) { innerPadding ->
        if (state.mediciones.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay mediciones registradas.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.mediciones, key = { it.id }) { m ->
                    MedicionItem(
                        medicion = m,
                        perfil = state.perfil,
                        onEliminar = { viewModel.eliminar(m) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MedicionItem(medicion: Medicion, perfil: Perfil?, onEliminar: () -> Unit) {
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
                    medicion.fecha.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Oro
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ChipMetric("${medicion.pesoKg} kg", Oro)
                    ChipMetric("%.1f%% G".format(medicion.grasaPct), Naranja)
                    ChipMetric("%.1f%% M".format(medicion.musculoPct), Verde)
                }
                perfil?.let {
                    Text(
                        "IMC: ${"%.1f".format(medicion.imc(it.alturaCm))}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Rojo)
            }
        }
    }
}

@Composable
private fun ChipMetric(text: String, color: androidx.compose.ui.graphics.Color) {
    Text(text, style = MaterialTheme.typography.bodyMedium, color = color)
}
