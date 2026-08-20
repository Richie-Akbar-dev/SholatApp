package com.sholatapp.model

/**
 * Represents a daily prayer (doa) with its Arabic text, transliteration, and meaning.
 */
data class DoaItem(
    val id: String,
    val title: String,
    val arabic: String,
    val latin: String,
    val translation: String,
    val category: DoaCategory,
    val isFavorite: Boolean = false
)

enum class DoaCategory(val displayName: String, val icon: String) {
    BANGUN_TIDUR("Bangun Tidur", ""),
    MASJID("Masjid", ""),
    MAKAN("Makan & Minum", ""),
    RUMAH("Rumah & Keluarga", ""),
    PERJALANAN("Perjalanan", ""),
    SHOLAT("Sholat", ""),
    HARIAN("Doa Harian", ""),
    LAINNYA("Lainnya", "")
}
