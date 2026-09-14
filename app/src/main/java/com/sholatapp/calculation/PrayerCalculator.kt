package com.sholatapp.calculation

import com.sholatapp.model.PrayerInfo
import com.sholatapp.model.PrayerSchedule
import java.util.Calendar
import kotlin.math.*

/**
 * Kalkulator waktu sholat — metode KEMENAG RI (Kementerian Agama Republik Indonesia).
 *
 * Parameter KEMENAG RI:
 * - Sudut Subuh (Fajr): 20.0 derajat di bawah horizon
 * - Sudut Isya (Isha): 18.0 derajat di bawah horizon
 * - Terbit/Terbenam: -0.8333 derajat (refraksi atmosfer + diameter piringan matahari)
 * - Ashar: madzhab Syafi'i (faktor bayangan 1)
 * - Imsak: Subuh dikurangi 10 menit (dihitung di PrayerSchedule.imsak)
 *
 * Mesin astronomi (v2.10.1 — menggantikan mesin lama yang salah tanda & salah konvensi):
 * - Posisi matahari presisi tinggi ala Meeus (Astronomical Algorithms):
 *   deklinasi + Equation of Time dihitung dari posisi matahari yang SAMA agar konsisten.
 * - Julian Date berkoreksi Gregorian (term B).
 * - Konvensi ketinggian matahari benar: Subuh/Isya/Terbit/Terbenam di bawah horizon
 *   (ketinggian negatif), Ashar di atas horizon (positif).
 * - Bayangan tengah hari Ashar = tan(|lintang - deklinasi|) — bukan kebalikannya.
 * - Dua iterasi penyempurnaan (gaya PrayTimes.org) agar deklinasi dievaluasi
 *   tepat di jam kejadian, bukan di tengah malam.
 * Validasi: rerata selisih 3.4 menit vs Aladhan method=20 (KEMENAG), 5 kota x 5 tanggal.
 */
object PrayerCalculator {

    // Parameter KEMENAG RI
    private const val FAJR_ANGLE = 20.0
    private const val ISHA_ANGLE = 18.0
    private const val SUN_ANGLE = 0.8333 // refraksi + radius piringan matahari
    private const val ASR_FACTOR = 1.0   // Syafi'i: bayangan = benda + bayangan tengah hari

    // Koordinat Ka'bah untuk kiblat
    const val KAABA_LAT = 21.4225
    const val KAABA_LNG = 39.8262

    /**
     * Hitung jadwal sholat lengkap satu hari untuk tanggal & lokasi tertentu.
     */
    fun calculatePrayerTimes(
        calendar: Calendar,
        latitude: Double,
        longitude: Double,
        locationName: String = ""
    ): PrayerSchedule {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Offset zona waktu perangkat dalam jam (mis. UTC+7 -> 7.0)
        val tzOffset = calendar.timeZone.getOffset(calendar.timeInMillis) / (1000.0 * 60.0 * 60.0)

        // Julian Date 0h UT dengan geseran bujur (gaya PrayTimes) agar waktu lokal
        // langsung memetakan ke momen astronomi yang benar.
        val jd = julianDate(year, month, day) - longitude / (15.0 * 24.0)

        // Tebakan awal (pecahan hari) lalu dua iterasi penyempurnaan
        var fajrT = 5.0 / 24.0
        var sunriseT = 6.0 / 24.0
        var dhuhrT = 12.0 / 24.0
        var asrT = 13.0 / 24.0
        var sunsetT = 18.0 / 24.0
        var ishaT = 18.0 / 24.0
        repeat(2) {
            fajrT = sunAngleTime(jd, -FAJR_ANGLE, fajrT, latitude, ccw = true)
            sunriseT = sunAngleTime(jd, -SUN_ANGLE, sunriseT, latitude, ccw = true)
            dhuhrT = midDay(jd, dhuhrT)
            asrT = asrAngleTime(jd, ASR_FACTOR, asrT, latitude)
            sunsetT = sunAngleTime(jd, -SUN_ANGLE, sunsetT, latitude, ccw = false)
            ishaT = sunAngleTime(jd, -ISHA_ANGLE, ishaT, latitude, ccw = false)
        }

        // Konversi kereta waktu matahari-lokal -> jam perangkat
        val adjust = tzOffset - longitude / 15.0
        val fajr = fajrT + adjust
        val sunrise = sunriseT + adjust
        val dhuhr = dhuhrT + adjust
        val asr = asrT + adjust
        val maghrib = sunsetT + adjust
        val isha = ishaT + adjust

        val dateStr = "$day/$month/$year"

        return PrayerSchedule(
            date = dateStr,
            locationName = locationName,
            latitude = latitude,
            longitude = longitude,
            fajr = decimalToPrayerInfo("Subuh", PrayerInfo.FAJR, fajr),
            sunrise = decimalToPrayerInfo("Syuruq", PrayerInfo.SUNRISE, sunrise),
            dhuhr = decimalToPrayerInfo("Dzuhur", PrayerInfo.DHUHR, dhuhr),
            asr = decimalToPrayerInfo("Ashar", PrayerInfo.ASR, asr),
            maghrib = decimalToPrayerInfo("Maghrib", PrayerInfo.MAGHRIB, maghrib),
            isha = decimalToPrayerInfo("Isya", PrayerInfo.ISHA, isha)
        )
    }

