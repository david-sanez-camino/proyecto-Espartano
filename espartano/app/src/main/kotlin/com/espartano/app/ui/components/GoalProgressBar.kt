package com.espartano.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.espartano.app.ui.theme.GrisLinea

@Composable
fun GoalProgressBar(
    label: String,
    progreso: Float,
    valorActual: String,
    valorObjetivo: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progreso.coerceIn(0f, 1f),
        animationSpec = tween(800),
        label = "progreso"
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.titleMedium)
            Text(
                "$valorActual → $valorObjetivo",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = GrisLinea
        )
        Text(
            text = "${(animatedProgress * 100).toInt()}% completado",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}
