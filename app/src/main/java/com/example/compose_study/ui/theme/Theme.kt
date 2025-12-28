package com.example.compose_study.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TossBlue,
    onPrimary = TossSurface,
    primaryContainer = TossBlueDark,
    onPrimaryContainer = TossSurface,
    secondary = TossSurfaceAlt,
    onSecondary = TossSurface,
    tertiary = TossSuccess,
    onTertiary = TossSurface,
    background = Color(0xFF0C111B),
    onBackground = TossSurface,
    surface = Color(0xFF121826),
    onSurface = TossSurface,
    surfaceVariant = Color(0xFF1B2436),
    onSurfaceVariant = TossTextMuted,
    outline = Color(0xFF2A344A)
)

private val LightColorScheme = lightColorScheme(
    primary = TossBlue,
    onPrimary = TossSurface,
    primaryContainer = TossBlueSoft,
    onPrimaryContainer = TossNavy,
    secondary = TossSurfaceAlt,
    onSecondary = TossNavy,
    tertiary = TossSuccess,
    onTertiary = TossSurface,
    background = TossBackground,
    onBackground = TossNavy,
    surface = TossSurface,
    onSurface = TossNavy,
    surfaceVariant = TossSurfaceAlt,
    onSurfaceVariant = TossTextMuted,
    outline = TossBorder

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ComposestudyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
