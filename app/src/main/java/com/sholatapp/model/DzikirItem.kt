package com.sholatapp.model

/**
 * Represents a dzikir item with target count.
 */
data class DzikirItem(
    val id: String,
    val arabic: String,
    val latin: String,
    val translation: String,
    val targetCount: Int,
    val category: DzikirCategory
)

enum class DzikirCategory(val displayName: String) {
    PAGI("Dzikir Pagi"),
    PETANG("Dzikir Petang"),
    SETELAH_SHOLAT("Setelah Sholat"),
    UMUM("Dzikir Umum")
}

/**
 * Tracks daily progress for a dzikir item.
 */
data class DzikirProgress(
    val dzikirId: String,
    val currentCount: Int,
    val targetCount: Int,
    val isCompleted: Boolean
) {
    val progress: Float
        get() = if (targetCount > 0) currentCount.toFloat() / targetCount else 0f
}