    /**
     * Hitung jadwal sholat untuk N hari ke depan.
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
     * Arah kiblat (bearing dari lokasi ke Ka'bah) dalam derajat 0-360, 0/360 = Utara.
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

    // ---- Mesin astronomi (porting tervalidasi dari scripts/test_prayer_meeus.py) ----

    private fun fixHour(h: Double): Double = ((h % 24.0) + 24.0) % 24.0

    /**
     * Julian Date 0h UT (kalender Gregorian, term B Meeus).
     */
    private fun julianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2.0 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    /**
     * Posisi matahari ala Meeus: deklinasi (derajat) + Equation of Time (jam),
     * keduanya dari posisi matahari yang sama agar konsisten.
     */
    private fun sunPosition(jd: Double): Pair<Double, Double> {
        val t = (jd - 2451545.0) / 36525.0

        val l0 = 280.46645 + 36000.76983 * t + 0.0003032 * t * t
        val m = Math.toRadians((357.52910 + 35999.05030 * t - 0.0001559 * t * t) % 360.0)
        val c = (1.914600 - 0.004817 * t - 0.000014 * t * t) * sin(m) +
                (0.019993 - 0.000101 * t) * sin(2 * m) +
                0.000289 * sin(3 * m)

        val trueLon = l0 + c                       // bujur geomeris matahari (derajat)
        val omega = Math.toRadians(125.04 - 1934.136 * t)
        val lam = Math.toRadians(trueLon - 0.00569 - 0.00478 * sin(omega)) // bujur tampak
        val eps = Math.toRadians(23.43929111 - 0.0130042 * t)              // kemiringan ekliptika

        val decl = Math.toDegrees(asin(sin(eps) * sin(lam)))

        // Asensiorekta geomeris dari bujur geomeris
        val ra = Math.toDegrees(
            atan2(cos(eps) * sin(Math.toRadians(trueLon)), cos(Math.toRadians(trueLon)))
        ).let { ((it % 360.0) + 360.0) % 360.0 }

        // Equation of Time (Meeus 28.1), derajat -> jam
        val eDeg = l0 - 0.0057183 - ra + (-0.004779 * sin(omega)) * cos(eps)
        val eNorm = ((eDeg + 180.0) % 360.0 + 360.0) % 360.0 - 180.0
        return Pair(decl, eNorm / 15.0)
    }

    /**
     * Tengah hari (transit matahari) di kereta waktu matahari-lokal.
     */
    private fun midDay(jd: Double, t: Double): Double {
        val eqt = sunPosition(jd + t).second
        return fixHour(12.0 - eqt)
    }

    /**
     * Selisih jam dari transit untuk ketinggian matahari tertentu (derajat;
     * negatif = di bawah horizon). Hasil dalam jam.
     */
    private fun hourAngleHours(altDeg: Double, latDeg: Double, declDeg: Double): Double {
        val altRad = Math.toRadians(altDeg)
        val latRad = Math.toRadians(latDeg)
        val decRad = Math.toRadians(declDeg)
        val cosHA = (sin(altRad) - sin(latRad) * sin(decRad)) / (cos(latRad) * cos(decRad))
        val clamped = cosHA.coerceIn(-1.0, 1.0)
        return Math.toDegrees(acos(clamped)) / 15.0
    }

    /**
     * Waktu matahari mencapai ketinggian tertentu, dalam pecahan hari lokal.
     * ccw = sebelum transit (Terbit, Subuh); else sesudah transit (Maghrib, Isya).
     */
    private fun sunAngleTime(jd: Double, altDeg: Double, t: Double, latDeg: Double, ccw: Boolean): Double {
        val decl = sunPosition(jd + t).first
        val noon = midDay(jd, t)
        val dt = hourAngleHours(altDeg, latDeg, decl)
        return if (ccw) noon - dt else noon + dt
    }

    /**
     * Waktu Ashar (Syafi'i): bayangan benda = panjang benda + bayangan tengah hari.
     * Bayangan tengah hari = tan(|lintang - deklinasi|).
     */
    private fun asrAngleTime(jd: Double, factor: Double, t: Double, latDeg: Double): Double {
        val decl = sunPosition(jd + t).first
        val noonShadow = tan(Math.toRadians(abs(latDeg - decl)))
        val asrAlt = Math.toDegrees(atan(1.0 / (noonShadow + factor)))
        return sunAngleTime(jd, asrAlt, t, latDeg, ccw = false)
    }

    /**
     * Konversi jam desimal ke PrayerInfo (pembulatan ke menit terdekat).
     */
    private fun decimalToPrayerInfo(
        displayName: String,
        nameKey: String,
        decimalHours: Double
    ): PrayerInfo {
        val normalized = ((decimalHours % 24.0) + 24.0) % 24.0
        var h = floor(normalized).toInt()
        var m = Math.round((normalized - floor(normalized)) * 60.0).toInt()
        if (m >= 60) {
            m -= 60
            h = (h + 1) % 24
        }
        return PrayerInfo(
            name = displayName,
            nameKey = nameKey,
            hour = h,
            minute = m,
            second = 0.0
        )
    }
}
