package com.sholatapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sholatapp.data.MutabaahData
import com.sholatapp.model.MutabaahItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class MutabaahUiState(
    val allItems: List<MutabaahItem> = emptyList(),
    val completedIds: Set<String> = emptySet(),
    val completionPercent: Int = 0,
    val streak: Int = 0
)

class MutabaahViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("mutabaah_prefs", 0)
    private val _uiState = MutableStateFlow(MutabaahUiState())
    val uiState: StateFlow<MutabaahUiState> = _uiState.asStateFlow()

    init {
        loadToday()
        loadStreak()
    }

    fun loadToday() {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val items = MutabaahData.getAllItems()
        val completed = prefs.getStringSet("${today}_completed", emptySet()) ?: emptySet()
        _uiState.update {
            it.copy(
                allItems = items,
                completedIds = completed,
                completionPercent = if (items.isNotEmpty()) (completed.size * 100 / items.size) else 0
            )
        }
    }

    fun toggleItem(id: String) {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val current = prefs.getStringSet("${today}_completed", emptySet())?.toMutableSet() ?: mutableSetOf()
        if (id in current) current.remove(id) else current.add(id)
        prefs.edit().putStringSet("${today}_completed", current).apply()
        _uiState.update {
            val items = it.allItems
            it.copy(
                completedIds = current.toSet(),
                completionPercent = if (items.isNotEmpty()) (current.size * 100 / items.size) else 0
            )
        }
    }

    fun isCompleted(id: String): Boolean = id in _uiState.value.completedIds

    fun loadStreak() {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            var streak = 0
            val allItems = MutabaahData.getAllItems()
            val totalRequired = allItems.size
            for (i in 0 until 30) {
                val checkDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -i) }
                val key = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(checkDate.time)
                val completed = prefs.getStringSet("${key}_completed", emptySet())?.size ?: 0
                if (completed >= totalRequired) streak++ else break
            }
            _uiState.update { it.copy(streak = streak) }
        }
    }
}
