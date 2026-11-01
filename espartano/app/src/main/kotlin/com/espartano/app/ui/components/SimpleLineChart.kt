package com.espartano.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SimpleLineChart(
    data: List<Float>,
    lineColor: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 3f,
    showDots: Boolean = true
) {
    if (data.size < 2) return

    Canvas(modifier = modifier.fillMaxSize()) {
        val minVal = data.min()
        val maxVal = data.max()
        // When all values are equal, use a small range so the line appears centered
        val range = if (maxVal - minVal < 0.001f) 1f else (maxVal - minVal)

        val stepX = size.width / (data.size - 1).toFloat()
        val paddingY = 12.dp.toPx()
        val usableH = size.height - paddingY * 2

        // Higher value = smaller y (higher position on screen)
        // minVal → bottom (paddingY + usableH), maxVal → top (paddingY)
        fun yFor(v: Float): Float {
            val normalized = (v - minVal) / range  // 0..1 (0=min, 1=max)
            return paddingY + usableH * (1f - normalized) // invert: min at bottom
        }

        val path = Path().apply {
            data.forEachIndexed { i, v ->
                val x = stepX * i
                val y = yFor(v)
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
        }

        drawPath(path, color = lineColor, style = Stroke(width = strokeWidth))

        if (showDots) {
            data.forEachIndexed { i, v ->
                drawCircle(
                    color = lineColor,
                    radius = 5.dp.toPx(),
                    center = Offset(stepX * i, yFor(v))
                )
                // White inner dot for visibility
                drawCircle(
                    color = Color.Black.copy(alpha = 0.3f),
                    radius = 2.5.dp.toPx(),
                    center = Offset(stepX * i, yFor(v))
                )
            }
        }
    }
}
