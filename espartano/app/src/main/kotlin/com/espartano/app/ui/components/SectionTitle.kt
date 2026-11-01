package com.espartano.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.espartano.app.ui.theme.Oro

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Oro,
        letterSpacing = androidx.compose.ui.unit.TextUnit(1.5f, androidx.compose.ui.unit.TextUnitType.Sp),
        modifier = modifier.padding(bottom = 8.dp)
    )
}
