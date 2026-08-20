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
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sholatapp.model.PrayerInfo
import com.sholatapp.ui.theme.DarkColors
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

    // PAGI: setelah Subuh, sebelum Dzuhur
    // SIANG: setelah Dzuhur, sebelum Ashar
    // SENJA: setelah Ashar, sebelum/sesudah Isya
    // MALAM: setelah Isya, sebelum Subuh
    return when {
        currentSeconds < schedule.fajr.totalSeconds -> TimePeriod.MALAM
        currentSeconds < schedule.dhuhr.totalSeconds -> TimePeriod.PAGI
        currentSeconds < schedule.asr.totalSeconds -> TimePeriod.SIANG
        currentSeconds < schedule.isha.totalSeconds -> TimePeriod.SENJA
        else -> TimePeriod.MALAM
    }
}

/**
 * URL gambar background untuk setiap periode waktu.
 * Diambil dari Pinterest via oEmbed — resolusi penuh (originals).
 */
private object SalatBackgrounds {
    // Pagi: gambar masjid pagi — https://pin.it/66Y17Krlc
    // (pin ini sudah berisi 2 gambar yang digabung menjadi 1 komposit)
    val PAGI_MAIN = "https://i.pinimg.com/originals/95/47/8b/95478ba93b40f1b1cd3bf753fb231538.jpg"
    val PAGI_OVERLAY: String? = null // Tidak perlu overlay kedua, pin sudah komposit

    // Siang: 1 gambar — https://pin.it/1i627AH4f
    val SIANG = "https://i.pinimg.com/originals/b6/5e/a0/b65ea0f9aa98c5759b28533a3d7e1b13.jpg"

    // Senja: 1 gambar — https://pin.it/7bV5qysVW
    val SENJA = "https://i.pinimg.com/originals/6a/18/4b/6a184b782a26bb1ba5d5ab563a01fb28.jpg"

    // Malam: 1 gambar — https://pin.it/6rS9dbORX
    val MALAM = "https://i.pinimg.com/originals/44/04/2d/44042d86d7134f565d32e09d3ba6cd15.jpg"
}

