package com.sholatapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import kotlin.math.floor

// ==================== HIJRI CALENDAR COLORS ====================
private object KalenderColors {
    val Background = Color(0xFF0A1A0A)
    val Gold = Color(0xFFD4AF37)
    val GoldLight = Color(0xFFFFD54F)
    val GoldDim = Color(0xFFB8941F)
    val Green = Color(0xFF1B4D3E)
    val White = Color(0xFFFFFFFF)
    val TextMuted = Color(0xFFA5D6A7)
    val TextDim = Color(0xFF6B9B6B)
    val Surface = Color(0xFF112211)
    val CardBg = Color(0xB3112211)
    val CardBorder = Color(0x33D4AF37)
    val JumatGold = Color(0x4DD4AF37)
    val EventRed = Color(0xFFEF5350)
    val EventGreen = Color(0xFF66BB6A)
}

// ==================== HIJRIAH CALCULATOR (offline) ====================
object HijriCalculator {
    private data class HijriDate(val day: Int, val month: Int, val year: Int)

    private val hijriMonthNames = arrayOf(
        "Muharram", "Safar", "Rabi'ul Awal", "Rabi'ul Akhir",
        "Jumadil Awal", "Jumadil Akhir", "Rajab", "Sya'ban",
        "Ramadhan", "Syawal", "Dzulqa'dah", "Dzulhijjah"
    )

    private val gregorianMonthDays = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)

    /** Convert Gregorian to Hijri date */
    fun gregorianToHijri(year: Int, month: Int, day: Int): Triple<Int, Int, String> {
        val jd = gregorianToJd(year, month, day)
        val hijri = jdToHijri(jd)
        return Triple(hijri.day, hijri.month, hijriMonthNames[hijri.month - 1])
    }

    /** Get Hijri month name */
    fun getMonthName(month: Int): String = hijriMonthNames[month - 1]

    /** Get days in Hijri month (approximate: alternates 29/30) */
    fun daysInMonth(year: Int, month: Int): Int {
        // Hijri months alternate 30/29 days, with Dzulhijjah = 30 in leap years
        return if (month % 2 == 1) 30
        else if (month == 12 && isHijriLeapYear(year)) 30
        else 29
    }

    private fun isHijriLeapYear(year: Int): Boolean {
        // Simple leap year rule (Kuwaiti algorithm approximation)
        return when {
            year % 2 == 0 -> false
            year % 3 == 0 -> false
            year % 4 == 0 -> true
            year % 5 == 0 -> false
            year % 7 == 0 -> true
            year % 8 == 0 -> false
            year % 10 == 0 -> true
            year % 12 == 0 -> false
            year % 15 == 0 -> true
            year % 16 == 0 -> false
            year % 18 == 0 -> true
            else -> false
        }
    }

    private fun gregorianToJd(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) { y--; m += 12 }
        val A = floor(y / 100.0)
        val B = 2 - A + floor(A / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + B - 1524.5
    }

    private fun jdToHijri(jd: Double): HijriDate {
        val l = jd - 1948439.5
        val n = floor(l / 10631.0)
        val lPrime = l - n * 10631.0
        val j = floor((lPrime - 0.5) / 531.0) * 283
        val lDPrime = lPrime - floor(j / 283.0) * 531.0
        val i = floor((lDPrime / 20.0) + 0.5)
        val d = lDPrime - floor(i * 19.45)
        val m = if (i < 145) ((i - 1) / 30) + 1 else ((i - 2) / 30) + 1
        val y = 30 * n + j / 285 + 30
        return HijriDate(d.toInt().coerceIn(1, 30), m.toInt().coerceIn(1, 12), y.toInt())
    }
}

// ==================== ISLAMIC EVENTS ====================
data class IslamicEvent(val hijriMonth: Int, val hijriDay: Int, val name: String, val color: Color)

private fun getIslamicEvents(hijriYear: Int): List<IslamicEvent> = listOf(
    IslamicEvent(1, 1, "Tahun Baru Islam 1 $hijriYear H", KalenderColors.EventGreen),
    IslamicEvent(1, 10, "Hari Asyura (10 Muharram)", KalenderColors.GoldDim),
    IslamicEvent(3, 12, "Maulid Nabi Muhammad SAW", KalenderColors.EventGreen),
    IslamicEvent(7, 27, "Isra' Mi'raj Nabi Muhammad SAW", KalenderColors.Gold),
    IslamicEvent(8, 15, "Malam Nisfu Sya'ban", KalenderColors.GoldDim),
    IslamicEvent(9, 1, "1 Ramadhan $hijriYear H", KalenderColors.EventRed),
    IslamicEvent(9, 27, "Nuzulul Quran (17 Ramadhan)", KalenderColors.Gold),
    IslamicEvent(10, 1, "1 Syawal $hijriYear H - Hari Raya Idul Fitri", KalenderColors.EventRed),
    IslamicEvent(10, 10, "Hari Arafah (9 Dzulhijjah)", KalenderColors.Gold),
    IslamicEvent(10, 11, "10 Dzulhijjah - Hari Raya Idul Adha", KalenderColors.EventRed),
    IslamicEvent(12, 10, "Hari Raya Idul Adha", KalenderColors.EventRed),
)

