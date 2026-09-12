package com.sholatapp.ui.screens

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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.data.MahfudzotData
import com.sholatapp.model.PrayerInfo
import com.sholatapp.viewmodel.PrayerViewModel
import com.sholatapp.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Light theme colors for Home Screen (Namaz Vakti style) — TIDAK berubah sejak v2.0.
 * v2.3 hanya menata ulang penempatan & menambah konten, warna tetap sama
 * agar user tidak bingung (permintaan eksplisit).
 */
private object HomeLightColors {
    val Background = Color(0xFFF5F5F0)
    val Surface = Color(0xFFFFFFFF)
    val HeroGreen = Color(0xFF1B4D3E)
    val HeroGreenDark = Color(0xFF143A2E)
    val Gold = Color(0xFFD4AF37)
    val GoldLight = Color(0xFFF0E6C8)
    val TextPrimary = Color(0xFF1A1A1A)
    val TextSecondary = Color(0xFF6B7280)
    val TextTertiary = Color(0xFF9CA3AF)
    val TextOnHero = Color(0xFFFFFFFF)
    val TextGold = Color(0xFFB8941F)
    val Divider = Color(0xFFE5E7EB)
    val CardBorder = Color(0xFFF0F0EC)
    val ActiveIndicator = Color(0xFF1B4D3E)
    val PassedText = Color(0xFF9CA3AF)
    val CheckGreen = Color(0xFF22C55E)
}

/** Radius jendela waktu sholat "sedang berlangsung" (menit). */
private const val ONGOING_WINDOW_MIN = 30

@Composable
fun HomeScreen(
    viewModel: PrayerViewModel,
    userName: String,
    onKiblatClick: () -> Unit,
    onQuranClick: () -> Unit,
    onKalenderClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSalatClick: () -> Unit,
    onMutabaahClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val mahfudzot = remember { MahfudzotData.getTodayMahfudzot() }
    val schedule = uiState.prayerSchedule
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeLightColors.Background)
    ) {
        if (uiState.isLoading && schedule == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = HomeLightColors.HeroGreen)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // 1. Header: lokasi + tanggal Masehi & Hijriah
                HomeHeaderSection(
                    locationAddress = uiState.locationAddress,
                    onRefreshLocation = { viewModel.detectLocation() },
                    onNotificationClick = onNotificationClick,
                    onSettingsClick = onSettingsClick
                )

                // 2. Salam dinamis (pagi/siang/sore/malam)
                GreetingSection(userName = userName)

                // 3. Kartu error — PINDAH KE ATAS (v2.3): sebelumnya di dasar
                //    layar sehingga tidak terlihat. Kini menggantikan posisi
                //    hero saat lokasi gagal, lengkap dengan tombol Coba Lagi.
                if (uiState.errorMessage != null) {
                    ErrorCard(
                        message = uiState.errorMessage!!,
                        prominent = schedule == null,
                        onRetry = { viewModel.detectLocation() }
                    )
                }

                // 4. Hero Card: ring progres + jam + sholat berikutnya
                if (schedule != null) {
                    NextPrayerHeroCard(schedule = schedule, uiState = uiState)
                }

                // 5. Grid jadwal 6 waktu + tautan Lihat Detail
                if (schedule != null) {
                    PrayerTimesGrid(
                        schedule = schedule,
                        checkedPrayers = uiState.checkedPrayers,
                        onSalatClick = onSalatClick
                    )
                }

                // 6. Tindakan cepat: Kiblat · Al-Qur'an · Kalender · Mutabaah
                QuickActionsSection(
                    onKiblatClick = onKiblatClick,
                    onQuranClick = onQuranClick,
                    onKalenderClick = onKalenderClick,
                    onMutabaahClick = onMutabaahClick
                )

                // 7. Pelacakan sholat (checklist harian)
                if (schedule != null) {
                    PrayerTrackingSection(
                        schedule = schedule,
                        checkedPrayers = uiState.checkedPrayers,
                        onCheckPrayer = { viewModel.checkPrayer(it) }
                    )
                }

                // 8. Mahfudzot — PINDAH KE BAWAH (v2.3): penutup renungan,
                //    tinggi kartu menyesuaikan panjang teks, tanpa sumber.
                MahfudzotCard(mahfudzot = mahfudzot)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ==================== HEADER: LOKASI + TANGGAL ====================
@Composable
private fun HomeHeaderSection(
    locationAddress: String,
    onRefreshLocation: () -> Unit,
    onNotificationClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val today = Calendar.getInstance()
    val dayNames = arrayOf("Ahad", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
    val monthNames = arrayOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )
    val dateStr = "${dayNames[today.get(Calendar.DAY_OF_WEEK) - 1]}, ${today.get(Calendar.DAY_OF_MONTH)} ${monthNames[today.get(Calendar.MONTH)]} ${today.get(Calendar.YEAR)}"

    // Tanggal Hijriah (kalkulator offline yang sama dgn KalenderScreen)
    val hijriStr = remember {
        val h = HijriCalculator.toHijriInfo(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH) + 1,
            today.get(Calendar.DAY_OF_MONTH)
        )
        "${h.day} ${h.monthName} ${h.year} H"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(HomeLightColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = HomeLightColors.HeroGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = locationAddress,
                style = MaterialTheme.typography.labelMedium,
                color = HomeLightColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onNotificationClick, modifier = Modifier.size(36.dp)) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifikasi",
                    tint = HomeLightColors.TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
            IconButton(onClick = onSettingsClick, modifier = Modifier.size(36.dp)) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Pengaturan",
                    tint = HomeLightColors.TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        // Tanggal Masehi · Hijriah
        Text(
            text = "$dateStr  ·  $hijriStr",
            style = MaterialTheme.typography.bodySmall,
            color = HomeLightColors.TextTertiary
        )
    }
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = HomeLightColors.Divider
    )
}

