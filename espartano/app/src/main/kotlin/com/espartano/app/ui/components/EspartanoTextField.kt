package com.espartano.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.espartano.app.ui.theme.GrisLinea
import com.espartano.app.ui.theme.Oro
import com.espartano.app.ui.theme.SuperficieMedia

@Composable
fun EspartanoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMsg: String? = null,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        suffix = suffix?.let { { Text(it) } },
        isError = isError,
        supportingText = if (isError && errorMsg != null) {
            { Text(errorMsg, color = MaterialTheme.colorScheme.error) }
        } else null,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = if (keyboardType == KeyboardType.Text) KeyboardCapitalization.Sentences else KeyboardCapitalization.None
        ),
        singleLine = true,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Oro,
            unfocusedBorderColor = GrisLinea,
            focusedLabelColor = Oro,
            unfocusedContainerColor = SuperficieMedia,
            focusedContainerColor = SuperficieMedia,
            cursorColor = Oro
        )
    )
}
