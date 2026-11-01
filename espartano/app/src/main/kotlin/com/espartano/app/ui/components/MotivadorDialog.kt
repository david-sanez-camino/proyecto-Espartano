package com.espartano.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.espartano.app.domain.usecase.TipoMensaje
import com.espartano.app.ui.theme.Oro
import com.espartano.app.ui.theme.Rojo
import com.espartano.app.ui.theme.SuperficieMedia
import com.espartano.app.ui.theme.Verde

@Composable
fun MotivadorDialog(
    mensaje: String,
    tipo: TipoMensaje,
    onDismiss: () -> Unit
) {
    val accentColor = when (tipo) {
        TipoMensaje.MEJORA -> Verde
        TipoMensaje.EMPEORA -> Rojo
        TipoMensaje.ESTANCAMIENTO -> Oro
        TipoMensaje.PRIMERA_VEZ -> Oro
    }

    val emoji = when (tipo) {
        TipoMensaje.MEJORA -> "🏆"
        TipoMensaje.EMPEORA -> "💀"
        TipoMensaje.ESTANCAMIENTO -> "🔥"
        TipoMensaje.PRIMERA_VEZ -> "🏛️"
    }

    val titulo = when (tipo) {
        TipoMensaje.MEJORA -> "VICTORIA"
        TipoMensaje.EMPEORA -> "DERROTA"
        TipoMensaje.ESTANCAMIENTO -> "ENTRENADOR ESPARTANO"
        TipoMensaje.PRIMERA_VEZ -> "ESPARTANO"
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SuperficieMedia),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(emoji, style = MaterialTheme.typography.displayLarge)
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    letterSpacing = androidx.compose.ui.unit.TextUnit(1.5f, androidx.compose.ui.unit.TextUnitType.Sp)
                )
                Text(
                    text = "\"$mensaje\"",
                    style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("ENTENDIDO", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
