package com.sholatapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sholatapp.R
import com.sholatapp.model.PrayerInfo
import com.sholatapp.viewmodel.PrayerViewModel
import com.sholatapp.viewmodel.UiState
import java.util.Calendar
import kotlin.math.*

/**
 * Waktu periode untuk menentukan background gambar.
 * PAGI: Subuh sampai sebelum Dzuhur
 * SIANG: Dzuhur sampai sebelum Ashar
 * SENJA: Ashar sampai Isya
 * MALAM: Setelah Isya sampai Subuh
 */
enum class TimePeriod(val label: String) {
    PAGI("Pagi"),
    SIANG("Siang"),
    SENJA("Senja"),
    MALAM("Malam")
}

/**
 * Menentukan periode waktu berdasarkan jadwal sholat dan waktu saat ini.
 */
private fun getTimePeriod(schedule: com.sholatapp.model.PrayerSchedule?): TimePeriod {
    if (schedule == null) return TimePeriod.MALAM
    val cal = Calendar.getInstance()
    val currentSeconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 +
        cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)

    return when {
        currentSeconds < schedule.fajr.totalSeconds -> TimePeriod.MALAM
        currentSeconds < schedule.dhuhr.totalSeconds -> TimePeriod.PAGI
        currentSeconds < schedule.asr.totalSeconds -> TimePeriod.SIANG
        currentSeconds < schedule.isha.totalSeconds -> TimePeriod.SENJA
        else -> TimePeriod.MALAM
    }
}

/** Jendela "waktu sholat sedang berlangsung" (menit sejak adzan) — selaras Beranda v2.3. */
private const val ONGOING_WINDOW_MIN = 30

/**
 * Warna aksen halaman Salat — semua tetap dalam skema hijau + emas.
 * v2.4: warna inline lama dipindahkan ke sini (NILAI TIDAK ADA YANG DIUBAH).
 */
private object SalatPeriodColors {
    val Green = Color(0xFF1B4D3E)
    val Gold = Color(0xFFD4AF37)
    val GoldLight = Color(0xFFFFD54F)
    val White = Color(0xFFFFFFFF)
    val GreenDark = Color(0xFF143A2E)
    val TextOnBg = Color(0xFFFFFFFF)
    val TextMuted = Color(0xFFB0BEC5)
    val SurfaceOverlay = Color(0xCC0A1A0A) // semi-transparent dark
    val CardBg = Color(0xB3112211) // semi-transparent dark green
    val CardBorder = Color(0x33D4AF37) // subtle gold border

    // v2.4 — konstanta tambahan (nilai dari warna inline lama)
    val CheckGreen = Color(0xFF4CAF50)
    val ErrorSoft = Color(0xFFE57373)
    val OverlayMedium = Color(0xCC0A1A0A)
    val OverlayDeep = Color(0xEE0A1A0A)
    val OverlayTop = Color(0xDD0A1A0A)
    val DividerGreen = Color(0x1A1B4D3E)
    val HandFaint = Color(0x33FFFFFF)
}

/** Latar foto lokal per periode — offline permanen, tanpa hotlink internet (v2.4). */
private fun backgroundResFor(period: TimePeriod): Int = when (period) {
    TimePeriod.PAGI -> R.drawable.bg_salat_pagi
    TimePeriod.SIANG -> R.drawable.bg_salat_siang
    TimePeriod.SENJA -> R.drawable.bg_salat_senja
    TimePeriod.MALAM -> R.drawable.bg_salat_malam
}

