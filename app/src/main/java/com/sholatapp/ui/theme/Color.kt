package com.sholatapp.ui.theme
import androidx.compose.ui.graphics.Color

/**
 * Palet warna aplikasi — TEMA TERANG KONSISTEN.
 *
 * Catatan: objek ini sebelumnya bernama `DarkColors` dan bersifat dinamis
 * (mendukung Mode Gelap). Sejak v2.2 Mode Gelap dihapus karena tidak
 * dibutuhkan; nama objek dipertahankan demi kompatibilitas dengan seluruh
 * layar yang sudah ada, tetapi nilainya kini STatis palet terang.
 *
 * Hasilnya: seluruh layar (termasuk Beranda, Puasa, Tilawah, Kiblat,
 * Kalender yang sebelumnya hardcode) kini memakai satu sumber warna yang sama.
 */
object DarkColors {

    // Background colors
    val Background = Color(0xFFF5F5F0)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFE4EDE4)
    val CardBackground = Color(0xFFFFFFFF)

    // Primary - Islamic deep green tones
    val Primary = Color(0xFF1B4D3E)
    val PrimaryLight = Color(0xFF2E7D32)
    val PrimaryDark = Color(0xFF143A2E)
    val PrimaryContainer = Color(0xFFE8F5E9)

    // Accent - Gold
    val Gold = Color(0xFFA97C0E)
    val GoldLight = Color(0xFFD4A843)
    val GoldDark = Color(0xFF8B6508)

    // Text colors
    val TextPrimary = Color(0xFF1A1A1A)
    val TextSecondary = Color(0xFF4A6355)
    val TextTertiary = Color(0xFF7D9184)
    val TextOnPrimary = Color(0xFFFFFFFF)

    // Warna teks khusus DI ATAS header hijau tua (PrimaryDark) — menjaga kontras
    val HeaderSubtitle = Color(0xFFBFDCC3)
    val HeaderPlaceholder = Color(0xFF9EC4A6)

    // Status colors
    val ActivePrayer = Color(0xFF2E7D32)
    val NextPrayer = Color(0xFFA97C0E)
    val PassedPrayer = Color(0xFFB3B8B3)

    // Border and divider
    val Border = Color(0xFFDCE5DC)
    val Divider = Color(0xFFE2EAE2)

    // Special
    val QiblaAccent = Color(0xFFA97C0E)
    val CountdownBg = Color(0xFFE8F5E9)
    val ShadowColor = Color(0x1A000000)
}
