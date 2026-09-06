package com.sholatapp.model

import java.util.Locale

/**
 * Represents a single prayer time entry.
 */
data class PrayerInfo(
    val name: String,
    val nameKey: String,
    val hour: Int,
    val minute: Int,
    val second: Double = 0.0
) {
    val timeString: String
        get() = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)

    /**
     * Total seconds since midnight for comparison purposes.
     */
    val totalSeconds: Int
        get() = hour * 3600 + minute * 60 + second.toInt()

    companion object {
        const val FAJR = "fajr"
        const val SUNRISE = "sunrise"
        const val DHUHR = "dhuhr"
        const val ASR = "asr"
        const val MAGHRIB = "maghrib"
        const val ISHA = "isha"
    }
}

/**
 * Full daily prayer schedule containing all 6 times.
 */
data class PrayerSchedule(
    val date: String,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val fajr: PrayerInfo,
    val sunrise: PrayerInfo,
    val dhuhr: PrayerInfo,
    val asr: PrayerInfo,
    val maghrib: PrayerInfo,
    val isha: PrayerInfo
) {
    /**
     * Waktu imsak (standar KEMENAG RI: 10 menit sebelum Subuh).
     * Bisa null jika belum dihitung (data lama).
     */
    val imsak: PrayerInfo?
        get() {
            val imsakSeconds = fajr.totalSeconds - 10 * 60
            if (imsakSeconds < 0) return null
            return PrayerInfo(
                name = "Imsak",
                nameKey = "imsak",
                hour = imsakSeconds / 3600,
                minute = (imsakSeconds % 3600) / 60
            )
        }

    /**
     * Returns all prayer times (excluding sunrise) as a list.
     */
    val prayerList: List<PrayerInfo>
        get() = listOf(fajr, dhuhr, asr, maghrib, isha)

    /**
     * Returns all 6 times including sunrise.
     */
    val allTimes: List<PrayerInfo>
        get() = listOf(fajr, sunrise, dhuhr, asr, maghrib, isha)

    /**
     * Find the next upcoming prayer based on current time.
     * Returns the prayer info or null if all prayers have passed.
     */
    fun getNextPrayer(currentHour: Int, currentMinute: Int): PrayerInfo? {
        val currentSeconds = currentHour * 3600 + currentMinute * 60
        return prayerList.firstOrNull { it.totalSeconds > currentSeconds }
            ?: prayerList.firstOrNull() // Wrap to tomorrow's Fajr
    }

    /**
     * Get seconds remaining until the next prayer.
     */
    fun getSecondsToNextPrayer(currentHour: Int, currentMinute: Int, currentSecond: Int = 0): Int {
        val currentSeconds = currentHour * 3600 + currentMinute * 60 + currentSecond
        val next = getNextPrayer(currentHour, currentMinute) ?: return 0
        val diff = next.totalSeconds - currentSeconds
        return if (diff > 0) diff else diff + 86400
    }
}