package com.sholatapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sholatapp.model.KhatamEstimate
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

data class TilawahUiState(
    val todayPages: Int = 0,
    val totalPagesRead: Int = 0,
    val khatamCount: Int = 0,
    val currentJuz: Int = 1,
    val currentPage: Int = 0,
    val juzProgress: Float = 0f,
    val estimate: KhatamEstimate = KhatamEstimate(),
    val last7Days: List<Pair<String, Int>> = emptyList()
)

class TilawahViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TOTAL_PAGES = 604
        const val PAGES_PER_JUZ = 20
    }

    private val prefs = application.getSharedPreferences("tilawah_prefs", 0)
    private val _uiState = MutableStateFlow(TilawahUiState())
    val uiState: StateFlow<TilawahUiState> = _uiState.asStateFlow()

    init { loadToday() }

    fun loadToday() {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val todayPages = prefs.getInt(today, 0)
        val totalPages = prefs.getInt("total_pages_read", 0)
        val khatamCount = prefs.getInt("khatam_count", 0)
        val currentPage = totalPages % TOTAL_PAGES
        val currentJuz = (currentPage / PAGES_PER_JUZ) + 1
        val juzProgress = ((currentPage % PAGES_PER_JUZ).toFloat() / PAGES_PER_JUZ) * 100f
        val estimate = calculateEstimate(totalPages)
        val last7 = loadLast7Days()

        _uiState.update {
            it.copy(
                todayPages = todayPages, totalPagesRead = totalPages,
                khatamCount = khatamCount, currentJuz = currentJuz.coerceIn(1, 30),
                currentPage = if (currentPage == 0 && totalPages > 0) TOTAL_PAGES else currentPage,
                juzProgress = juzProgress, estimate = estimate, last7Days = last7
            )
        }
    }

    fun addPages(count: Int) {
        if (count <= 0) return
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val currentToday = prefs.getInt(today, 0)
        val currentTotal = prefs.getInt("total_pages_read", 0)
        val newTotal = currentTotal + count
        val khatamCount = newTotal / TOTAL_PAGES

        prefs.edit()
            .putInt(today, currentToday + count)
            .putInt("total_pages_read", newTotal)
            .putInt("khatam_count", khatamCount)
            .apply()
        loadToday()
    }

    fun resetToday() {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        prefs.edit().remove(today).apply()
        loadToday()
    }

    private fun calculateEstimate(totalPages: Int): KhatamEstimate {
        val currentPage = totalPages % TOTAL_PAGES
        val remaining = if (currentPage == 0 && totalPages > 0) 0 else TOTAL_PAGES - currentPage
        val currentJuz = (currentPage / PAGES_PER_JUZ).coerceIn(1, 30)
        val pageInJuz = currentPage % PAGES_PER_JUZ

        // Khatam complete
        if (remaining == 0 && totalPages > 0) {
            return KhatamEstimate(
                totalPagesRead = totalPages, pagesRemaining = 0,
                currentJuz = 30, currentPageInJuz = 0,
                daysActive = 0, avgPagesPerDay = 0f,
                estimatedDaysToKhatam = 0, estimatedKhatamDate = "Alhamdulillah!"
            )
        }

        var totalDays = 0
        var totalPagesRead = 0
        for (i in 0 until 30) {
            val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -i) }
            val key = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
            val pages = prefs.getInt(key, 0)
            if (pages > 0) {
                totalDays++
                totalPagesRead += pages
            }
        }
        val avg = if (totalDays > 0) totalPagesRead.toFloat() / totalDays else 0f
        val daysToKhatam = if (avg > 0) (remaining / avg).toInt().coerceAtLeast(1) else 0
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, daysToKhatam) }
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))

        return KhatamEstimate(
            totalPagesRead = totalPages, pagesRemaining = remaining,
            currentJuz = currentJuz, currentPageInJuz = pageInJuz,
            daysActive = totalDays, avgPagesPerDay = avg,
            estimatedDaysToKhatam = daysToKhatam,
            estimatedKhatamDate = if (avg > 0) sdf.format(cal.time) else "-"
        )
    }

    private fun loadLast7Days(): List<Pair<String, Int>> {
        val result = mutableListOf<Pair<String, Int>>()
        val sdf = SimpleDateFormat("EEE", Locale("id", "ID"))
        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -i) }
            val key = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
            val label = sdf.format(cal.time)
            val pages = prefs.getInt(key, 0)
            result.add(label to pages)
        }
        return result
    }
}
