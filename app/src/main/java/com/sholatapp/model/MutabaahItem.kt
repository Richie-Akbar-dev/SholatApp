package com.sholatapp.model

enum class MutabaahCategory(val displayName: String, val icon: String) {
    SHOLAT_FARDHU("Sholat Fardhu", "1"),
    SHOLAT_SUNNAH("Sholat Sunnah", "2"),
    QURAN("Al-Qur'an", "3"),
    DZIKIR("Dzikir & Doa", "4"),
    AKHLAK("Akhlak", "5")
}

data class MutabaahItem(
    val id: String,
    val title: String,
    val category: MutabaahCategory,
    val isSunnah: Boolean = false
)

// Stores a day's completed mutaba'ah item IDs
data class MutabaahRecord(
    val date: String, // yyyy-MM-dd
    val completedIds: Set<String> = emptySet()
) {
    val totalItems: Int get() = completedIds.size
}
