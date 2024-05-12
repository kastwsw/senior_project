package io.r3chain.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun R3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF5D60B4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE2DFFF),
    onPrimaryContainer = Color(0xFF15134A),
    inversePrimary = Color(0xFFC2C1FF),
    secondary = Color(0xFF576421),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDAEA98),
    onSecondaryContainer = Color(0xFF181E00),
    tertiary = Color(0xFF67558E),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFEADDFF),
    onTertiaryContainer = Color(0xFF220F46),
    background = Color(0xFFFCF8FF),
    onBackground = Color(0xFF1B1B21),
    surface = Color(0xFFF7F9FE),
    onSurface = Color(0xFF1B1B21),
    surfaceVariant = Color(0xFFE4E1EC),
    onSurfaceVariant = Color(0xFF47464F),
    surfaceTint = Color(0xFF595892),
    inverseSurface = Color(0xFF303036),
    inverseOnSurface = Color(0xFFEEF1F6),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF8B9198),
    outlineVariant = Color(0xFF41474D),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF39383F),
    surfaceContainer = Color(0xFF1F1F25),
    surfaceContainerHigh = Color(0xFF2A292F),
    surfaceContainerHighest = Color(0xFF35343A),
    surfaceContainerLow = Color(0xFF1B1B21),
    surfaceContainerLowest = Color(0xFF0E0E13),
    surfaceDim = Color(0xFF131318)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFC2C1FF),
    onPrimary = Color(0xFF2B2A60),
    primaryContainer = Color(0xFF414178),
    onPrimaryContainer = Color(0xFFE2DFFF),
    inversePrimary = Color(0xFF595892),
    secondary = Color(0xFFBECE7F),
    onSecondary = Color(0xFF2B3400),
    secondaryContainer = Color(0xFFCCDC8C),
    onSecondaryContainer = Color(0xFF384402),
    tertiary = Color(0xFFD1BCFD),
    onTertiary = Color(0xFF37265C),
    tertiaryContainer = Color(0xFF4E3D75),
    onTertiaryContainer = Color(0xFFEADDFF),
    background = Color(0xFF131318),
    onBackground = Color(0xFFE5E1E9),
    surface = Color(0xFF131318),
    onSurface = Color(0xFFDFE3E8),
    surfaceVariant = Color(0xFF47464F),
    onSurfaceVariant = Color(0xFFC1C7CE),
    surfaceTint = Color(0xFFC2C1FF),
    inverseSurface = Color(0xFFDFE3E8),
    inverseOnSurface = Color(0xFF303036),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF8B9198),
    outlineVariant = Color(0xFF41474D),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF39383F),
    surfaceContainer = Color(0xFF1F1F25),
    surfaceContainerHigh = Color(0xFF2A292F),
    surfaceContainerHighest = Color(0xFF35343A),
    surfaceContainerLow = Color(0xFF1B1B21),
    surfaceContainerLowest = Color(0xFF0E0E13),
    surfaceDim = Color(0xFF131318),
)
