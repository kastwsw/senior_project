package io.r3chain.core.ui.components

import android.content.res.Configuration
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.r3chain.core.ui.theme.R3Theme


@Composable
fun ScreenHeader(
    title: String,
    backAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
    ) {
        backAction?.also { action ->
            IconButton(onClick = action) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 6.dp)
        )
    }
}


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
fun ActionPlate(
    title: String,
    description: String? = null,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = title,
                role = Role.Button,
                enabled = enabled,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (leadingContent != null) leadingContent()
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            description?.also {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (trailingContent != null) trailingContent()
    }
}

@Composable
fun IconActionPlate(
    icon: ImageVector,
    title: String,
    description: String? = null,
    onClick: () -> Unit
) {
    ActionPlate(
        title = title,
        description = description,
        onClick = onClick,
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}


@Composable
fun SwitchPlate(
    text: String,
    checked: Boolean,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onCheckedChange: (Boolean) -> Unit
) {
    ActionPlate(
        title = text,
        enabled = enabled,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                interactionSource = interactionSource
            )
        },
        onClick = {
            onCheckedChange(!checked)
        }
    )
}

@Preview(
    name = "Demo Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun PreviewLight() {
    Demo()
}


@Preview(
    name = "Demo Night",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun PreviewNight() {
    Demo()
}

@Composable
private fun Demo() {
    R3Theme {
        Surface {
            Column {
                ScreenHeader(title = "Screen title") {}

                IconActionPlate(
                    title = "IconActionPlate",
                    icon = Icons.Outlined.PhotoCamera
                ) {}
                IconActionPlate(
                    title = "IconActionPlate",
                    description = "Description text",
                    icon = Icons.Outlined.LocalShipping
                ) {}

                SwitchPlate(text = "SwitchPlate", checked = true) {}

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LoadingBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    )
                    ErrorPlate(text = "Have some error")
                }
            }
        }
    }
}