// ==================== MAIN SCREEN ====================
@Composable
fun KalenderScreen(
    onBack: () -> Unit = {}
) {
    val cal = Calendar.getInstance()
    var viewYear by remember { mutableIntStateOf(cal.get(Calendar.YEAR)) }
    var viewMonth by remember { mutableIntStateOf(cal.get(Calendar.MONTH)) }

    // Get Hijri info for 1st of viewing month to determine Hijri month/year
    val firstDayHijri = remember(viewYear, viewMonth) {
        HijriCalculator.gregorianToHijri(viewYear, viewMonth + 1, 1)
    }

    // Get events for the Hijri year
    val events = remember(firstDayHijri) {
        getIslamicEvents(firstDayHijri.first) + getIslamicEvents(firstDayHijri.first + 1)
    }

    val monthNames = arrayOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )
    val dayNames = listOf("S", "S", "R", "K", "J", "S", "M")

    val calMonth = Calendar.getInstance().apply { set(viewYear, viewMonth, 1) }
    val maxDay = calMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = (calMonth.get(Calendar.DAY_OF_WEEK) + 5) % 7
    val todayDay = if (viewYear == cal.get(Calendar.YEAR) && viewMonth == cal.get(Calendar.MONTH))
        cal.get(Calendar.DAY_OF_MONTH) else -1

    // Events this month
    val monthEvents = remember(events, viewYear, viewMonth) {
        events.filter { event ->
            // Check each day of this Gregorian month to see if it matches the Hijri event
            for (d in 1..maxDay) {
                val (hDay, hMonth, _) = HijriCalculator.gregorianToHijri(viewYear, viewMonth + 1, d)
                if (hDay == event.hijriDay && hMonth == event.hijriMonth) return@filter true
            }
            false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KalenderColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, null, tint = KalenderColors.Gold)
            }
            Column {
                Text(
                    "Kalender Islam",
                    style = MaterialTheme.typography.headlineMedium,
                    color = KalenderColors.Gold,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${firstDayHijri.third} ${firstDayHijri.first} H",
                    style = MaterialTheme.typography.bodyMedium,
                    color = KalenderColors.TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Month navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (viewMonth == 0) { viewMonth = 11; viewYear-- } else viewMonth--
            }) {
                Icon(Icons.Default.ChevronLeft, null, tint = KalenderColors.Gold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    monthNames[viewMonth],
                    style = MaterialTheme.typography.titleLarge,
                    color = KalenderColors.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$viewYear",
                    style = MaterialTheme.typography.bodySmall,
                    color = KalenderColors.TextMuted
                )
            }
            IconButton(onClick = {
                if (viewMonth == 11) { viewMonth = 0; viewYear++ } else viewMonth++
            }) {
                Icon(Icons.Default.ChevronRight, null, tint = KalenderColors.Gold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Day headers
        Row(modifier = Modifier.fillMaxWidth()) {
            dayNames.forEach { day ->
                Text(
                    day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (day == "J") KalenderColors.Gold else KalenderColors.TextDim,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Calendar grid
        var rows = 0
        for (startIdx in 0 until maxDay step 7) {
            if (rows > 0) Spacer(modifier = Modifier.height(1.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0..6) {
                    val dayNum = startIdx + col - firstDayOfWeek + 1
                    Box(
                        modifier = Modifier.weight(1f).height(52.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayNum in 1..maxDay) {
                            val isToday = dayNum == todayDay
                            val isJumat = col == 4 // kolom Jumat (Senin=0 ... Jumat=4, Sabtu=5, Ahad=6)
                            val (_, hDay, hMonthName) = HijriCalculator.gregorianToHijri(viewYear, viewMonth + 1, dayNum)
                            val isEvent = events.any { it.hijriDay == hDay && it.hijriMonth == HijriCalculator.gregorianToHijri(viewYear, viewMonth + 1, dayNum).second }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Hijri day (small, gold)
                                Text(
                                    "$hDay", style = MaterialTheme.typography.labelSmall,
                                    color = when {
                                        isToday -> KalenderColors.GoldLight
                                        isEvent -> KalenderColors.EventGreen
                                        else -> KalenderColors.GoldDim
                                    },
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                )
                                // Gregorian day (larger)
                                Text(
                                    "$dayNum", style = MaterialTheme.typography.bodyMedium,
                                    color = when {
                                        isToday -> KalenderColors.White
                                        isJumat -> KalenderColors.JumatGold
                                        else -> KalenderColors.TextMuted
                                    },
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
            rows++
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(KalenderColors.Gold))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Hijri", style = MaterialTheme.typography.labelSmall, color = KalenderColors.TextDim)
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(KalenderColors.White))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Masehi", style = MaterialTheme.typography.labelSmall, color = KalenderColors.TextDim)
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(KalenderColors.JumatGold))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Jumat", style = MaterialTheme.typography.labelSmall, color = KalenderColors.TextDim)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // === EVENTS THIS MONTH ===
        if (monthEvents.isNotEmpty()) {
            Text(
                "Hari Besar Islam",
                style = MaterialTheme.typography.titleMedium,
                color = KalenderColors.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            monthEvents.forEach { event ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = KalenderColors.CardBg),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, event.color.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(8.dp).clip(CircleShape).background(event.color)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            event.name, style = MaterialTheme.typography.bodyMedium,
                            color = KalenderColors.White, fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
