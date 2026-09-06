package com.sholatapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.sholatapp.model.PrayerSchedule
import com.sholatapp.viewmodel.PrayerViewModel
import com.sholatapp.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.*

// ==================== FASTING COLORS ====================
private object PuasaColors {
    val Background = Color(0xFF0A1A0A)
    val Gold = Color(0xFFD4AF37)
    val GoldLight = Color(0xFFFFD54F)
    val Green = Color(0xFF1B4D3E)
    val GreenLight = Color(0xFF4CAF50)
    val White = Color(0xFFFFFFFF)
    val TextMuted = Color(0xFFA5D6A7)
    val TextDim = Color(0xFF6B9B6B)
    val Surface = Color(0xFF112211)
    val CardBg = Color(0xB3112211)
    val CardBorder = Color(0x33D4AF37)
    val FastedGreen = Color(0xFF4CAF50)
    val MissedRed = Color(0xFFEF5350)
}

@Composable
fun PuasaScreen(
    viewModel: PrayerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val schedule = uiState.prayerSchedule
    val cal = Calendar.getInstance()
    val todayStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

    // Puasa prefs
    val puasaPrefs = remember { 
        viewModel.getApplication<Application>()
            .getSharedPreferences("puasa_prefs", android.content.Context.MODE_PRIVATE)
    }
    // refreshKey dipakai agar UI langsung diperbarui setelah toggle puasa
    var refreshKey by remember { mutableIntStateOf(0) }
    val isFastingToday = remember(todayStr, refreshKey) {
        puasaPrefs.getStringSet("fasted_dates", emptySet())?.contains(todayStr) == true
    }

    // Current viewing month
    var viewYear by remember { mutableIntStateOf(cal.get(Calendar.YEAR)) }
    var viewMonth by remember { mutableIntStateOf(cal.get(Calendar.MONTH)) }

    // Get fasted dates for viewing month
    val fastedDates = remember(viewYear, viewMonth, refreshKey) {
        val allDates = puasaPrefs.getStringSet("fasted_dates", emptySet()) ?: emptySet()
        val calMonth = Calendar.getInstance().apply { set(viewYear, viewMonth, 1) }
        val maxDay = calMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        val monthDates = mutableSetOf<Int>()
        for (d in 1..maxDay) {
            val dateStr = String.format("%04d%02d%02d", viewYear, viewMonth + 1, d)
            if (dateStr in allDates) monthDates.add(d)
        }
        monthDates
    }

    // Count fasted days this month
    val fastedCountThisMonth = fastedDates.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PuasaColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            "Pelacak Puasa",
            style = MaterialTheme.typography.headlineMedium,
            color = PuasaColors.Gold,
            fontWeight = FontWeight.Bold
        )
        Text(
            uiState.locationAddress,
            style = MaterialTheme.typography.bodySmall,
            color = PuasaColors.TextMuted
        )

        Spacer(modifier = Modifier.height(20.dp))

        // === IMSAK & IFTAR COUNTDOWN ===
        if (schedule != null) {
            val currentSeconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 +
                cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)

            val isRamadhanTime = currentSeconds < schedule.fajr.totalSeconds ||
                currentSeconds >= schedule.maghrib.totalSeconds

            // Imsak card
            PuasaCountdownCard(
                label = "Imsak (Subuh)",
                prayerInfo = schedule.fajr,
                currentSeconds = currentSeconds,
                isBefore = currentSeconds < schedule.fajr.totalSeconds,
                icon = Icons.Default.Brightness5
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Iftar card
            PuasaCountdownCard(
                label = "Iftar (Maghrib)",
                prayerInfo = schedule.maghrib,
                currentSeconds = currentSeconds,
                isBefore = currentSeconds < schedule.maghrib.totalSeconds,
                icon = Icons.Default.Nightlight
            )

            Spacer(modifier = Modifier.height(20.dp))

            // === MARK FASTING TODAY ===
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isFastingToday) Color(0x334CAF50) else PuasaColors.CardBg
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, if (isFastingToday) PuasaColors.FastedGreen else PuasaColors.CardBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val dates = puasaPrefs.getStringSet("fasted_dates", emptySet())?.toMutableSet() ?: mutableSetOf()
                            if (isFastingToday) {
                                dates.remove(todayStr)
                            } else {
                                dates.add(todayStr)
                            }
                            puasaPrefs.edit().putStringSet("fasted_dates", dates).apply()
                            refreshKey++ // trigger recompose agar UI langsung berubah
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (isFastingToday) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        null,
                        tint = if (isFastingToday) PuasaColors.FastedGreen else PuasaColors.Gold,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            if (isFastingToday) "Hari ini sedang berpuasa" else "Tandai puasa hari ini",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isFastingToday) PuasaColors.FastedGreen else PuasaColors.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date()),
                            style = MaterialTheme.typography.bodySmall,
                            color = PuasaColors.TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // === MONTHLY CALENDAR ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (viewMonth == 0) { viewMonth = 11; viewYear-- }
                    else viewMonth--
                }) {
                    Icon(Icons.Default.ChevronLeft, null, tint = PuasaColors.Gold)
                }
                val monthNames = arrayOf(
                    "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                    "Juli", "Agustus", "September", "Oktober", "November", "Desember"
                )
                Text(
                    "${monthNames[viewMonth]} $viewYear",
                    style = MaterialTheme.typography.titleMedium,
                    color = PuasaColors.White,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = {
                    if (viewMonth == 11) { viewMonth = 0; viewYear++ }
                    else viewMonth++
                }) {
                    Icon(Icons.Default.ChevronRight, null, tint = PuasaColors.Gold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Day headers
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("S", "S", "R", "K", "J", "S", "M").forEach { day ->
                    Text(
                        day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = PuasaColors.TextDim,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Calendar grid
            val calMonth = Calendar.getInstance().apply { set(viewYear, viewMonth, 1) }
            val maxDay = calMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            val firstDayOfWeek = (calMonth.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Mon=0
            val todayDay = if (viewYear == cal.get(Calendar.YEAR) && viewMonth == cal.get(Calendar.MONTH))
                cal.get(Calendar.DAY_OF_MONTH) else -1

            var rows = 0
            for (startIdx in 0 until maxDay step 7) {
                if (rows > 0) Spacer(modifier = Modifier.height(2.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0..6) {
                        val dayNum = startIdx + col - firstDayOfWeek + 1
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (dayNum in 1..maxDay) {
                                val isFasted = dayNum in fastedDates
                                val isToday = dayNum == todayDay
                                val isSunnah = isPuasaSunnahDay(viewYear, viewMonth, dayNum)
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isToday && isFasted -> PuasaColors.FastedGreen
                                                isToday -> PuasaColors.Gold.copy(alpha = 0.3f)
                                                isFasted -> PuasaColors.Green.copy(alpha = 0.5f)
                                                else -> Color.Transparent
                                            }
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isSunnah && !isFasted && !isToday)
                                                PuasaColors.Gold.copy(alpha = 0.55f) else Color.Transparent,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "$dayNum",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = when {
                                            isToday && isFasted -> PuasaColors.White
                                            isToday -> PuasaColors.Gold
                                            isFasted -> PuasaColors.White
                                            col == 4 -> PuasaColors.Gold.copy(alpha = 0.8f) // Jumat
                                            isSunnah -> PuasaColors.GoldLight
                                            else -> PuasaColors.TextMuted
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
                Box(
                    modifier = Modifier.size(10.dp).clip(CircleShape)
                        .background(PuasaColors.FastedGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Puasa", style = MaterialTheme.typography.labelSmall, color = PuasaColors.TextDim)
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier.size(10.dp).clip(CircleShape)
                        .background(PuasaColors.Gold.copy(alpha = 0.3f))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Hari ini", style = MaterialTheme.typography.labelSmall, color = PuasaColors.TextDim)
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .border(1.dp, PuasaColors.Gold.copy(alpha = 0.55f), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Puasa sunnah", style = MaterialTheme.typography.labelSmall, color = PuasaColors.TextDim)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Info puasa sunnah
            Card(
                colors = CardDefaults.cardColors(containerColor = PuasaColors.CardBg),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PuasaColors.CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Info,
                        null,
                        tint = PuasaColors.Gold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Hari berlingkar emas adalah anjuran puasa sunnah: Senin & Kamis, serta hari Ayyamul Bidh (13, 14, 15 bulan Hijriah).",
                        style = MaterialTheme.typography.labelSmall,
                        color = PuasaColors.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // === STATISTICS ===
            Text(
                "Statistik Bulan Ini",
                style = MaterialTheme.typography.titleMedium,
                color = PuasaColors.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = PuasaColors.CardBg),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PuasaColors.CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(value = "$fastedCountThisMonth", label = "Hari Puasa")
                    StatItem(value = "$maxDay", label = "Total Hari")
                    val pct = if (maxDay > 0) (fastedCountThisMonth * 100 / maxDay) else 0
                    StatItem(value = "$pct%", label = "Persentase")
                }
            }

            // Progress bar
            Spacer(modifier = Modifier.height(8.dp))
            val progress = if (maxDay > 0) fastedCountThisMonth.toFloat() / maxDay else 0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .height(8.dp),
                color = PuasaColors.Gold,
                trackColor = PuasaColors.Surface
            )
        } else {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PuasaColors.Gold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PuasaCountdownCard(
    label: String,
    prayerInfo: com.sholatapp.model.PrayerInfo,
    currentSeconds: Int,
    isBefore: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    val diff = prayerInfo.totalSeconds - currentSeconds
    val remaining = if (isBefore && diff > 0) diff else 0
    val h = remaining / 3600
    val m = (remaining % 3600) / 60
    val s = remaining % 60

    Card(
        colors = CardDefaults.cardColors(containerColor = PuasaColors.CardBg),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PuasaColors.CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PuasaColors.Gold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = PuasaColors.Gold, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.labelMedium, color = PuasaColors.TextMuted)
                Text(
                    prayerInfo.timeString,
                    style = MaterialTheme.typography.titleLarge,
                    color = PuasaColors.White,
                    fontWeight = FontWeight.Bold
                )
            }
            if (isBefore) {
                Text(
                    String.format("%02d:%02d:%02d", h, m, s),
                    style = MaterialTheme.typography.headlineSmall,
                    color = PuasaColors.Gold,
                    fontWeight = FontWeight.Light
                )
            } else {
                Text(
                    "Telah masuk",
                    style = MaterialTheme.typography.bodySmall,
                    color = PuasaColors.TextDim
                )
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value, style = MaterialTheme.typography.headlineSmall,
            color = PuasaColors.Gold, fontWeight = FontWeight.Bold
        )
        Text(
            label, style = MaterialTheme.typography.labelSmall,
            color = PuasaColors.TextMuted
        )
    }
}

/**
 * Cek apakah suatu tanggal adalah hari anjuran puasa sunnah:
 * - Senin & Kamis (puasa sunnah mingguan)
 * - Ayyamul Bidh: tanggal Hijriah 13, 14, 15 (dihitung dengan kalender Islam,
 *   android.icu tersedia sejak API 24 — sama dengan minSdk aplikasi)
 */
private fun isPuasaSunnahDay(year: Int, month: Int, day: Int): Boolean {
    val cal = Calendar.getInstance().apply { set(year, month, day) }
    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
    if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.THURSDAY) return true

    // Ayyamul Bidh — tanggal 13-15 Hijriah
    return try {
        val islamic = android.icu.util.IslamicCalendar()
        islamic.time = cal.time
        val hijriDay = islamic.get(android.icu.util.IslamicCalendar.DAY_OF_MONTH)
        hijriDay in 13..15
    } catch (e: Exception) {
        false
    }
}
