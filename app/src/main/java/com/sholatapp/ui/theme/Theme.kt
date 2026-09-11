package com.sholatapp.ui.theme

import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Tema aplikasi — sejak v2.2 memakai TEMA TERANG KONSISTEN (Mode Gelap dihapus).
 *
 * Semua layar kini memakai satu skema Material 3 warna terang yang dibangun
 * dari palet `DarkColors` (objek palet terang). Header/halaman khusus seperti
 * SalatScreen tetap memakai latar imersif sendiri sebagai gaya desain,
 * bukan sebagai mode gelap.
 */
@Composable
fun SholatAppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
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
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
