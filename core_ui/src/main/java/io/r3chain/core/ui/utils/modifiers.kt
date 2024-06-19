package io.r3chain.core.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/**
 * Текст-кнопка.
 */
fun Modifier.clickableLabel(
    paddingValues: PaddingValues = PaddingValues(vertical = 4.dp, horizontal = 8.dp),
    onClick: () -> Unit
) = this
    .clip(CircleShape)
    .clickable(
        role = Role.Button,
        onClick = onClick
    )
    .padding(paddingValues)
