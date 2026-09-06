package com.sholatapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Tema aplikasi — sekarang mendukung Mode Gelap & Mode Terang.
 * Skema dibangun ulang setiap kali AppThemeState.isDark berubah,
 * sehingga seluruh layar otomatis ikut berpindah tema.
 */
@Composable
fun SholatAppTheme(
    content: @Composable () -> Unit
) {
    // Membaca state agar recompose saat toggle tema berubah
    val isDark = AppThemeState.isDark

    val colorScheme = if (isDark) darkColorScheme(
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
    ) else lightColorScheme(
        primary = DarkColors.Primary,
        onPrimary = DarkColors.TextOnPrimary,
        primaryContainer = DarkColors.PrimaryContainer,
        onPrimaryContainer = DarkColors.PrimaryDark,
        secondary = DarkColors.Gold,
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFF0E6C8),
        onSecondaryContainer = Color(0xFF5C4308),
        tertiary = DarkColors.PrimaryLight,
        onTertiary = Color.White,
        tertiaryContainer = DarkColors.PrimaryContainer,
        onTertiaryContainer = DarkColors.PrimaryDark,
        background = DarkColors.Background,
        onBackground = DarkColors.TextPrimary,
        surface = DarkColors.Surface,
        onSurface = DarkColors.TextPrimary,
        surfaceVariant = DarkColors.SurfaceVariant,
        onSurfaceVariant = DarkColors.TextSecondary,
        outline = DarkColors.Border,
        outlineVariant = DarkColors.Divider,
        error = Color(0xFFB3261E),
        onError = Color.White,
        errorContainer = Color(0xFFF9DEDC),
        onErrorContainer = Color(0xFF410E0B),
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkColors.Background.toArgb()
            window.navigationBarColor = DarkColors.Background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
