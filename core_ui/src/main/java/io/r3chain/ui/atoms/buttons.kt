package io.r3chain.ui.atoms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    FilledTonalButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .then(modifier),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text)
    }
}