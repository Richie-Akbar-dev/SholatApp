package com.sholatapp.calculation

import java.util.Calendar

/**
 * Kalkulator hari puasa (v2.7) — sumber logika bersama untuk:
 * - UI halaman Puasa (cincin kalender, chip jadwal, label jenis puasa)
 * - Pengingat notifikasi puasa sunnah (SunnahReminderReceiver)
 *
 * Hari anjuran puasa sunnah:
 * - Senin & Kamis (sunnah mingguan)
 * - Ayyamul Bidh: tanggal Hijriah 13, 14, 15 (android.icu, tersedia sejak API 24)
 * Puasa wajib: seluruh bulan Ramadhan (bulan Hijriah 9).
 */
object SunnahDayCalculator {

    /** Tanggal Hijriah (tanggal, bulan 1-12) utk tanggal Masehi; null bila perhitungan gagal. */
    fun hijriOf(year: Int, month0: Int, day: Int): Pair<Int, Int>? {
        return try {
            val cal = Calendar.getInstance().apply { set(year, month0, day, 12, 0, 0) }
            val islamic = android.icu.util.IslamicCalendar()
            islamic.time = cal.time
            val d = islamic.get(android.icu.util.IslamicCalendar.DAY_OF_MONTH)
            val m = islamic.get(android.icu.util.IslamicCalendar.MONTH) + 1 // ICU 0-based
            Pair(d, m)
        } catch (e: Exception) {
            null
        }
    }

    fun isAyyamulBidh(year: Int, month0: Int, day: Int): Boolean {
        val h = hijriOf(year, month0, day) ?: return false
        return h.first in 13..15
    }

    fun isMondayOrThursday(year: Int, month0: Int, day: Int): Boolean {
        val cal = Calendar.getInstance().apply { set(year, month0, day) }
        val dow = cal.get(Calendar.DAY_OF_WEEK)
        return dow == Calendar.MONDAY || dow == Calendar.THURSDAY
    }

    /** Hari anjuran puasa sunnah: Senin/Kamis atau Ayyamul Bidh. */
    fun isSunnahDay(year: Int, month0: Int, day: Int): Boolean =
        isMondayOrThursday(year, month0, day) || isAyyamulBidh(year, month0, day)

    /** Apakah tanggal jatuh pada bulan Ramadhan (puasa wajib). */
    fun isRamadhan(year: Int, month0: Int, day: Int): Boolean {
        val h = hijriOf(year, month0, day) ?: return false
        return h.second == 9
    }

    /**
     * Label jenis puasa otomatis dari tanggal — dipakai chip pada kartu status:
     * "Wajib · Ramadhan", "Sunnah · Senin", "Sunnah · Kamis",
     * "Sunnah · Ayyamul Bidh", atau "Puasa" (ketika tanpa penanda khusus).
     */
    fun fastingTypeLabel(year: Int, month0: Int, day: Int): String {
        if (isRamadhan(year, month0, day)) return "Wajib · Ramadhan"
        val cal = Calendar.getInstance().apply { set(year, month0, day) }
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Sunnah · Senin"
            Calendar.THURSDAY -> "Sunnah · Kamis"
            else -> if (isAyyamulBidh(year, month0, day)) "Sunnah · Ayyamul Bidh" else "Puasa"
        }
    }

    /** Nama hari singkat (Indonesia): Sen, Sel, Rab, Kam, Jum, Sab, Min. */
    fun weekdayShort(year: Int, month0: Int, day: Int): String {
        val cal = Calendar.getInstance().apply { set(year, month0, day) }
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Sen"
            Calendar.TUESDAY -> "Sel"
            Calendar.WEDNESDAY -> "Rab"
            Calendar.THURSDAY -> "Kam"
            Calendar.FRIDAY -> "Jum"
            Calendar.SATURDAY -> "Sab"
            else -> "Min"
        }
    }

    /** Nama hari lengkap (Indonesia): Senin, Selasa, ... Minggu. */
    fun weekdayFull(year: Int, month0: Int, day: Int): String {
        val cal = Calendar.getInstance().apply { set(year, month0, day) }
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Senin"
            Calendar.TUESDAY -> "Selasa"
            Calendar.WEDNESDAY -> "Rabu"
            Calendar.THURSDAY -> "Kamis"
            Calendar.FRIDAY -> "Jumat"
            Calendar.SATURDAY -> "Sabtu"
            else -> "Minggu"
        }
    }

    /**
     * Tanggal puasa sunnah berikutnya SETELAH tanggal pada [base]
     * (pindai maksimum 45 hari ke depan). Null bila tidak ditemukan.
     */
    fun nextSunnahAfter(base: Calendar): Calendar? {
        val cal = base.clone() as Calendar
        for (i in 1..45) {
            cal.add(Calendar.DAY_OF_MONTH, 1)
            if (isSunnahDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))) {
                return cal.clone() as Calendar
            }
        }
        return null
    }

    /** Selisih hari (>=1) antara hari ini dan tanggal target. */
    fun daysFromToday(target: Calendar): Int {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val t = target.clone() as Calendar
        t.set(Calendar.HOUR_OF_DAY, 0); t.set(Calendar.MINUTE, 0)
        t.set(Calendar.SECOND, 0); t.set(Calendar.MILLISECOND, 0)
        return ((t.timeInMillis - today.timeInMillis) / 86400000L).toInt()
    }

    /** Kejadian puasa sunnah dalam 1 bulan Masehi (rentang berurutan digabung utk chip Ayyamul Bidh). */
    data class SunnahOccurrence(val dayStart: Int, val dayEnd: Int, val hasAyyamulBidh: Boolean)

    fun occurrencesInMonth(year: Int, month0: Int): List<SunnahOccurrence> {
        val cal = Calendar.getInstance().apply { set(year, month0, 1) }
        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val result = mutableListOf<SunnahOccurrence>()
        var runStart = -1
        var runBidh = false
        for (d in 1..maxDay) {
            val isSunnah = isSunnahDay(year, month0, d)
            val isBidh = isAyyamulBidh(year, month0, d)
            if (isSunnah) {
                if (runStart == -1) {
                    runStart = d
                    runBidh = isBidh
                } else {
                    runBidh = runBidh || isBidh
                }
            } else {
                if (runStart != -1) {
                    result.add(SunnahOccurrence(runStart, d - 1, runBidh))
                    runStart = -1
                }
            }
        }
        if (runStart != -1) result.add(SunnahOccurrence(runStart, maxDay, runBidh))
        return result
    }

    /** Label chip utk satu kejadian: "Sen 7", "Kam 10", atau "Sel 13–15 · Ayyamul Bidh". */
    fun occurrenceLabel(year: Int, month0: Int, occ: SunnahOccurrence): String {
        val sb = StringBuilder(weekdayShort(year, month0, occ.dayStart))
            .append(' ').append(occ.dayStart)
        if (occ.dayEnd > occ.dayStart) sb.append('\u2013').append(occ.dayEnd)
        if (occ.hasAyyamulBidh) sb.append(" · Ayyamul Bidh")
        return sb.toString()
    }
}
