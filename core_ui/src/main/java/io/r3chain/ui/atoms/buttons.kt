package io.r3chain.ui.atoms

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
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
    Button(
        modifier = Modifier
            .defaultMinSize(minWidth = 160.dp)
            .height(60.dp)
            .then(modifier),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text)
    }
}