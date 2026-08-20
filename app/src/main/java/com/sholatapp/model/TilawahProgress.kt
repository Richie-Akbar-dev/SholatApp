package com.sholatapp.model

data class TilawahProgress(
    val date: String,
    val pagesRead: Int = 0,
    val startPage: Int = 1,
    val endPage: Int = 0
)

data class KhatamEstimate(
    val totalPagesRead: Int = 0,
    val pagesRemaining: Int = 0,
    val currentJuz: Int = 1,
    val currentPageInJuz: Int = 0,
    val daysActive: Int = 0,
    val avgPagesPerDay: Float = 0f,
    val estimatedDaysToKhatam: Int = 0,
    val estimatedKhatamDate: String = "-"
)