// ==================== SALAM DINAMIS ====================
@Composable
private fun GreetingSection(userName: String) {
    val now = Calendar.getInstance()
    val waktu = when (now.get(Calendar.HOUR_OF_DAY)) {
        in 4..10 -> "pagi"
        in 11..14 -> "siang"
        in 15..17 -> "sore"
        else -> "malam"
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Ahlan wa sahlan, Selamat $waktu",
            style = MaterialTheme.typography.bodyMedium,
            color = HomeLightColors.TextSecondary
        )
        Text(
            text = if (userName.isNotBlank()) userName else "Muslim",
            style = MaterialTheme.typography.headlineMedium,
            color = HomeLightColors.TextPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

// ==================== KARTU ERROR + COBA LAGI ====================
@Composable
private fun ErrorCard(
    message: String,
    prominent: Boolean,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = if (prominent) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF991B1B),
                    modifier = Modifier.weight(1f)
                )
            }
            if (prominent) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Coba Lagi")
                }
            }
        }
    }
}

// ==================== HERO CARD: RING + SHOLAT BERIKUTNYA ====================
@Composable
private fun NextPrayerHeroCard(
    schedule: com.sholatapp.model.PrayerSchedule,
    uiState: UiState
) {
    val cal = Calendar.getInstance()
    val nowSeconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 +
            cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)
    val prayers = schedule.prayerList // 5 sholat wajib

    // Sholat yang sedang berlangsung (≤30 menit sejak adzan)
    val ongoing = prayers.firstOrNull {
        nowSeconds >= it.totalSeconds && nowSeconds < it.totalSeconds + ONGOING_WINDOW_MIN * 60
    }

    // Sholat berikutnya & sebelumnya (untuk progres ring)
    val nextIdx = prayers.indexOfFirst { it.totalSeconds > nowSeconds }
    val isTomorrowFajr = nextIdx == -1
    val nextPrayer = if (isTomorrowFajr) prayers.first() else prayers[nextIdx]
    val prevPrayer = if (nextIdx <= 0) prayers.last() else prayers[nextIdx - 1]

    val span = (nextPrayer.totalSeconds - prevPrayer.totalSeconds).let {
        if (it <= 0) it + 86400 else it
    }
    val elapsed = (nowSeconds - prevPrayer.totalSeconds).let {
        if (it < 0) it + 86400 else it
    }
    val progress = (elapsed.toFloat() / span.toFloat()).coerceIn(0f, 1f)

    // Jam digital + zona waktu otomatis (WIB/WITA/WIT sesuai perangkat)
    val zoneLabel = remember {
        val tz = SimpleDateFormat("zzz", Locale.getDefault()).format(Date())
        if (tz.startsWith("GMT")) null else tz
    }
    val countdown = uiState.countdownSeconds

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = HomeLightColors.HeroGreen),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Baris atas: cincin progres (kiri) + nama & jam sholat (kanan)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PrayerProgressRing(
                    progress = if (ongoing != null) 1f else progress,
                    clockText = uiState.currentTimeStr.ifEmpty { "--:--" },
                    zoneLabel = zoneLabel
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (ongoing != null) {
                        Text(
                            text = "WAKTU SHOLAT",
                            style = MaterialTheme.typography.labelMedium,
                            color = HomeLightColors.Gold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = ongoing.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = HomeLightColors.TextOnHero,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = ongoing.timeString,
                            style = MaterialTheme.typography.titleMedium,
                            color = HomeLightColors.Gold,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = "SHOLAT BERIKUTNYA",
                            style = MaterialTheme.typography.labelMedium,
                            color = HomeLightColors.TextOnHero.copy(alpha = 0.8f),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = nextPrayer.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = HomeLightColors.TextOnHero,
                                fontWeight = FontWeight.Bold
                            )
                            // Tag "Besok" saat menuju Subuh hari esok
                            if (isTomorrowFajr) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(HomeLightColors.GoldLight)
                                ) {
                                    Text(
                                        text = "Besok",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = HomeLightColors.HeroGreen,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = nextPrayer.timeString,
                            style = MaterialTheme.typography.titleMedium,
                            color = HomeLightColors.Gold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (ongoing != null) {
                // Status "sedang berlangsung" (v2.3)
                val minutesLeft = ((ongoing.totalSeconds + ONGOING_WINDOW_MIN * 60 - nowSeconds) / 60).coerceAtLeast(1)
                Text(
                    text = "Laksanakan segera · ±$minutesLeft menit lagi",
                    style = MaterialTheme.typography.labelMedium,
                    color = HomeLightColors.TextOnHero.copy(alpha = 0.8f)
                )
            } else {
                // Hitung mundur (jam : menit : detik) — lebar penuh, aman di layar kecil
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CountdownUnit(value = countdown / 3600)
                    ColonSeparator()
                    CountdownUnit(value = (countdown % 3600) / 60)
                    ColonSeparator()
                    CountdownUnit(value = countdown % 60)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "menuju adzan",
                    style = MaterialTheme.typography.labelSmall,
                    color = HomeLightColors.TextOnHero.copy(alpha = 0.6f)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun ColonSeparator() {
    Text(
        text = ":",
        style = MaterialTheme.typography.titleMedium,
        color = HomeLightColors.TextOnHero.copy(alpha = 0.6f),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 3.dp)
    )
}

/** Cincin progres emas dengan jam digital + zona waktu di tengah. */
@Composable
private fun PrayerProgressRing(
    progress: Float,
    clockText: String,
    zoneLabel: String?
) {
    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(112.dp)) {
            val strokeW = 9.dp.toPx()
            val diameter = size.minDimension - strokeW
            val topLeft = Offset(
                (size.width - diameter) / 2f,
                (size.height - diameter) / 2f
            )
            // Lintasan gelap (penuh)
            drawArc(
                color = HomeLightColors.HeroGreenDark,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )
            // Busur emas (progres waktu menuju sholat berikutnya)
            drawArc(
                color = HomeLightColors.Gold,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = clockText,
                style = MaterialTheme.typography.headlineSmall,
                color = HomeLightColors.TextOnHero,
                fontWeight = FontWeight.Bold
            )
            if (zoneLabel != null) {
                Text(
                    text = zoneLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = HomeLightColors.TextOnHero.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ==================== GRID JADWAL SHOLAT ====================
@Composable
private fun PrayerTimesGrid(
    schedule: com.sholatapp.model.PrayerSchedule,
    checkedPrayers: Set<String>,
    onSalatClick: () -> Unit
) {
    val cal = Calendar.getInstance()
    val currentSeconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60
    val nextPrayer = schedule.getNextPrayer(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Judul + tautan detail (v2.3: affordance yang sebelumnya hilang)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Jadwal Sholat Hari Ini",
                style = MaterialTheme.typography.titleMedium,
                color = HomeLightColors.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onSalatClick() }
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lihat Detail",
                    style = MaterialTheme.typography.labelMedium,
                    color = HomeLightColors.HeroGreen,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = HomeLightColors.HeroGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        val allTimes = schedule.allTimes

        // Baris 1: Subuh + Terbit
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrayerTimeCard(
                prayer = allTimes[0],
                isNext = nextPrayer?.nameKey == allTimes[0].nameKey,
                isPassed = allTimes[0].totalSeconds <= currentSeconds,
                isChecked = allTimes[0].nameKey in checkedPrayers,
                imsakTime = schedule.imsak?.timeString,
                modifier = Modifier.weight(1f)
            )
            PrayerTimeCard(
                prayer = allTimes[1],
                isNext = false,
                isPassed = allTimes[1].totalSeconds <= currentSeconds,
                isChecked = false,
                isSunrise = true,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Baris 2: Dzuhur + Ashar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrayerTimeCard(
                prayer = allTimes[2],
                isNext = nextPrayer?.nameKey == allTimes[2].nameKey,
                isPassed = allTimes[2].totalSeconds <= currentSeconds,
                isChecked = allTimes[2].nameKey in checkedPrayers,
                modifier = Modifier.weight(1f)
            )
            PrayerTimeCard(
                prayer = allTimes[3],
                isNext = nextPrayer?.nameKey == allTimes[3].nameKey,
                isPassed = allTimes[3].totalSeconds <= currentSeconds,
                isChecked = allTimes[3].nameKey in checkedPrayers,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Baris 3: Maghrib + Isya
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrayerTimeCard(
                prayer = allTimes[4],
                isNext = nextPrayer?.nameKey == allTimes[4].nameKey,
                isPassed = allTimes[4].totalSeconds <= currentSeconds,
                isChecked = allTimes[4].nameKey in checkedPrayers,
                modifier = Modifier.weight(1f)
            )
            PrayerTimeCard(
                prayer = allTimes[5],
                isNext = nextPrayer?.nameKey == allTimes[5].nameKey,
                isPassed = allTimes[5].totalSeconds <= currentSeconds,
                isChecked = allTimes[5].nameKey in checkedPrayers,
                modifier = Modifier.weight(1f)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun PrayerTimeCard(
    prayer: PrayerInfo,
    isNext: Boolean,
    isPassed: Boolean,
    isChecked: Boolean,
    isSunrise: Boolean = false,
    imsakTime: String? = null,
    modifier: Modifier = Modifier
) {
    val bgColor = when {
        isChecked -> Color(0xFFDCFCE7)
        isNext -> HomeLightColors.HeroGreen
        else -> HomeLightColors.Surface
    }
    val nameColor = when {
        isChecked -> Color(0xFF166534)
        isNext -> HomeLightColors.TextOnHero
        isPassed -> HomeLightColors.PassedText
        else -> HomeLightColors.TextPrimary
    }
    val timeColor = when {
        isChecked -> Color(0xFF166534)
        isNext -> HomeLightColors.Gold
        isPassed -> HomeLightColors.PassedText
        else -> HomeLightColors.TextSecondary
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(16.dp),
        elevation = if (isNext) CardDefaults.cardElevation(2.dp) else CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSunrise) {
                    Icon(
                        Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = if (isPassed) HomeLightColors.PassedText else Color(0xFFF59E0B),
                        modifier = Modifier.size(16.dp)
                    )
                } else if (isChecked) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF22C55E),
                        modifier = Modifier.size(16.dp)
                    )
                } else if (isNext) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(HomeLightColors.Gold)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = prayer.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = nameColor,
                    fontWeight = if (isNext) FontWeight.Bold else FontWeight.Medium
                )
                // Pil "Berikutnya" (v2.3) — memakai palet lama, tidak ada warna baru
                if (isNext && !isSunrise && !isChecked) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(HomeLightColors.GoldLight)
                    ) {
                        Text(
                            text = "Berikutnya",
                            style = MaterialTheme.typography.labelSmall,
                            color = HomeLightColors.HeroGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = prayer.timeString,
                style = MaterialTheme.typography.titleLarge,
                color = timeColor,
                fontWeight = FontWeight.Bold
            )
            if (imsakTime != null) {
                Text(
                    text = "Imsak $imsakTime",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isPassed) HomeLightColors.PassedText else HomeLightColors.TextSecondary
                )
            }
        }
    }
}

// ==================== TINDAKAN CEPAT ====================
@Composable
private fun QuickActionsSection(
    onKiblatClick: () -> Unit,
    onQuranClick: () -> Unit,
    onKalenderClick: () -> Unit,
    onMutabaahClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Tindakan Cepat",
            style = MaterialTheme.typography.titleMedium,
            color = HomeLightColors.TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))

        // v2.3: Zikir & Puasa keluar (keduanya punya tab sendiri di nav bawah),
        // Al-Qur'an masuk sebagai aksi cepat utama
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionItem(
                icon = Icons.Default.Explore,
                label = "Kiblat",
                onClick = onKiblatClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.MenuBook,
                label = "Al-Qur'an",
                onClick = onQuranClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.CalendarMonth,
                label = "Kalender",
                onClick = onKalenderClick,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // v2.10: pintu "Lainnya" DIHAPUS (keputusan user). Slotnya dinaiki
        // Mutabaah; Doa & Asmaul Husna pindah jadi kategori halaman Zikir;
        // halaman Tilawah lama dihapus (sudah diwakili Al-Qur'an lengkap).
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onMutabaahClick),
            colors = CardDefaults.cardColors(containerColor = HomeLightColors.Surface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(HomeLightColors.GoldLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.FactCheck,
                        contentDescription = "Mutabaah",
                        tint = HomeLightColors.HeroGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mutabaah",
                        style = MaterialTheme.typography.labelMedium,
                        color = HomeLightColors.TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Checklist ibadah harian & statistik istiqamah",
                        style = MaterialTheme.typography.labelSmall,
                        color = HomeLightColors.TextSecondary,
                        maxLines = 1
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = HomeLightColors.TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = HomeLightColors.Surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(HomeLightColors.GoldLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = HomeLightColors.HeroGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = HomeLightColors.TextPrimary,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

// ==================== PELACAKAN SHOLAT ====================
@Composable
private fun PrayerTrackingSection(
    schedule: com.sholatapp.model.PrayerSchedule,
    checkedPrayers: Set<String>,
    onCheckPrayer: (String) -> Unit
) {
    val cal = Calendar.getInstance()
    val currentSeconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60
    val prayers = schedule.prayerList
    val checkedCount = prayers.count { it.nameKey in checkedPrayers }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pelacakan Sholat",
                style = MaterialTheme.typography.titleMedium,
                color = HomeLightColors.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$checkedCount/${prayers.size}",
                style = MaterialTheme.typography.labelLarge,
                color = HomeLightColors.HeroGreen,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        val progress = if (prayers.isNotEmpty()) checkedCount.toFloat() / prayers.size else 0f
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .height(6.dp),
            color = HomeLightColors.HeroGreen,
            trackColor = HomeLightColors.Divider,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = HomeLightColors.Surface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            prayers.forEachIndexed { index, prayer ->
                val isChecked = prayer.nameKey in checkedPrayers

                PrayerTrackingItem(
                    prayer = prayer,
                    isChecked = isChecked,
                    isLast = index == prayers.lastIndex,
                    onCheck = { onCheckPrayer(prayer.nameKey) }
                )
            }
        }
    }
}

@Composable
private fun PrayerTrackingItem(
    prayer: PrayerInfo,
    isChecked: Boolean,
    isLast: Boolean,
    onCheck: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheck() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (isChecked) HomeLightColors.CheckGreen
                        else Color.Transparent
                    )
                    .border(
                        width = 2.dp,
                        color = if (isChecked) HomeLightColors.CheckGreen
                        else HomeLightColors.Divider,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = prayer.name,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isChecked) HomeLightColors.PassedText else HomeLightColors.TextPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = prayer.timeString,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isChecked) HomeLightColors.PassedText else HomeLightColors.TextSecondary
            )
        }

        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 40.dp),
                color = HomeLightColors.Divider,
                thickness = 1.dp
            )
        }
    }
}

// ==================== MAHFUDZOT (PENUTUP) ====================
/**
 * Kartu Mahfudzot di dasar Beranda (v2.3).
 * - Tanpa sumber/periwayat (permintaan user)
 * - Tinggi kartu mengikuti panjang teks (wrap content) — tidak ada pemotongan
 * - Gaya emas lama dipertahankan persis
 */
@Composable
private fun MahfudzotCard(mahfudzot: com.sholatapp.data.Mahfudzot) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = HomeLightColors.GoldLight),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Tanda kutip dekoratif
            Text(
                text = "\u201D",
                style = MaterialTheme.typography.headlineLarge,
                color = HomeLightColors.TextGold,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.height(24.dp)
            )
            // Teks Arab — rata kanan, tanpa batas baris agar tidak terpotong
            Text(
                text = mahfudzot.arabic,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                lineHeight = 34.sp,
                color = HomeLightColors.TextGold,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            // Arti
            Text(
                text = mahfudzot.meaning,
                style = MaterialTheme.typography.bodySmall,
                color = HomeLightColors.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}
