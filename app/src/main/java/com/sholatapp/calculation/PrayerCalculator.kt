package com.sholatapp.calculation

import com.sholatapp.model.PrayerInfo
import com.sholatapp.model.PrayerSchedule
import java.util.Calendar
import kotlin.math.*

/**
 * Prayer time calculator using the KEMENAG RI (Kementerian Agama Republik Indonesia) method.
 *
 * Calculation parameters for KEMENAG RI:
 * - Fajr angle: 20.0°
 * - Isha angle: 18.0°
 * - Maghrib/Sunrise angle: 0.8333° (accounts for atmospheric refraction)
 * - Asr: Shafi'i method (shadow factor = 1)
 */
object PrayerCalculator {

    // KEMENAG RI parameters
    private const val FAJR_ANGLE = 20.0
    private const val ISHA_ANGLE = 18.0
    private const val SUN_ANGLE = 0.8333 // for sunrise and maghrib
    private const val ASR_FACTOR = 1.0 // Shafi'i: shadow = object + shadow at noon

    // Mecca coordinates for Qibla
    const val KAABA_LAT = 21.4225
    const val KAABA_LNG = 39.8262

    /**
     * Calculate the full daily prayer schedule for a given date and location.
     *
     * @param calendar The date for which to calculate prayer times
     * @param latitude Location latitude in decimal degrees
     * @param longitude Location longitude in decimal degrees
     * @param locationName Human-readable location name
     * @return PrayerSchedule with all 6 prayer times
     */
    fun calculatePrayerTimes(
        calendar: Calendar,
        latitude: Double,
        longitude: Double,
        locationName: String = ""
    ): PrayerSchedule {
        val jd = getJulianDate(calendar)
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        // Sun parameters
        val declination = getSunDeclination(jd, dayOfYear)
        val eqTime = getEquationOfTime(jd, dayOfYear)

        // Get device timezone offset in hours (e.g., UTC+7 → 7.0)
        val timezoneOffset = calendar.timeZone.getOffset(calendar.timeInMillis) / (1000.0 * 60.0 * 60.0)

        // Latitude in radians
        val latRad = Math.toRadians(latitude)
        val decRad = Math.toRadians(declination)

        // Calculate each prayer time as decimal hours (local solar time)
        // 12.0 = solar noon at Greenwich in UTC
        // - longitude/15.0 = convert to local solar meridian
        // - eqTime/60.0 = equation of time correction (converted from minutes to hours)
        // + timezoneOffset = convert UTC to device local time
        val dhuhrDecimal = 12.0 - (longitude / 15.0) - (eqTime / 60.0) + timezoneOffset

        // Hour angle calculations
        val sunriseHA = getHourAngle(latRad, decRad, SUN_ANGLE)
        val fajrHA = getHourAngle(latRad, decRad, FAJR_ANGLE)
        val ishaHA = getHourAngle(latRad, decRad, ISHA_ANGLE)
        val asrHA = getAsrHourAngle(latRad, decRad, ASR_FACTOR, dhuhrDecimal, longitude, eqTime)

        // Convert to actual times (decimal hours)
        val sunriseTime = dhuhrDecimal - sunriseHA
        val fajrTime = dhuhrDecimal - fajrHA
        val maghribTime = dhuhrDecimal + sunriseHA
        val ishaTime = dhuhrDecimal + ishaHA
        val asrTime = dhuhrDecimal + asrHA

        // Format the date string
        val dateStr = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"

        return PrayerSchedule(
            date = dateStr,
            locationName = locationName,
            latitude = latitude,
            longitude = longitude,
            fajr = decimalToPrayerInfo("Subuh", PrayerInfo.FAJR, fajrTime),
            sunrise = decimalToPrayerInfo("Syuruq", PrayerInfo.SUNRISE, sunriseTime),
            dhuhr = decimalToPrayerInfo("Dzuhur", PrayerInfo.DHUHR, dhuhrDecimal),
            asr = decimalToPrayerInfo("Ashar", PrayerInfo.ASR, asrTime),
            maghrib = decimalToPrayerInfo("Maghrib", PrayerInfo.MAGHRIB, maghribTime),
            isha = decimalToPrayerInfo("Isya", PrayerInfo.ISHA, ishaTime)
        )
    }