@Composable
fun SalatScreen(
    viewModel: PrayerViewModel,
    onKalenderClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val schedule = uiState.prayerSchedule

    // Periode latar dihitung ulang saat menit berganti (kunci = string "HH:mm")
    val timePeriod = remember(schedule, uiState.currentTimeStr) { getTimePeriod(schedule) }
    val bgRes = remember(timePeriod) { backgroundResFor(timePeriod) }

    val cal = Calendar.getInstance()
    val currentSeconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 +
        cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)
    val isSubuhTime = schedule != null &&
        currentSeconds >= schedule.fajr.totalSeconds &&
        currentSeconds < schedule.sunrise.totalSeconds

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Latar foto lokal dengan crossfade halus saat pergantian periode
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(bgRes)
                .crossfade(300)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark gradient overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            SalatPeriodColors.OverlayMedium,
                            SalatPeriodColors.OverlayDeep
                        )
                    )
                )
        )
        // Top dark overlay for header text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SalatPeriodColors.OverlayTop,
                            Color.Transparent
                        )
                    )
                )
        )

        if (uiState.isLoading && schedule == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SalatPeriodColors.Gold)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Judul halaman — lokasi & tanggal cukup ditampilkan di Beranda (v2.4)
                Text(
                    text = "Jadwal Salat",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SalatPeriodColors.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (schedule == null) {
                    // Lokasi gagal dideteksi — error state di ATAS + tombol coba lagi (v2.4)
                    SalatErrorCard(
                        message = uiState.errorMessage,
                        onRetry = { viewModel.detectLocation() }
                    )
                } else {
                    // === SUBUH ANIMATION SECTION ===
                    if (isSubuhTime) {
                        SubuhAnimationCard(schedule = schedule)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // === HERO: JAM ANALOG + SHOLAT BERIKUTNYA ===
                    SalatHeroCard(schedule = schedule, uiState = uiState)

                    Spacer(modifier = Modifier.height(20.dp))

                    // === DETAIL WAKTU SHOLAT (naik ke bawah hero, v2.4) ===
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Detail Waktu Sholat",
                            style = MaterialTheme.typography.titleMedium,
                            color = SalatPeriodColors.White,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        val done = schedule.prayerList.count { it.nameKey in uiState.checkedPrayers }
                        Text(
                            text = "$done/5 selesai",
                            style = MaterialTheme.typography.labelLarge,
                            color = SalatPeriodColors.Gold,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    SalatDetailCard(
                        schedule = schedule,
                        uiState = uiState,
                        currentSeconds = currentSeconds,
                        onToggleCheck = { nameKey ->
                            if (nameKey in uiState.checkedPrayers) viewModel.uncheckPrayer(nameKey)
                            else viewModel.checkPrayer(nameKey)
                        },
                        onKalenderClick = onKalenderClick
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ==================== PIL KECIL (Besok / Berikutnya) ====================
@Composable
private fun SalatPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(SalatPeriodColors.GoldLight)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = SalatPeriodColors.Green,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

// ==================== HERO: JAM ANALOG + SHOLAT BERIKUTNYA ====================
/**
 * Kartu hero v2.4:
 * - Kiri  : jam analog 5 jarum (3 waktu nyata + 2 alarm hijau) menggantikan ring
 * - Kanan : label + nama + waktu sholat berikutnya, pil "Besok" saat menuju Subuh esok
 * - Bawah : hitung mundur 3 kotak (jam : menit : detik) / status "WAKTU SHOLAT"
 */
@Composable
private fun SalatHeroCard(
    schedule: com.sholatapp.model.PrayerSchedule,
    uiState: UiState
) {
    val cal = Calendar.getInstance()
    val nowSeconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 +
        cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)
    val prayers = schedule.prayerList // 5 sholat wajib

    // Sholat yang sedang berlangsung (<=30 menit sejak adzan)
    val ongoing = prayers.firstOrNull {
        nowSeconds >= it.totalSeconds && nowSeconds < it.totalSeconds + ONGOING_WINDOW_MIN * 60
    }

    val nextIdx = prayers.indexOfFirst { it.totalSeconds > nowSeconds }
    val isTomorrowFajr = nextIdx == -1
    val nextPrayer = if (isTomorrowFajr) prayers.first() else prayers[nextIdx]

    Card(
        colors = CardDefaults.cardColors(
            containerColor = SalatPeriodColors.CardBg
        ),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, SalatPeriodColors.CardBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Baris atas: jam analog (kiri) + info sholat berikutnya (kanan)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SalatAnalogClock(uiState = uiState)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Panah hijau = sholat berikutnya",
                        style = TextStyle(
                            fontSize = 9.sp,
                            color = SalatPeriodColors.TextMuted,
                            textAlign = TextAlign.Center
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (ongoing != null) {
                        Text(
                            text = "WAKTU SHOLAT",
                            style = MaterialTheme.typography.labelMedium,
                            color = SalatPeriodColors.Gold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = ongoing.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = SalatPeriodColors.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = ongoing.timeString,
                            style = MaterialTheme.typography.titleMedium,
                            color = SalatPeriodColors.Gold,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = "SHOLAT BERIKUTNYA",
                            style = MaterialTheme.typography.labelMedium,
                            color = SalatPeriodColors.TextMuted,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = nextPrayer.name,
                                style = MaterialTheme.typography.headlineSmall,
                                color = SalatPeriodColors.White,
                                fontWeight = FontWeight.Bold
                            )
                            if (isTomorrowFajr) {
                                Spacer(modifier = Modifier.width(8.dp))
                                SalatPill(text = "Besok")
                            }
                        }
                        Text(
                            text = nextPrayer.timeString,
                            style = MaterialTheme.typography.titleMedium,
                            color = SalatPeriodColors.Gold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (ongoing != null) {
                // Status "sedang berlangsung" — selaras Beranda v2.3
                val minutesLeft = ((ongoing.totalSeconds + ONGOING_WINDOW_MIN * 60 - nowSeconds) / 60)
                    .coerceAtLeast(1)
                Text(
                    text = "Laksanakan segera · ±$minutesLeft menit lagi",
                    style = MaterialTheme.typography.labelMedium,
                    color = SalatPeriodColors.TextOnBg.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            } else {
                // Hitung mundur (jam : menit : detik) dalam 3 kotak
                val countdown = uiState.countdownSeconds
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SalatCountdownUnit(value = countdown / 3600, label = "JAM")
                    SalatColonSeparator()
                    SalatCountdownUnit(value = (countdown % 3600) / 60, label = "MENIT")
                    SalatColonSeparator()
                    SalatCountdownUnit(value = countdown % 60, label = "DETIK")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "menuju adzan",
                    style = MaterialTheme.typography.labelSmall,
                    color = SalatPeriodColors.TextOnBg.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ==================== KOTAK HITUNG MUNDUR ====================
@Composable
private fun SalatCountdownUnit(value: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.06f))
                .border(1.dp, SalatPeriodColors.CardBorder, RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "%02d".format(value),
                style = MaterialTheme.typography.titleMedium,
                color = SalatPeriodColors.Gold,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = TextStyle(fontSize = 9.sp, color = SalatPeriodColors.TextMuted)
        )
    }
}

@Composable
private fun SalatColonSeparator() {
    Text(
        text = ":",
        style = MaterialTheme.typography.titleMedium,
        color = SalatPeriodColors.TextOnBg.copy(alpha = 0.6f),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 3.dp)
    )
}

// ==================== JAM ANALOG 5 JARUM ====================
/**
 * Jam analog 5 jarum — fitur inti halaman ini (dipertahankan, desain dirapikan v2.4):
 * - 3 jarum waktu nyata : jam & menit (putih), detik (emas)
 * - 2 jarum alarm hijau : jam & menit sholat berikutnya + titik hijau di tepi
 * Wajah minimalis: angka hanya 12 / 3 / 6 / 9 agar tetap terbaca di ukuran kecil.
 */
@Composable
private fun SalatAnalogClock(uiState: UiState) {
    val schedule = uiState.prayerSchedule ?: return
    val cal = Calendar.getInstance()
    // Alarm menunjuk sholat berikutnya; bila semua sudah dicentang → Subuh esok
    val nextPrayer = schedule.prayerList.firstOrNull { it.nameKey !in uiState.checkedPrayers }
        ?: schedule.prayerList.first()

    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = Modifier.size(112.dp)) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = size.minDimension / 2f

        // Wajah jam + bingkai emas tipis
        drawCircle(SalatPeriodColors.CardBg, radius, center = Offset(cx, cy))
        drawCircle(
            SalatPeriodColors.CardBorder,
            radius,
            center = Offset(cx, cy),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Tick menit (tipis) & jam (emas)
        for (i in 0..59) {
            val angle = Math.toRadians(i * 6.0 - 90.0)
            if (i % 5 == 0) {
                val innerR = radius - 12.dp.toPx()
                val outerR = radius - 4.dp.toPx()
                drawLine(
                    SalatPeriodColors.Gold,
                    Offset(
                        cx + innerR * cos(angle).toFloat(),
                        cy + innerR * sin(angle).toFloat()
                    ),
                    Offset(
                        cx + outerR * cos(angle).toFloat(),
                        cy + outerR * sin(angle).toFloat()
                    ),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            } else {
                val innerR = radius - 7.dp.toPx()
                val outerR = radius - 4.dp.toPx()
                drawLine(
                    SalatPeriodColors.HandFaint,
                    Offset(
                        cx + innerR * cos(angle).toFloat(),
                        cy + innerR * sin(angle).toFloat()
                    ),
                    Offset(
                        cx + outerR * cos(angle).toFloat(),
                        cy + outerR * sin(angle).toFloat()
                    ),
                    strokeWidth = 0.8.dp.toPx()
                )
            }
        }

        // Angka minimalis: 12 / 3 / 6 / 9
        listOf(12, 3, 6, 9).forEach { n ->
            val degree = if (n == 12) 0 else n * 30
            val angle = Math.toRadians(degree.toDouble() - 90.0)
            val textR = radius - 21.dp.toPx()
            val x = cx + textR * cos(angle).toFloat()
            val y = cy + textR * sin(angle).toFloat()
            val textLayout = textMeasurer.measure(
                text = n.toString(),
                style = TextStyle(
                    color = SalatPeriodColors.Gold.copy(alpha = 0.85f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    x - textLayout.size.width / 2f,
                    y - textLayout.size.height / 2f
                )
            )
        }

        // Jarum alarm hijau: jam (pendek) + menit sholat berikutnya
        val alarmHA = Math.toRadians(
            nextPrayer.hour % 12 * 30.0 + nextPrayer.minute * 0.5 - 90.0
        )
        val alarmMA = Math.toRadians(nextPrayer.minute * 6.0 - 90.0)
        drawLine(
            SalatPeriodColors.CheckGreen,
            Offset(cx, cy),
            Offset(
                cx + radius * 0.38f * cos(alarmHA).toFloat(),
                cy + radius * 0.38f * sin(alarmHA).toFloat()
            ),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            SalatPeriodColors.CheckGreen,
            Offset(cx, cy),
            Offset(
                cx + radius * 0.55f * cos(alarmMA).toFloat(),
                cy + radius * 0.55f * sin(alarmMA).toFloat()
            ),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        // Titik hijau di tepi posisi sholat berikutnya
        drawCircle(
            SalatPeriodColors.CheckGreen,
            3.dp.toPx(),
            center = Offset(
                cx + (radius - 3.dp.toPx()) * cos(alarmHA).toFloat(),
                cy + (radius - 3.dp.toPx()) * sin(alarmHA).toFloat()
            )
        )

        // Jarum waktu nyata: jam & menit (putih), detik (emas)
        val nowH12 = cal.get(Calendar.HOUR_OF_DAY) % 12
        val hourAngle = Math.toRadians(
            nowH12 * 30.0 + cal.get(Calendar.MINUTE) * 0.5 - 90.0
        )
        val minuteAngle = Math.toRadians(
            cal.get(Calendar.MINUTE) * 6.0 + cal.get(Calendar.SECOND) * 0.1 - 90.0
        )
        val secondAngle = Math.toRadians(
            cal.get(Calendar.SECOND) * 6.0 - 90.0
        )

        drawLine(
            Color.White,
            Offset(cx, cy),
            Offset(
                cx + radius * 0.45f * cos(hourAngle).toFloat(),
                cy + radius * 0.45f * sin(hourAngle).toFloat()
            ),
            strokeWidth = 3.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            Color.White,
            Offset(cx, cy),
            Offset(
                cx + radius * 0.65f * cos(minuteAngle).toFloat(),
                cy + radius * 0.65f * sin(minuteAngle).toFloat()
            ),
            strokeWidth = 2.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            SalatPeriodColors.Gold,
            Offset(cx, cy),
            Offset(
                cx + radius * 0.7f * cos(secondAngle).toFloat(),
                cy + radius * 0.7f * sin(secondAngle).toFloat()
            ),
            strokeWidth = 1.2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Titik tengah emas
        drawCircle(SalatPeriodColors.Gold, 4.dp.toPx(), center = Offset(cx, cy))
    }
}

// ==================== DETAIL WAKTU SHOLAT ====================
/**
 * Kartu detail v2.4:
 * - Imsak & Terbit info-only (tidak bisa dicentang)
 * - 5 sholat wajib: ketuk untuk centang, ketuk lagi untuk membatalkan (undo)
 * - Pil "Berikutnya"/"Besok" pada baris sholat yang akan datang
 * - Footer menuju Kalender Bulanan
 */
@Composable
private fun SalatDetailCard(
    schedule: com.sholatapp.model.PrayerSchedule,
    uiState: UiState,
    currentSeconds: Int,
    onToggleCheck: (String) -> Unit,
    onKalenderClick: () -> Unit
) {
    val cal = Calendar.getInstance()
    val nextPrayer = schedule.getNextPrayer(
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE)
    )
    // Semua sudah lewat → getNextPrayer membungkus ke Subuh; tandai sebagai "Besok"
    val isTomorrowFajr = nextPrayer != null &&
        nextPrayer.nameKey == PrayerInfo.FAJR &&
        nextPrayer.totalSeconds <= currentSeconds

    Card(
        colors = CardDefaults.cardColors(
            containerColor = SalatPeriodColors.CardBg
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, SalatPeriodColors.CardBorder
        )
    ) {
        Column {
            // Baris Imsak (10 menit sebelum Subuh) — hanya info, tidak bisa dicentang
            schedule.imsak?.let { imsak ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.WbTwilight,
                        null,
                        tint = SalatPeriodColors.Gold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = imsak.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = SalatPeriodColors.TextMuted,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = imsak.timeString,
                        style = MaterialTheme.typography.titleMedium,
                        color = SalatPeriodColors.Gold,
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(start = 50.dp),
                    color = SalatPeriodColors.DividerGreen
                )
            }

            schedule.allTimes.forEachIndexed { index, prayer ->
                val isSunrise = prayer.nameKey == PrayerInfo.SUNRISE
                val isChecked = prayer.nameKey in uiState.checkedPrayers
                val isPassed = prayer.totalSeconds <= currentSeconds
                val isNext = nextPrayer != null && prayer.nameKey == nextPrayer.nameKey

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isSunrise) { onToggleCheck(prayer.nameKey) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when {
                        isSunrise -> Icon(
                            Icons.Default.WbSunny,
                            null,
                            tint = SalatPeriodColors.Gold,
                            modifier = Modifier.size(20.dp)
                        )
                        isChecked -> Icon(
                            Icons.Default.CheckCircle,
                            null,
                            tint = SalatPeriodColors.CheckGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        else -> Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 1.5.dp,
                                    color = SalatPeriodColors.CardBorder,
                                    shape = CircleShape
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = prayer.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isChecked || (isPassed && !isChecked && !isNext))
                            SalatPeriodColors.TextMuted
                        else SalatPeriodColors.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    if (isNext) {
                        SalatPill(text = if (isTomorrowFajr) "Besok" else "Berikutnya")
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = prayer.timeString,
                        style = MaterialTheme.typography.titleMedium,
                        color = when {
                            isChecked -> SalatPeriodColors.TextMuted
                            isPassed && !isChecked -> SalatPeriodColors.TextMuted
                            else -> SalatPeriodColors.Gold
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
                if (index < schedule.allTimes.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 50.dp),
                        color = SalatPeriodColors.DividerGreen
                    )
                }
            }

            // Footer: jembatan ke Kalender Bulanan
            HorizontalDivider(color = SalatPeriodColors.DividerGreen)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onKalenderClick() }
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lihat Kalender Bulanan ›",
                    style = MaterialTheme.typography.labelLarge,
                    color = SalatPeriodColors.Gold,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ==================== ERROR STATE ====================
@Composable
private fun SalatErrorCard(
    message: String?,
    onRetry: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = SalatPeriodColors.CardBg
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, SalatPeriodColors.CardBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = SalatPeriodColors.ErrorSoft,
                modifier = Modifier.size(34.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Jadwal tidak tersedia",
                style = MaterialTheme.typography.titleMedium,
                color = SalatPeriodColors.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message ?: "Lokasi tidak dapat dideteksi. Periksa GPS dan izin lokasi, lalu coba lagi.",
                style = MaterialTheme.typography.bodySmall,
                color = SalatPeriodColors.TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SalatPeriodColors.Gold,
                    contentColor = SalatPeriodColors.Green
                )
            ) {
                Text(text = "Coba Lagi", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// ==================== SUBUH ANIMATION CARD ====================
/**
 * Animasi khusus untuk waktu Subuh:
 * - Awalnya elemen (nama Subuh + waktu) berada di sisi KIRI dengan warna PUTIH
 * - Saat memasuki waktu Subuh, elemen BERGESER ke KANAN
 * - Saat bergeser: warna berubah dari PUTIH menjadi EMAS (teks) dan HIJAU (elemen)
 */
@Composable
private fun SubuhAnimationCard(schedule: com.sholatapp.model.PrayerSchedule) {
    // Animate offset from left (0f) to right (1f)
    val transition = updateTransition(
        targetState = true, // selalu true karena komponen hanya muncul saat Subuh
        label = "subuhSlide"
    )

    val slideOffset by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 2000, easing = EaseInOutCubic)
        },
        label = "slideOffset"
    ) { 1f } // 0 = kiri, 1 = kanan

    // Animate colors: White → Gold (text), White → Green (accent)
    val textTargetColor by transition.animateColor(
        transitionSpec = {
            tween(durationMillis = 2000, delayMillis = 800)
        },
        label = "textColor"
    ) { SalatPeriodColors.Gold }

    val accentTargetColor by transition.animateColor(
        transitionSpec = {
            tween(durationMillis = 2000, delayMillis = 800)
        },
        label = "accentColor"
    ) { SalatPeriodColors.Green }

    val startTextColor = SalatPeriodColors.White
    val startAccentColor = SalatPeriodColors.White

    val currentTextColor = lerp(startTextColor, textTargetColor, slideOffset)
    val currentAccentColor = lerp(startAccentColor, accentTargetColor, slideOffset)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = SalatPeriodColors.CardBg
        ),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, currentAccentColor.copy(alpha = 0.5f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // Slide the content from left to right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (slideOffset * 80).dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Glow circle indicator
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    currentAccentColor.copy(alpha = 0.6f),
                                    currentAccentColor.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Brightness5,
                        contentDescription = null,
                        tint = currentTextColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Subuh",
                        style = MaterialTheme.typography.headlineSmall,
                        color = currentTextColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = schedule.fajr.timeString,
                        style = MaterialTheme.typography.titleLarge,
                        color = currentTextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Waktu sholat Subuh telah masuk",
                        style = MaterialTheme.typography.bodySmall,
                        color = SalatPeriodColors.TextMuted
                    )
                }
            }
        }
    }
}
