package com.sholatapp.model

/**
 * Represents a dzikir item with target count.
 *
 * v2.5: + [title] — nama singkat yang tampil sebagai chip kecil di kartu
 * (mis. "Ayat Kursi", "Sayyidul Istighfar") dan sebagai judul di pop up fokus.
 */
data class DzikirItem(
    val id: String,
    val title: String = "",
    val arabic: String,
    val latin: String,
    val translation: String,
    val targetCount: Int,
    val category: DzikirCategory
)

enum class DzikirCategory(val displayName: String, val shortLabel: String) {
    PAGI("Dzikir Pagi", "Pagi"),
    PETANG("Dzikir Petang", "Petang"),
    SETELAH_SHOLAT("Setelah Sholat", "Setelah Sholat"),
    UMUM("Dzikir Umum", "Umum"),
    // v2.10: kategori pintu — TIDAK memiliki item DzikirItem. Saat chip ini
    // aktif, badan halaman Zikir diambil alih DoaContent()/AsmaulHusnaContent()
    // (pindahan dari halaman "Lainnya" yang dihapus, keputusan user v2.10).
    DOA("Doa Harian", "Doa"),
    ASMAUL_HUSNA("Asmaul Husna", "Asmaul Husna")
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
