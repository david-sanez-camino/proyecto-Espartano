package com.espartano.app.ui.graficos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.espartano.app.ui.components.SimpleLineChart
import com.espartano.app.ui.theme.Azul
import com.espartano.app.ui.theme.FondoOscuro
import com.espartano.app.ui.theme.GrisLinea
import com.espartano.app.ui.theme.Naranja
import com.espartano.app.ui.theme.Oro
import com.espartano.app.ui.theme.SuperficieMedia
import com.espartano.app.ui.theme.Verde

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraficosScreen(viewModel: GraficosViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gráficos") },
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Métrica", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricaGrafico.entries.forEach { m ->
                    FilterChip(
                        selected = state.metrica == m,
                        onClick = { viewModel.setMetrica(m) },
                        label = { Text(m.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Oro,
                            selectedLabelColor = FondoOscuro,
                            containerColor = SuperficieMedia
                        )
                    )
                }
            }

            Text("Período", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RangoFechas.entries.forEach { r ->
                    FilterChip(
                        selected = state.rango == r,
                        onClick = { viewModel.setRango(r) },
                        label = { Text(r.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Oro,
                            selectedLabelColor = FondoOscuro,
                            containerColor = SuperficieMedia
                        )
                    )
                }
            }

            val datos = state.valoresGrafico
            if (datos.size < 2) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Se necesitan al menos 2 mediciones en el período seleccionado.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                val color = when (state.metrica) {
                    MetricaGrafico.PESO -> Oro
                    MetricaGrafico.GRASA -> Naranja
                    MetricaGrafico.MUSCULO -> Verde
                    MetricaGrafico.IMC -> Azul
                }
                Text(
                    "${state.metrica.label}: ${datos.last().let { "%.1f".format(it) }}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = color
                )
                SimpleLineChart(
                    data = datos,
                    lineColor = color,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                val min = datos.min()
                val max = datos.max()
                val avg = datos.average().toFloat()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatLabel("Mín", "%.1f".format(min))
                    StatLabel("Máx", "%.1f".format(max))
                    StatLabel("Media", "%.1f".format(avg))
                }
            }
        }
    }
}

@Composable
private fun StatLabel(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text(value, style = MaterialTheme.typography.titleLarge, color = Oro)
    }
}
