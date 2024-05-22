package io.r3chain.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ErrorPlate(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onErrorContainer,
        modifier = Modifier
            .then(modifier)
            .background(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}


@Composable
fun LoadingBox(
    modifier: Modifier = Modifier
) {
    // анимировать значение прозрачности от 0f до 1f
    val aniAlpha = remember {
        Animatable(0f)
    }
    // key фейковый, так как изменение анимации ни на что не завязанно
    LaunchedEffect(Unit) {
        aniAlpha.animateTo(
            targetValue = 0.8f,
            animationSpec = tween(
                durationMillis = 1200,
                delayMillis = 800,
                easing = LinearEasing
            )
        )
    }
    Box(
        modifier = Modifier
            .then(modifier)
            .graphicsLayer {
                alpha = aniAlpha.value
            },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            strokeWidth = 8.dp,
            color = MaterialTheme.colorScheme.tertiary,
            strokeCap = StrokeCap.Round
        )
    }
}


@Composable
fun ActionPlate(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = text,
                role = Role.Button,
                onClick = onClick
            )
            .padding(20.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}


@Composable
fun SwitchPlate(
    text: String,
    checked: Boolean,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClickLabel = text,
                enabled = enabled,
                role = Role.Button,
                onClick = {
                    onCheckedChange(!checked)
                }
            )
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 20.dp)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}


@Preview(
    name = "Demo"
)
@Composable
private fun PlatesPreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LoadingBox(modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp))
                ErrorPlate(text = "Have some error")
            }
        }
    }
}
