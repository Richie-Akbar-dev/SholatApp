package com.sholatapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkColors.Primary,
    onPrimary = DarkColors.TextOnPrimary,
    primaryContainer = DarkColors.PrimaryContainer,
    onPrimaryContainer = DarkColors.PrimaryLight,
    secondary = DarkColors.Gold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF3D2E0A),
    onSecondaryContainer = DarkColors.GoldLight,
    tertiary = DarkColors.PrimaryLight,
    onTertiary = Color.Black,
    tertiaryContainer = DarkColors.PrimaryDark,
    onTertiaryContainer = DarkColors.TextPrimary,
    background = DarkColors.Background,
    onBackground = DarkColors.TextPrimary,
    surface = DarkColors.Surface,
    onSurface = DarkColors.TextPrimary,
    surfaceVariant = DarkColors.SurfaceVariant,
    onSurfaceVariant = DarkColors.TextSecondary,
    outline = DarkColors.Border,
    outlineVariant = DarkColors.Divider,
    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color(0xFFFFDAD6),
)

@Composable
fun SholatAppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkColors.Background.toArgb()
            window.navigationBarColor = DarkColors.Background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}