/**
 * Warna aksen per periode — semua tetap dalam skema hijau + emas aplikasi.
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
}

@Composable
fun SalatScreen(
    viewModel: PrayerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val schedule = uiState.prayerSchedule
    val timePeriod = remember(schedule) { getTimePeriod(schedule) }

    // Background images based on time period
    val bgImageMain = remember(timePeriod) {
        when (timePeriod) {
            TimePeriod.PAGI -> SalatBackgrounds.PAGI_MAIN
            TimePeriod.SIANG -> SalatBackgrounds.SIANG
            TimePeriod.SENJA -> SalatBackgrounds.SENJA
            TimePeriod.MALAM -> SalatBackgrounds.MALAM
        }
    }

    // Check if we're currently in Subuh time (for animation trigger)
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
        // Background image layer
            AsyncImage(
                model = bgImageMain,
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
                            Color(0xCC0A1A0A),
                            Color(0xEE0A1A0A)
                        ),
                        endY = 900f
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
                            Color(0xDD0A1A0A),
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
                // Header with location
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = SalatPeriodColors.Gold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = uiState.locationAddress,
                        style = MaterialTheme.typography.bodySmall,
                        color = SalatPeriodColors.TextMuted,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Jadwal Salat",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SalatPeriodColors.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (schedule != null) {
                    // === SUBUH ANIMATION SECTION ===
                    if (isSubuhTime) {
                        SubuhAnimationCard(schedule = schedule)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // === NEXT PRAYER HERO ===
                    SalatNextPrayerHero(schedule = schedule, uiState = uiState)

                    Spacer(modifier = Modifier.height(16.dp))

                    // === CLOCK + CHECKLIST ROW ===
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Prayer checklist
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            val unchecked = schedule.prayerList.filter {
                                it.nameKey !in uiState.checkedPrayers
                            }
                            if (unchecked.isEmpty()) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    null,
                                    tint = SalatPeriodColors.Gold,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Semua sholat\nselesai",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SalatPeriodColors.Gold
                                )
                            } else {
                                unchecked.forEachIndexed { index, prayer ->
                                    val isNext = index == 0
                                    SalatCheckItem(
                                        prayer = prayer,
                                        isNext = isNext,
                                        onCheck = { viewModel.checkPrayer(it) }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Right: Analog clock
                        SalatAnalogClock(
                            modifier = Modifier.weight(1.2f),
                            uiState = uiState
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // === FULL SCHEDULE LIST ===
                    Text(
                        text = "Detail Waktu Sholat",
                        style = MaterialTheme.typography.titleMedium,
                        color = SalatPeriodColors.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = SalatPeriodColors.CardBg
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, SalatPeriodColors.CardBorder
                        )
                    ) {
                        schedule.allTimes.forEachIndexed { index, prayer ->
                            val isPassed = prayer.totalSeconds <= currentSeconds
                            val isChecked = prayer.nameKey in uiState.checkedPrayers

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        enabled = !isChecked && prayer.nameKey != "sunrise"
                                    ) {
                                        viewModel.checkPrayer(prayer.nameKey)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (prayer.nameKey == "sunrise") {
                                    Icon(
                                        Icons.Default.WbSunny,
                                        null,
                                        tint = SalatPeriodColors.Gold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else if (isChecked) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Box(
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
                                    color = if (isChecked) SalatPeriodColors.TextMuted
                                    else SalatPeriodColors.White,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
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
                                    color = Color(0x1A1B4D3E)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
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

// ==================== NEXT PRAYER HERO CARD ====================
@Composable
private fun SalatNextPrayerHero(
    schedule: com.sholatapp.model.PrayerSchedule,
    uiState: UiState
) {
    val cal = Calendar.getInstance()
    val nextPrayer = schedule.getNextPrayer(
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE)
    )

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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sholat Berikutnya",
                style = MaterialTheme.typography.labelLarge,
                color = SalatPeriodColors.TextMuted,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = nextPrayer?.name ?: "--",
                style = MaterialTheme.typography.headlineSmall,
                color = SalatPeriodColors.Gold,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = nextPrayer?.timeString ?: "--:--",
                style = MaterialTheme.typography.titleLarge,
                color = SalatPeriodColors.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            val h = uiState.countdownSeconds / 3600
            val m = (uiState.countdownSeconds % 3600) / 60
            val s = uiState.countdownSeconds % 60
            Text(
                text = String.format("%02d:%02d:%02d", h, m, s),
                style = MaterialTheme.typography.displaySmall,
                color = SalatPeriodColors.Gold,
                fontWeight = FontWeight.Light
            )
        }
    }
}

// ==================== CHECKLIST ITEMS ====================
@Composable
private fun SalatCheckItem(
    prayer: PrayerInfo,
    isNext: Boolean,
    onCheck: (String) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isNext) Color(0x33D4AF37) else Color.Transparent)
            .clickable(enabled = isNext) { onCheck(prayer.nameKey) }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(if (isNext) 32.dp else 24.dp)
                .then(if (isNext) Modifier.graphicsLayer {
                    scaleX = scale; scaleY = scale
                } else Modifier)
                .clip(CircleShape)
                .background(
                    if (isNext) Color(0x26D4AF37) else Color.Transparent,
                    CircleShape
                )
                .border(
                    width = if (isNext) 2.dp else 1.dp,
                    color = if (isNext) SalatPeriodColors.Gold
                    else Color(0x33FFFFFF),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isNext) {
                Text(
                    text = prayer.name.first().toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = SalatPeriodColors.Gold,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = prayer.name,
                style = if (isNext) MaterialTheme.typography.titleSmall
                else MaterialTheme.typography.bodyMedium,
                color = if (isNext) SalatPeriodColors.Gold
                else SalatPeriodColors.White,
                fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = prayer.timeString,
                style = MaterialTheme.typography.labelSmall,
                color = if (isNext) SalatPeriodColors.GoldLight
                else SalatPeriodColors.TextMuted
            )
        }
    }
}

// ==================== ANALOG CLOCK ====================
@Composable
private fun SalatAnalogClock(
    modifier: Modifier = Modifier,
    uiState: UiState
) {
    val schedule = uiState.prayerSchedule ?: return
    val cal = Calendar.getInstance()
    val nextPrayer = schedule.prayerList
        .firstOrNull { it.nameKey !in uiState.checkedPrayers }

    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        val textMeasurer = rememberTextMeasurer()

        Canvas(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
        ) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val radius = size.minDimension / 2f

            // Clock face - semi-transparent dark
            drawCircle(Color(0xB3112211), radius, center = Offset(cx, cy))
            drawCircle(
                SalatPeriodColors.Gold.copy(alpha = 0.4f),
                radius,
                center = Offset(cx, cy),
                style = Stroke(width = 2.dp.toPx())
            )

            // Tick marks
            for (i in 0..59) {
                val angle = Math.toRadians(i * 6.0 - 90.0)
                if (i % 5 == 0) {
                    val innerR = radius - 14.dp.toPx()
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
                    val innerR = radius - 8.dp.toPx()
                    val outerR = radius - 4.dp.toPx()
                    drawLine(
                        Color(0x33FFFFFF),
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

            // Numbers
            for (i in 1..12) {
                val angle = Math.toRadians(i * 30.0 - 90.0)
                val textR = radius - 24.dp.toPx()
                val x = cx + textR * cos(angle).toFloat()
                val y = cy + textR * sin(angle).toFloat()
                val textLayout = textMeasurer.measure(
                    text = i.toString(),
                    style = TextStyle(
                        color = SalatPeriodColors.Gold,
                        fontSize = 11.sp,
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

            // Alarm hands (green, behind current)
            if (nextPrayer != null) {
                val alarmH = nextPrayer.hour % 12
                val alarmHA = Math.toRadians(
                    alarmH * 30.0 + nextPrayer.minute * 0.5 - 90.0
                )
                val alarmMA = Math.toRadians(
                    nextPrayer.minute * 6.0 - 90.0
                )
                drawLine(
                    Color(0xFF4CAF50),
                    Offset(cx, cy),
                    Offset(
                        cx + radius * 0.4f * cos(alarmHA).toFloat(),
                        cy + radius * 0.4f * sin(alarmHA).toFloat()
                    ),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    Color(0xFF4CAF50),
                    Offset(cx, cy),
                    Offset(
                        cx + radius * 0.6f * cos(alarmMA).toFloat(),
                        cy + radius * 0.6f * sin(alarmMA).toFloat()
                    ),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Current time hands (white)
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

            // Center dot
            drawCircle(SalatPeriodColors.Gold, 4.dp.toPx(), center = Offset(cx, cy))
        }
    }
}