    /**
     * Calculate prayer times for the next 30 days.
     */
    fun calculateMonthlySchedule(
        latitude: Double,
        longitude: Double,
        locationName: String = "",
        days: Int = 30
    ): List<PrayerSchedule> {
        val schedules = mutableListOf<PrayerSchedule>()
        val cal = Calendar.getInstance()

        for (i in 0 until days) {
            val dayCal = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, i)
            }
            schedules.add(
                calculatePrayerTimes(dayCal, latitude, longitude, locationName)
            )
        }
        return schedules
    }

    /**
     * Calculate the Qibla direction (bearing from location to Mecca) in degrees.
     * Returns 0-360 where 0/360 is North.
     */
    fun calculateQiblaDirection(latitude: Double, longitude: Double): Double {
        val lat1 = Math.toRadians(latitude)
        val lng1 = Math.toRadians(longitude)
        val lat2 = Math.toRadians(KAABA_LAT)
        val lng2 = Math.toRadians(KAABA_LNG)

        val dLng = lng2 - lng1

        val x = sin(dLng) * cos(lat2)
        val y = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLng)

        var bearing = Math.toDegrees(atan2(x, y))
        bearing = (bearing + 360) % 360
        return bearing
    }

    // ---- Private calculation methods ----

    /**
     * Convert Gregorian date to Julian Date number.
     */
    private fun getJulianDate(cal: Calendar): Double {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        if (month <= 2) {
            val adjustedYear = year - 1
            val adjustedMonth = month + 12
            return (floor(365.25 * (adjustedYear + 4716)) + floor(30.6001 * (adjustedMonth + 1))
                    + day + hour / 24.0 + minute / 1440.0 - 1524.5)
        }
        return (floor(365.25 * (year + 4716)) + floor(30.6001 * (month + 1))
                + day + hour / 24.0 + minute / 1440.0 - 1524.5)
    }

    /**
     * Calculate the sun's declination in degrees.
     * Uses a simplified but accurate formula based on the day of the year.
     */
    private fun getSunDeclination(jd: Double, dayOfYear: Int): Double {
        // More accurate calculation using Julian centuries
        val T = (jd - 2451545.0) / 36525.0
        val L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T * T
        var M = 357.52910 + 35999.05030 * T - 0.0001559 * T * T
        var e = 0.016708617 - 0.000042037 * T - 0.0000001236 * T * T

        M = Math.toRadians(M % 360.0)
        val C = (1.914600 - 0.004817 * T - 0.000014 * T * T) * sin(M)
                + (0.019993 - 0.000101 * T) * sin(2 * M)
                + 0.000289 * sin(3 * M)

        val sunLon = Math.toRadians(L0 + C)
        val omega = 125.04 - 1934.136 * T
        val lambda = sunLon - Math.toRadians(0.00569 - 0.00478 * sin(Math.toRadians(omega)))

        return Math.toDegrees(
            asin(
                sin(lambda) * sin(Math.toRadians(23.43929111 - 0.0130042 * T))
            )
        )
    }

    /**
     * Calculate the equation of time in hours.
     */
    private fun getEquationOfTime(jd: Double, dayOfYear: Int): Double {
        val B = Math.toRadians(360.0 / 365.0 * (dayOfYear - 81))
        return 9.87 * sin(2 * B) - 7.53 * cos(B) - 1.5 * sin(B)
    }

    /**
     * Calculate the hour angle for a given sun altitude angle.
     * Returns the hour angle in decimal hours.
     */
    private fun getHourAngle(
        latRad: Double,
        decRad: Double,
        angle: Double
    ): Double {
        val angleRad = Math.toRadians(angle)
        val cosHA = (sin(angleRad) - sin(latRad) * sin(decRad)) / (cos(latRad) * cos(decRad))
        // Clamp to handle edge cases near poles
        val clampedCos = cosHA.coerceIn(-1.0, 1.0)
        return Math.toDegrees(acos(clampedCos)) / 15.0
    }

    /**
     * Calculate the hour angle for Asr prayer.
     * Shafi'i method: shadow length = object height + shadow at noon.
     */
    private fun getAsrHourAngle(
        latRad: Double,
        decRad: Double,
        factor: Double,
        dhuhrDecimal: Double,
        longitude: Double,
        eqTime: Double
    ): Double {
        // Calculate solar altitude at noon
        val noonAltitude = asin(
            sin(latRad) * sin(decRad) + cos(latRad) * cos(decRad)
        ).let { 90.0 - Math.toDegrees(it) } // zenith angle

        // Shadow length at noon = cotangent of noon altitude
        val noonShadow = if (noonAltitude > 0) 1.0 / tan(Math.toRadians(noonAltitude)) else 0.0

        // Asr shadow = noon shadow + factor
        val asrShadow = noonShadow + factor

        // Asr altitude angle
        val asrAltitude = Math.toDegrees(atan(1.0 / asrShadow))

        return getHourAngle(latRad, decRad, asrAltitude)
    }

    /**
     * Convert decimal hours to a PrayerInfo object.
     */
    private fun decimalToPrayerInfo(
        displayName: String,
        nameKey: String,
        decimalHours: Double
    ): PrayerInfo {
        val normalized = ((decimalHours % 24.0) + 24.0) % 24.0
        val h = normalized.toInt()
        val m = ((normalized - h) * 60.0).toInt()
        val s = (((normalized - h) * 60.0 - m) * 60.0)
        return PrayerInfo(
            name = displayName,
            nameKey = nameKey,
            hour = h,
            minute = m,
            second = s
        )
    }
}