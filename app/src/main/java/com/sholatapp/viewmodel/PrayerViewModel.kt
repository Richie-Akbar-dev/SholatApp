package com.sholatapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sholatapp.alarm.AlarmScheduler
import com.sholatapp.azan.AzanPlayer
import com.sholatapp.calculation.PrayerCalculator
import com.sholatapp.location.LocationHelper
import com.sholatapp.location.LocationResult
import com.sholatapp.model.PrayerSchedule
import com.sholatapp.notification.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class UiState(
    val isLoading: Boolean = true,
    val locationAddress: String = "Mendeteksi lokasi...",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val prayerSchedule: PrayerSchedule? = null,
    val monthlySchedule: List<PrayerSchedule> = emptyList(),
    val countdownSeconds: Int = 0,
    val isAlarmEnabled: Boolean = true,
    val isPrepAlarmEnabled: Boolean = true,
    val showMonthlyView: Boolean = false,
    val errorMessage: String? = null,
    val isAzanDownloaded: Boolean = false,
    val isDownloadingAzan: Boolean = false,
    val checkedPrayers: Set<String> = emptySet(),
    val currentTimeStr: String = "",
    val isDndEnabled: Boolean = false
)

class PrayerViewModel(application: Application) : AndroidViewModel(application) {

    private val locationHelper = LocationHelper(application)
    private val alarmScheduler = AlarmScheduler(application)
    private val prefs = application.getSharedPreferences("sholat_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val checklistPrefs = application.getSharedPreferences("sholat_checklist", Context.MODE_PRIVATE)
    private var isLocationDetected = false

    init {
        // Create notification channel
        NotificationHelper(application).createNotificationChannel()

        // Load saved settings
        val alarmEnabled = prefs.getBoolean("alarm_enabled", true)
        val prepAlarmEnabled = prefs.getBoolean("prep_alarm_enabled", true)
        val dndEnabled = prefs.getBoolean("dnd_enabled", false)
        _uiState.update { it.copy(isAlarmEnabled = alarmEnabled, isPrepAlarmEnabled = prepAlarmEnabled, isDndEnabled = dndEnabled) }

        // Load today's checked prayers
        loadCheckedPrayers()

        // Try to load last known location first
        val savedLat = prefs.getFloat("last_latitude", 0f).toDouble()
        val savedLng = prefs.getFloat("last_longitude", 0f).toDouble()
        val savedName = prefs.getString("last_location_name", "") ?: ""

        if (savedLat != 0.0 || savedLng != 0.0) {
            // Use cached location immediately
            calculateAndUpdate(savedLat, savedLng, savedName)
        }

        // Then try to get fresh location
        detectLocation()

        // Check azan availability
        val azanPlayer = AzanPlayer(application)
        _uiState.update { it.copy(isAzanDownloaded = azanPlayer.isAzanAvailable()) }

        // Start clock + countdown timer
        startClock()
    }

    /**
     * Load today's checked prayers from SharedPreferences.
     */
    private fun loadCheckedPrayers() {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val checked = checklistPrefs.getStringSet("${today}_checked", emptySet()) ?: emptySet()
        _uiState.update { it.copy(checkedPrayers = checked) }
    }

    /**
     * Mark a prayer as checked for today.
     */
    fun checkPrayer(nameKey: String) {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val current = checklistPrefs.getStringSet("${today}_checked", emptySet())?.toMutableSet() ?: mutableSetOf()
        current.add(nameKey)
        checklistPrefs.edit().putStringSet("${today}_checked", current).apply()
        _uiState.update { it.copy(checkedPrayers = current.toSet()) }
    }

    /**
     * Detect user's current location.
     */
    fun detectLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = locationHelper.getCurrentLocation()) {
                is LocationResult.Success -> {
                    isLocationDetected = true
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            latitude = result.latitude,
                            longitude = result.longitude,
                            locationAddress = result.address,
                            errorMessage = null
                        )
                    }

                    // Save location to preferences
                    prefs.edit()
                        .putFloat("last_latitude", result.latitude.toFloat())
                        .putFloat("last_longitude", result.longitude.toFloat())
                        .putString("last_location_name", result.address)
                        .apply()

                    // Calculate prayer times
                    calculateAndUpdate(
                        result.latitude,
                        result.longitude,
                        result.address
                    )
                }
                is LocationResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    /**
     * Calculate prayer times and update the UI state.
     */
    private fun calculateAndUpdate(lat: Double, lng: Double, name: String) {
        val now = Calendar.getInstance()
        val schedule = PrayerCalculator.calculatePrayerTimes(now, lat, lng, name)
        val monthly = PrayerCalculator.calculateMonthlySchedule(lat, lng, name, 30)

        _uiState.update {
            it.copy(
                prayerSchedule = schedule,
                monthlySchedule = monthly,
                locationAddress = name,
                latitude = lat,
                longitude = lng,
                isLoading = false
            )
        }

        // Schedule alarms if enabled
        if (_uiState.value.isAlarmEnabled) {
            alarmScheduler.schedulePrayerAlarms(schedule)
        }
    }

    /**
     * Start a clock timer that updates every second.
     */
    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                val schedule = _uiState.value.prayerSchedule
                val cal = Calendar.getInstance()
                val timeStr = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                val seconds = if (schedule != null) {
                    schedule.getSecondsToNextPrayer(
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        cal.get(Calendar.SECOND)
                    )
                } else 0
                _uiState.update { it.copy(currentTimeStr = timeStr, countdownSeconds = seconds) }
                delay(1000)
            }
        }
    }

    /**
     * Toggle alarm on/off.
     */
    fun toggleAlarm(enabled: Boolean) {
        _uiState.update { it.copy(isAlarmEnabled = enabled) }
        prefs.edit().putBoolean("alarm_enabled", enabled).apply()

        if (enabled) {
            _uiState.value.prayerSchedule?.let {
                alarmScheduler.schedulePrayerAlarms(it)
            }
        } else {
            alarmScheduler.cancelAllAlarms()
        }
    }

    /**
     * Toggle prep-alarm (20 min before) on/off.
     */
    fun togglePrepAlarm(enabled: Boolean) {
        _uiState.update { it.copy(isPrepAlarmEnabled = enabled) }
        prefs.edit().putBoolean("prep_alarm_enabled", enabled).apply()

        // Reschedule with updated setting
        if (_uiState.value.isAlarmEnabled) {
            _uiState.value.prayerSchedule?.let {
                alarmScheduler.schedulePrayerAlarms(it)
            }
        }
    }

    /**
     * Toggle DND Focus Mode on/off saat waktu sholat.
     * Jika diaktifkan, DND akan menyala otomatis saat alarm sholat
     * dan mati setelah 30 menit.
     */
    fun toggleDnd(enabled: Boolean) {
        _uiState.update { it.copy(isDndEnabled = enabled) }
        prefs.edit().putBoolean("dnd_enabled", enabled).apply()
    }

    /**
     * Toggle monthly view.
     */
    fun toggleMonthlyView(show: Boolean) {
        _uiState.update { it.copy(showMonthlyView = show) }
    }

    /**
     * Check azan availability (now bundled, no download needed).
     */
    fun checkAzanAvailability() {
        val azanPlayer = AzanPlayer(getApplication())
        _uiState.update { it.copy(isAzanDownloaded = azanPlayer.isAzanAvailable()) }
    }
}
