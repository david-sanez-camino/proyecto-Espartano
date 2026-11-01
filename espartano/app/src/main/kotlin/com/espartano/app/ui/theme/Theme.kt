package com.espartano.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EspartanoDarkColors = darkColorScheme(
    primary = Oro,
    onPrimary = FondoOscuro,
    primaryContainer = OroOscuro,
    onPrimaryContainer = OroClaro,
    secondary = OroClaro,
    onSecondary = FondoOscuro,
    background = FondoOscuro,
    onBackground = TextoPrimario,
    surface = SuperficieOscura,
    onSurface = TextoPrimario,
    surfaceVariant = SuperficieMedia,
    onSurfaceVariant = TextoSecundario,
    error = Rojo,
    onError = FondoOscuro,
    outline = GrisLinea,
    outlineVariant = SuperficieAlta
)

@Composable
fun EspartanoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EspartanoDarkColors,
        typography = EspartanoTypography,
        content = content
    )
}
