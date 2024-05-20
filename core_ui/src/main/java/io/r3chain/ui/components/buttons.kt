package io.r3chain.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.r3chain.ui.theme.R3Theme

enum class ButtonStyle {
    PRIMARY, SECONDARY, TERTIARY
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonStyle: ButtonStyle = ButtonStyle.PRIMARY,
    onClick: () -> Unit
) {
    val colors = when (buttonStyle) {
        ButtonStyle.PRIMARY ->
            MaterialTheme.colorScheme.onPrimary to MaterialTheme.colorScheme.primary

        ButtonStyle.SECONDARY ->
            MaterialTheme.colorScheme.onSecondaryContainer to MaterialTheme.colorScheme.secondaryContainer

        ButtonStyle.TERTIARY ->
            MaterialTheme.colorScheme.onTertiaryContainer to MaterialTheme.colorScheme.tertiaryContainer
    }
    Button(
        modifier = Modifier
            .defaultMinSize(minWidth = 160.dp)
            .height(60.dp)
            .then(modifier),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            contentColor = colors.first,
            containerColor = colors.second
        ),
        onClick = onClick
    ) {
        Text(text = text)
    }
}


@Composable
fun LinkButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    TextButton(
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

@Preview(
    name = "Demo"
)
@Composable
private fun ButtonsPreview() {
    R3Theme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PrimaryButton(text = "Primary") {}
                PrimaryButton(text = "Secondary", buttonStyle = ButtonStyle.SECONDARY) {}
                LinkButton(text = "Link button") {}
            }
        }
    }
}