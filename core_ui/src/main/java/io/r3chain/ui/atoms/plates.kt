package io.r3chain.ui.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorPlate(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onError,
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.error,
                shape = MaterialTheme.shapes.small
            )
            .then(modifier)
    )
}
