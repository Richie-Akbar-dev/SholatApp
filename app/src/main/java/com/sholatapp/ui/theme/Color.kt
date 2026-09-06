package com.sholatapp.ui.theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * State tema global aplikasi.
 * Diubah lewat toggle "Mode Gelap" di Pengaturan dan dipersist ke
 * SharedPreferences ("sholatapp_prefs" -> "is_dark_theme").
 * Semua warna di bawah otomatis mengikuti nilai ini karena dibaca
 * sebagai Compose State di dalam composable.
 */
object AppThemeState {
    var isDark by mutableStateOf(true)
}

/**
 * Palet warna dinamis aplikasi.
 *
 * Nama objek dipertahankan sebagai `DarkColors` demi kompatibilitas dengan
 * seluruh layar yang sudah ada, tetapi nilainya kini DINAMIS: ketika
 * AppThemeState.isDark = false, seluruh warna beralih ke palet terang.
 */
object DarkColors {

    // Background colors
    val Background get() = if (AppThemeState.isDark) Color(0xFF0A1A0A) else Color(0xFFF5F5F0)
    val Surface get() = if (AppThemeState.isDark) Color(0xFF112211) else Color(0xFFFFFFFF)
    val SurfaceVariant get() = if (AppThemeState.isDark) Color(0xFF1A2E1A) else Color(0xFFE4EDE4)
    val CardBackground get() = if (AppThemeState.isDark) Color(0xFF152615) else Color(0xFFFFFFFF)

    // Primary - Islamic green tones
    val Primary get() = if (AppThemeState.isDark) Color(0xFF2E7D32) else Color(0xFF1B4D3E)
    val PrimaryLight get() = if (AppThemeState.isDark) Color(0xFF4CAF50) else Color(0xFF2E7D32)
    val PrimaryDark get() = if (AppThemeState.isDark) Color(0xFF1B5E20) else Color(0xFF143A2E)
    val PrimaryContainer get() = if (AppThemeState.isDark) Color(0xFF1A3A1A) else Color(0xFFE8F5E9)

    // Accent - Gold
    val Gold get() = if (AppThemeState.isDark) Color(0xFFD4A843) else Color(0xFFA97C0E)
    val GoldLight get() = if (AppThemeState.isDark) Color(0xFFFFD54F) else Color(0xFFD4A843)
    val GoldDark get() = if (AppThemeState.isDark) Color(0xFFB8860B) else Color(0xFF8B6508)

    // Text colors
    val TextPrimary get() = if (AppThemeState.isDark) Color(0xFFE8F5E9) else Color(0xFF1A1A1A)
    val TextSecondary get() = if (AppThemeState.isDark) Color(0xFFA5D6A7) else Color(0xFF4A6355)
    val TextTertiary get() = if (AppThemeState.isDark) Color(0xFF6B9B6B) else Color(0xFF7D9184)
    val TextOnPrimary get() = Color(0xFFFFFFFF)

    // Status colors
    val ActivePrayer get() = if (AppThemeState.isDark) Color(0xFF4CAF50) else Color(0xFF2E7D32)
    val NextPrayer get() = if (AppThemeState.isDark) Color(0xFFD4A843) else Color(0xFFA97C0E)
    val PassedPrayer get() = if (AppThemeState.isDark) Color(0xFF4A4A4A) else Color(0xFFB3B8B3)

    // Border and divider
    val Border get() = if (AppThemeState.isDark) Color(0xFF2A3F2A) else Color(0xFFDCE5DC)
    val Divider get() = if (AppThemeState.isDark) Color(0xFF1E3018) else Color(0xFFE2EAE2)

    // Special
    val QiblaAccent get() = if (AppThemeState.isDark) Color(0xFFD4A843) else Color(0xFFA97C0E)
    val CountdownBg get() = if (AppThemeState.isDark) Color(0xFF0D2B0D) else Color(0xFFE8F5E9)
    val ShadowColor get() = if (AppThemeState.isDark) Color(0x40000000) else Color(0x1A000000)
}
