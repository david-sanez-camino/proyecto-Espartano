package com.espartano.app.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.espartano.app.R
import com.espartano.app.ui.components.GoalProgressBar
import com.espartano.app.ui.components.MetricCard
import com.espartano.app.ui.components.SectionTitle
import com.espartano.app.ui.components.SimpleLineChart
import com.espartano.app.ui.theme.Azul
import com.espartano.app.ui.theme.Naranja
import com.espartano.app.ui.theme.Oro
import com.espartano.app.ui.theme.Rojo
import com.espartano.app.ui.theme.TextoSecundario
import com.espartano.app.ui.theme.Verde

@Composable
fun DashboardScreen(
    onNuevaMedicion: () -> Unit,
    onVerHistorial: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevaMedicion,
                containerColor = Oro,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva medición")
            }
        }
    ) { innerPadding ->

        if (!state.perfilConfigurado) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_escudo_espartano),
                        contentDescription = "Escudo Espartano",
                        modifier = Modifier.size(140.dp)
                    )
                    Text("ESPARTANO", style = MaterialTheme.typography.headlineLarge, color = Oro)
                    Text(
                        "Configura tu perfil en Ajustes\npara comenzar el seguimiento",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = TextoSecundario
                    )
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("ESPARTANO", style = MaterialTheme.typography.headlineLarge, color = Oro)
                    state.perfil?.let {
                        Text(
                            "${it.edad} años · ${it.alturaCm.toInt()} cm",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                    }
                }
                Image(
                    painter = painterResource(R.drawable.ic_escudo_espartano),
                    contentDescription = "Escudo",
                    modifier = Modifier.size(72.dp)
                )
            }

            state.ultimaMedicion?.let { m ->
                val prev = state.penultimaMedicion
                SectionTitle("Última medición — ${m.fecha}")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val deltaP = prev?.let { d ->
                        val diff = m.pesoKg - d.pesoKg
                        (if (diff >= 0) "+%.1f".format(diff) else "%.1f".format(diff)) + " kg"
                    }
                    MetricCard(
                        label = "Peso", value = "%.1f".format(m.pesoKg), unit = "kg",
                        accentColor = Oro, delta = deltaP, modifier = Modifier.weight(1f)
                    )
                    val deltaG = prev?.let { d ->
                        val diff = m.grasaPct - d.grasaPct
                        (if (diff >= 0) "+%.1f".format(diff) else "%.1f".format(diff)) + " %"
                    }
                    MetricCard(
                        label = "Grasa", value = "%.1f".format(m.grasaPct), unit = "%",
                        accentColor = Naranja, delta = deltaG, modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val deltaM = prev?.let { d ->
                        val diff = m.musculoPct - d.musculoPct
                        (if (diff >= 0) "+%.1f".format(diff) else "%.1f".format(diff)) + " %"
                    }
                    MetricCard(
                        label = "Músculo", value = "%.1f".format(m.musculoPct), unit = "%",
                        accentColor = Verde, delta = deltaM, modifier = Modifier.weight(1f)
                    )
                    state.perfil?.let { p ->
                        val imcVal = m.imc(p.alturaCm)
                        MetricCard(
                            label = "IMC", value = "%.1f".format(imcVal),
                            unit = clasificarImc(imcVal), accentColor = Azul,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } ?: Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_escudo_espartano),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )
                }
                Text(
                    "Pulsa + para registrar tu primera medición",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
            }

            if (state.recientes.size >= 2) {
                SectionTitle("Evolución — Peso (kg)")
                SimpleLineChart(
                    data = state.recientes.map { it.pesoKg },
                    lineColor = Oro,
                    modifier = Modifier.fillMaxWidth().height(80.dp)
                )
            }

            val pesoP = state.pesoObjetivoProgreso
            val grasaP = state.grasaObjetivoProgreso
            val obj = state.perfil

            if (pesoP != null && obj?.pesoObjetivoKg != null && state.ultimaMedicion != null) {
                SectionTitle("Progreso hacia metas")
                GoalProgressBar(
                    label = "Peso objetivo",
                    progreso = pesoP,
                    valorActual = "%.1f kg".format(state.ultimaMedicion!!.pesoKg),
                    valorObjetivo = "%.1f kg".format(obj.pesoObjetivoKg),
                    color = Oro,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (grasaP != null && obj?.grasaObjetivoPct != null && state.ultimaMedicion != null) {
                GoalProgressBar(
                    label = "Grasa objetivo",
                    progreso = grasaP,
                    valorActual = "%.1f%%".format(state.ultimaMedicion!!.grasaPct),
                    valorObjetivo = "%.1f%%".format(obj.grasaObjetivoPct),
                    color = Naranja,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            TextButton(
                onClick = onVerHistorial,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.List, contentDescription = null, tint = TextoSecundario)
                Text(
                    "  Ver historial completo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

private fun clasificarImc(imc: Float): String = when {
    imc < 18.5f -> "Bajo peso"
    imc < 25f -> "Normal"
    imc < 30f -> "Sobrepeso"
    else -> "Obesidad"
}
