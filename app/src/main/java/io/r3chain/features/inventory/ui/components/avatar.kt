package io.r3chain.features.inventory.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.valentinilk.shimmer.shimmer
import io.r3chain.data.vo.ResourceVO
import io.r3chain.data.vo.UserVO

@Composable
fun UserAvatar(
    user: UserVO,
    picture: ResourceVO? = null,
    hasEditSymbol: Boolean = false,
    size: Dp = 100.dp,
    letterStyle: TextStyle = MaterialTheme.typography.displayMedium,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clickable(
                indication = rememberRipple(bounded = true, radius = size / 2),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // картинка или буква
        if (user.imageResourceID > 0) {
            // есть изображение
            if (picture?.posterLink?.isNotBlank() == true) {
                // загружено
                Image(
                    painter = rememberAsyncImagePainter(picture.posterLink),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(shape = CircleShape)
                )
            } else {
                // индикатор загрузки
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .shimmer()
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        )
                )
            }
        } else {
            // первая буква имени
            user.firstName.firstOrNull()?.toString()?.also {
                Text(
                    text = it,
                    style = letterStyle,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // символ редактирования
        if (hasEditSymbol) Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 4.dp)
                .size(24.dp)
                .background(
                    color = MaterialTheme.colorScheme.inverseSurface,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }
}