package com.espartano.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espartano.app.ui.theme.SuperficieMedia
import com.espartano.app.ui.theme.TextoSecundario

@Composable
fun MetricCard(
    label: String,
    value: String,
    unit: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    delta: String? = null
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SuperficieMedia),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = TextoSecundario,
                letterSpacing = 1.sp
            )
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = accentColor
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                color = TextoSecundario
            )
            delta?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (it.startsWith("-")) Color(0xFF4CAF7D) else Color(0xFFCF6679)
                )
            }
        }
    }
}
