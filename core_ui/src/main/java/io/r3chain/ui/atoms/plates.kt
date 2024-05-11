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
        color = MaterialTheme.colorScheme.onError,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 20.dp)
            .background(
                color = MaterialTheme.colorScheme.error,
                shape = CircleShape
            )
            .padding(16.dp)
            .then(modifier)
    )
}
