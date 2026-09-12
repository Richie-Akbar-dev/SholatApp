package com.sholatapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.calculation.SunnahDayCalculator
import com.sholatapp.data.DoaData
import com.sholatapp.model.DoaItem
import com.sholatapp.model.PrayerSchedule
import com.sholatapp.viewmodel.PrayerViewModel
import java.text.SimpleDateFormat
import java.util.*

// ==================== PUASA COLORS (v2.7 — tema terang, konsisten dengan palet aplikasi) ====================
private object PuasaColors {
    val Background = Color(0xFFF5F5F0)
    val Surface = Color(0xFFFFFFFF)
    val Primary = Color(0xFF1B4D3E)
    val PrimaryDark = Color(0xFF143A2E)
    val Gold = Color(0xFFA97C0E)
    val GoldBright = Color(0xFFE9C46A)   // emas terang utk teks di atas hero gelap
    val GoldSoft = Color(0x1AA97C0E)     // 10% emas utk latar chip
    val TextDark = Color(0xFF1C1B16)
    val TextMuted = Color(0xFF75806F)
    val GreenSoft = Color(0xFFE8F5E9)
    val CheckGreen = Color(0xFF4CAF50)
    val Divider = Color(0xFFE7E7DE)
    val HeroTrack = Color(0x33FFFFFF)
    val HeroChipBg = Color(0x26FFFFFF)
    val ErrorSoft = Color(0xFFE57373)
}

private val MONTH_NAMES = arrayOf(
    "Januari", "Februari", "Maret", "April", "Mei", "Juni",
    "Juli", "Agustus", "September", "Oktober", "November", "Desember"
)

/**
 * Halaman Puasa (v2.7) — rombak total sesuai mockup & keputusan user:
 * - Tema terang (hero hijau tua + konten putih), judul tanpa lokasi.
 * - Hero satuan cerdas: MENUJU IMSAK / MENUJU BERBUKA, countdown LIVE 3 kotak,
 *   target Imsak (bukan Subuh), chip Hijriah, % waktu puasa terlewati.
 * - Kalender menjadi mesin pencatatan: ketuk tanggal = catat puasa,
 *   ketuk lagi = batalkan (tombol "Catat" digeser fungsinya).
 * - Chip jadwal puasa sunnah bulan terpilih + "x Hari Lagi" (juga masuk notifikasi).
 * - Statistik Bulan Ini / Total / Tahun Ini (tanpa target tahunan).
 * - Niat & Doa → pop up lembar bawah (data satu sumber dengan Doa Harian).
 * - Banner Ramadhan otomatis saat bulan Hijriah 9.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PuasaScreen(
    viewModel: PrayerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val schedule = uiState.prayerSchedule

    val puasaPrefs = remember {
        viewModel.getApplication<Application>()
            .getSharedPreferences("puasa_prefs", android.content.Context.MODE_PRIVATE)
    }
    var refreshKey by remember { mutableIntStateOf(0) }
    val todayCal = Calendar.getInstance()
    val todayStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    val todayDateStr = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(Date())

    val isFastingToday = remember(todayStr, refreshKey) {
        puasaPrefs.getStringSet("fasted_dates", emptySet())?.contains(todayStr) == true
    }

    // ===== Catat/batal puasa pada SEMUA tanggal (fungsi "Catat" digeser ke kalender) =====
    fun toggleDate(dateStr: String) {
        val dates = puasaPrefs.getStringSet("fasted_dates", emptySet())?.toMutableSet()
            ?: mutableSetOf()
        if (dateStr in dates) dates.remove(dateStr) else dates.add(dateStr)
        puasaPrefs.edit().putStringSet("fasted_dates", dates).apply()
        refreshKey++
    }

    // Bulan yang sedang dilihat
    var viewYear by remember { mutableIntStateOf(todayCal.get(Calendar.YEAR)) }
    var viewMonth by remember { mutableIntStateOf(todayCal.get(Calendar.MONTH)) }

    // Tanggal puasa pada bulan yang dilihat
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

    // Statistik (bulan berjalan / tahun berjalan / total — tanpa target tahunan)
    val allDates = remember(refreshKey) {
        puasaPrefs.getStringSet("fasted_dates", emptySet()) ?: emptySet()
    }
    val curYear = todayCal.get(Calendar.YEAR)
    val curMonth0 = todayCal.get(Calendar.MONTH)
    val curMonthPrefix = String.format("%04d%02d", curYear, curMonth0 + 1)
    val countMonth = allDates.count { it.startsWith(curMonthPrefix) }
    val countYear = allDates.count { it.startsWith("$curYear") }
    val countTotal = allDates.size

    // Hijriah hari ini (chip hero + banner Ramadhan)
    val hijriToday = remember(todayStr) {
        HijriCalculator.toHijriInfo(
            todayCal.get(Calendar.YEAR),
            todayCal.get(Calendar.MONTH) + 1,
            todayCal.get(Calendar.DAY_OF_MONTH)
        )
    }
    val isRamadhanNow = hijriToday.month == 9

    // Label jenis puasa hari ini (otomatis dari tanggal)
    val todayTypeLabel = remember(todayStr) {
        SunnahDayCalculator.fastingTypeLabel(
            todayCal.get(Calendar.YEAR),
            todayCal.get(Calendar.MONTH),
            todayCal.get(Calendar.DAY_OF_MONTH)
        )
    }

    // Pop up Niat & Doa — satu sumber data dengan layar Doa Harian
    var sheetDoa by remember { mutableStateOf<DoaItem?>(null) }
    val niatDoa = remember { DoaData.getAllDoa().firstOrNull { it.id == "makan_4" } }
    val berbukaDoa = remember { DoaData.getAllDoa().firstOrNull { it.id == "makan_3" } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PuasaColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Judul halaman — tanpa lokasi (konsisten pola v2.4)
        Text(
            "Puasa",
            style = MaterialTheme.typography.headlineMedium,
            color = PuasaColors.Primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // === BANNER RAMADHAN (otomatis saat bulan Hijriah 9) ===
        if (isRamadhanNow) {
            RamadhanBanner(hijriDay = hijriToday.day, hijriYear = hijriToday.year)
            Spacer(modifier = Modifier.height(12.dp))
        }

        // === HERO (satuan cerdas, live) ===
        if (schedule != null) {
            val nowSec = uiState.currentTotalSeconds.takeIf { it > 0 } ?: run {
                Calendar.getInstance().let {
                    it.get(Calendar.HOUR_OF_DAY) * 3600 +
                        it.get(Calendar.MINUTE) * 60 + it.get(Calendar.SECOND)
                }
            }
            PuasaHero(schedule = schedule, nowSec = nowSec, hijri = hijriToday)
        } else {
            PuasaHeroFallback(
                errorMessage = uiState.errorMessage,
                onRetry = { viewModel.detectLocation() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === STATUS PUASA HARI INI (toggle cepat, sinkron dengan kalender) ===
        PuasaMarkCard(
            isFasted = isFastingToday,
            dateText = todayDateStr,
            typeLabel = todayTypeLabel,
            onTap = { toggleDate(todayStr) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // === KALENDER — ketuk tanggal untuk mencatat / membatalkan ===
        val todayDay =
            if (viewYear == todayCal.get(Calendar.YEAR) && viewMonth == todayCal.get(Calendar.MONTH))
                todayCal.get(Calendar.DAY_OF_MONTH) else -1
        PuasaCalendarCard(
            viewYear = viewYear,
            viewMonth = viewMonth,
            fastedDates = fastedDates,
            todayDay = todayDay,
            onPrevMonth = {
                if (viewMonth == 0) { viewMonth = 11; viewYear-- } else viewMonth--
            },
            onNextMonth = {
                if (viewMonth == 11) { viewMonth = 0; viewYear++ } else viewMonth++
            },
            onToggleDay = { day ->
                toggleDate(String.format("%04d%02d%02d", viewYear, viewMonth + 1, day))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // === JADWAL PUASA SUNNAH BULAN INI + x HARI LAGI ===
        SunnahScheduleCard(viewYear = viewYear, viewMonth = viewMonth)

        Spacer(modifier = Modifier.height(16.dp))

        // === STATISTIK (Bulan Ini / Total / Tahun Ini) ===
        PuasaStatsCard(
            monthCount = countMonth,
            totalCount = countTotal,
            yearCount = countYear
        )

        Spacer(modifier = Modifier.height(16.dp))

        // === NIAT & DOA (pop up) ===
        DoaQuickCard(
            hasNiat = niatDoa != null,
            hasBerbuka = berbukaDoa != null,
            onNiat = { niatDoa?.let { sheetDoa = it } },
            onBerbuka = { berbukaDoa?.let { sheetDoa = it } }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    // === POP UP NIAT / DOA BERBUKA (lembar bawah, pola Zikir v2.5) ===
    sheetDoa?.let { doa ->
        PuasaDoaSheet(doa = doa, onDismiss = { sheetDoa = null })
    }
}

// ==================== HERO ====================

@Composable
private fun PuasaHero(
    schedule: PrayerSchedule,
    nowSec: Int,
    hijri: HijriInfo
) {
    val imsakInfo = schedule.imsak
    val imsakSec = imsakInfo?.totalSeconds ?: schedule.fajr.totalSeconds
    val maghribSec = schedule.maghrib.totalSeconds

    val beforeImsak = nowSec < imsakSec
    val afterMaghrib = nowSec >= maghribSec

    val stateLabel = when {
        beforeImsak -> "MENUJU IMSAK"
        afterMaghrib -> "MENUJU IMSAK BESOK"
        else -> "MENUJU BERBUKA"
    }
    val subLabel = when {
        beforeImsak -> "Waktu sahur berakhir pada"
        afterMaghrib -> "Persiapan puasa esok hari"
        else -> "Buka Puasa Hari Ini"
    }
    val targetName = when {
        beforeImsak -> "Imsak"
        afterMaghrib -> "Imsak"
        else -> "Maghrib"
    }
    val targetSec = when {
        beforeImsak -> imsakSec
        afterMaghrib -> imsakSec + 86400 // besok (aproksimasi +24 jam)
        else -> maghribSec
    }
    val targetTimeStr = when {
        afterMaghrib -> imsakInfo?.timeString ?: schedule.fajr.timeString
        else -> if (beforeImsak) (imsakInfo?.timeString ?: schedule.fajr.timeString)
        else schedule.maghrib.timeString
    }

    val diff = (targetSec - nowSec).coerceAtLeast(0)
    val h = diff / 3600
    val m = (diff % 3600) / 60
    val s = diff % 60

    val pct = when {
        beforeImsak -> 0
        afterMaghrib -> 100
        maghribSec > imsakSec -> ((nowSec - imsakSec) * 100) / (maghribSec - imsakSec)
        else -> 0
    }.coerceIn(0, 100)

    val imsakInfoText = "Imsak ${imsakInfo?.timeString ?: schedule.fajr.timeString}"

    Card(
        colors = CardDefaults.cardColors(containerColor = PuasaColors.PrimaryDark),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Baris atas: label status + chip Hijriah
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stateLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = PuasaColors.GoldBright,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(PuasaColors.HeroChipBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        "${hijri.day} ${hijri.monthName} ${hijri.year} H",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Baris tengah: keterangan + jam target
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    subLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        targetName,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        targetTimeStr,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        " WIB",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Countdown LIVE 3 kotak (dipicu ticker 1 detik ViewModel)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeroCountdownBox(value = h, label = "JAM", modifier = Modifier.weight(1f))
                HeroCountdownBox(value = m, label = "MENIT", modifier = Modifier.weight(1f))
                HeroCountdownBox(value = s, label = "DETIK", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progres hari puasa + info Imsak
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(PuasaColors.HeroTrack)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(pct / 100f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(PuasaColors.GoldBright)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Waktu puasa terlewati $pct%",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.75f)
                )
                Text(
                    imsakInfoText,
                    style = MaterialTheme.typography.labelSmall,
                    color = PuasaColors.GoldBright,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun HeroCountdownBox(value: Int, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                String.format("%02d", value),
                style = MaterialTheme.typography.titleLarge,
                color = PuasaColors.PrimaryDark,
                fontWeight = FontWeight.Bold
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = PuasaColors.TextMuted
            )
        }
    }
}

/** Hero belum siap: spinner saat memuat, kartu error + Coba Lagi bila lokasi gagal. */
@Composable
private fun PuasaHeroFallback(errorMessage: String?, onRetry: () -> Unit) {
    if (errorMessage != null) {
        Card(
            colors = CardDefaults.cardColors(containerColor = PuasaColors.Surface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, PuasaColors.Divider)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.WifiOff,
                    contentDescription = null,
                    tint = PuasaColors.ErrorSoft,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Jadwal belum tersedia",
                    style = MaterialTheme.typography.titleSmall,
                    color = PuasaColors.TextDark,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = PuasaColors.TextMuted,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PuasaColors.Primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Coba Lagi", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PuasaColors.Gold)
        }
    }
}

// ==================== KARTU STATUS HARI INI ====================

@Composable
private fun PuasaMarkCard(
    isFasted: Boolean,
    dateText: String,
    typeLabel: String,
    onTap: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isFasted) PuasaColors.GreenSoft else PuasaColors.Surface
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            if (isFasted) PuasaColors.CheckGreen.copy(alpha = 0.4f) else PuasaColors.Divider
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTap)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isFasted) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(PuasaColors.CheckGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Icon(
                    Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = PuasaColors.TextMuted,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (isFasted) "Alhamdulillah · Puasa Hari Ini" else "Tandai puasa hari ini",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isFasted) PuasaColors.Primary else PuasaColors.TextDark,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    dateText,
                    style = MaterialTheme.typography.bodySmall,
                    color = PuasaColors.TextMuted
                )
            }
            if (isFasted) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .border(1.dp, PuasaColors.Gold.copy(alpha = 0.5f), RoundedCornerShape(50))
                        .background(PuasaColors.GoldSoft)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        typeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = PuasaColors.Gold,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ==================== KALENDER ====================

@Composable
private fun PuasaCalendarCard(
    viewYear: Int,
    viewMonth: Int,
    fastedDates: Set<Int>,
    todayDay: Int,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToggleDay: (Int) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PuasaColors.Surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PuasaColors.Divider)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Kalender Puasa",
                    style = MaterialTheme.typography.titleMedium,
                    color = PuasaColors.TextDark,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onPrevMonth) {
                        Icon(
                            Icons.Default.ChevronLeft, null,
                            tint = PuasaColors.Gold
                        )
                    }
                    Text(
                        "${MONTH_NAMES[viewMonth]} $viewYear",
                        style = MaterialTheme.typography.titleSmall,
                        color = PuasaColors.TextDark,
                        fontWeight = FontWeight.SemiBold
                    )
                    IconButton(onClick = onNextMonth) {
                        Icon(
                            Icons.Default.ChevronRight, null,
                            tint = PuasaColors.Gold
                        )
                    }
                }
            }

            Text(
                "Ketuk tanggal untuk mencatat / membatalkan puasa",
                style = MaterialTheme.typography.labelSmall,
                color = PuasaColors.TextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Header hari (Senin s/d Minggu)
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("S", "S", "R", "K", "J", "S", "M").forEach { day ->
                    Text(
                        day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = PuasaColors.TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            val calMonth = Calendar.getInstance().apply { set(viewYear, viewMonth, 1) }
            val maxDay = calMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            val firstDayOfWeek = (calMonth.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Senin = 0

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
                                val isSunnah = SunnahDayCalculator.isSunnahDay(viewYear, viewMonth, dayNum)
                                val isFriday = col == 4

                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isFasted -> PuasaColors.Primary
                                                else -> Color.Transparent
                                            }
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = when {
                                                isToday -> PuasaColors.Gold
                                                isSunnah && !isFasted -> PuasaColors.Gold.copy(alpha = 0.55f)
                                                else -> Color.Transparent
                                            },
                                            shape = CircleShape
                                        )
                                        .clickable { onToggleDay(dayNum) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "$dayNum",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = when {
                                            isFasted -> Color.White
                                            isToday -> PuasaColors.Gold
                                            isFriday -> PuasaColors.Gold
                                            isSunnah -> PuasaColors.Gold
                                            else -> PuasaColors.TextDark
                                        },
                                        fontWeight = if (isToday || isFasted) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
                rows++
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(PuasaColors.Primary)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Puasa", style = MaterialTheme.typography.labelSmall, color = PuasaColors.TextMuted)
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .border(1.dp, PuasaColors.Gold, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Hari ini", style = MaterialTheme.typography.labelSmall, color = PuasaColors.TextMuted)
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .border(1.dp, PuasaColors.Gold.copy(alpha = 0.55f), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Puasa sunnah", style = MaterialTheme.typography.labelSmall, color = PuasaColors.TextMuted)
            }
        }
    }
}

// ==================== JADWAL SUNNAH ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SunnahScheduleCard(viewYear: Int, viewMonth: Int) {
    val occurrences = remember(viewYear, viewMonth) {
        SunnahDayCalculator.occurrencesInMonth(viewYear, viewMonth)
    }
    val nextLabel = remember {
        SunnahDayCalculator.nextSunnahAfter(Calendar.getInstance())?.let { target ->
            when (val d = SunnahDayCalculator.daysFromToday(target)) {
                1 -> "Besok"
                else -> "$d Hari Lagi"
            }
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = PuasaColors.Surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PuasaColors.Divider)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Puasa Sunnah Bulan Ini",
                    style = MaterialTheme.typography.titleMedium,
                    color = PuasaColors.TextDark,
                    fontWeight = FontWeight.Bold
                )
                if (nextLabel != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(PuasaColors.GoldSoft)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            nextLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = PuasaColors.Gold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (occurrences.isEmpty()) {
                Text(
                    "Tidak ada hari anjuran puasa sunnah di bulan ini",
                    style = MaterialTheme.typography.bodySmall,
                    color = PuasaColors.TextMuted
                )
            } else {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    occurrences.forEach { occ ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    PuasaColors.Gold.copy(alpha = 0.45f),
                                    RoundedCornerShape(8.dp)
                                )
                                .background(PuasaColors.GoldSoft)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                SunnahDayCalculator.occurrenceLabel(viewYear, viewMonth, occ),
                                style = MaterialTheme.typography.labelMedium,
                                color = PuasaColors.Gold,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== STATISTIK ====================

@Composable
private fun PuasaStatsCard(monthCount: Int, totalCount: Int, yearCount: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PuasaColors.Surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PuasaColors.Divider)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Statistik",
                style = MaterialTheme.typography.titleMedium,
                color = PuasaColors.TextDark,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PuasaStatItem(
                    value = "$monthCount",
                    label = "Bulan Ini",
                    modifier = Modifier.weight(1f)
                )
                VerticalDivider(
                    modifier = Modifier.height(38.dp),
                    color = PuasaColors.Divider
                )
                PuasaStatItem(
                    value = "$totalCount",
                    label = "Total",
                    modifier = Modifier.weight(1f)
                )
                VerticalDivider(
                    modifier = Modifier.height(38.dp),
                    color = PuasaColors.Divider
                )
                PuasaStatItem(
                    value = "$yearCount",
                    label = "Tahun Ini",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PuasaStatItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.headlineSmall,
            color = PuasaColors.PrimaryDark,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = PuasaColors.TextMuted
        )
    }
}

// ==================== NIAT & DOA ====================

@Composable
private fun DoaQuickCard(
    hasNiat: Boolean,
    hasBerbuka: Boolean,
    onNiat: () -> Unit,
    onBerbuka: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PuasaColors.Surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PuasaColors.Divider)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Niat & Doa",
                style = MaterialTheme.typography.titleMedium,
                color = PuasaColors.TextDark,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DoaQuickChip(
                    text = "Niat Puasa",
                    icon = Icons.Default.MenuBook,
                    iconTint = PuasaColors.Primary,
                    enabled = hasNiat,
                    modifier = Modifier.weight(1f),
                    onClick = onNiat
                )
                DoaQuickChip(
                    text = "Doa Berbuka",
                    icon = Icons.Default.Brightness3,
                    iconTint = PuasaColors.Gold,
                    enabled = hasBerbuka,
                    modifier = Modifier.weight(1f),
                    onClick = onBerbuka
                )
            }
        }
    }
}

@Composable
private fun DoaQuickChip(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, PuasaColors.Divider, RoundedCornerShape(12.dp))
            .background(PuasaColors.Background)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium,
                color = PuasaColors.TextDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/** Pop up lembar bawah berisi teks penuh doa (Arab + latin + arti). */
@Composable
private fun PuasaDoaSheet(doa: DoaItem, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PuasaColors.Surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                doa.title,
                style = MaterialTheme.typography.titleLarge,
                color = PuasaColors.Primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                doa.arabic,
                style = MaterialTheme.typography.headlineSmall,
                color = PuasaColors.TextDark,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 44.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                "\"${doa.latin}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = PuasaColors.Primary,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = PuasaColors.Divider)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                doa.translation,
                style = MaterialTheme.typography.bodyMedium,
                color = PuasaColors.TextDark.copy(alpha = 0.85f),
                lineHeight = 22.sp
            )
        }
    }
}

// ==================== BANNER RAMADHAN ====================

@Composable
private fun RamadhanBanner(hijriDay: Int, hijriYear: Int) {
    val monthLen = HijriCalculator.daysInMonth(hijriYear, 9)
    val progress = if (monthLen > 0) hijriDay.toFloat() / monthLen else 0f
    val remaining = (monthLen - hijriDay).coerceAtLeast(0)
    val noteText = if (remaining > 0) {
        "Sisa $remaining hari menuju 1 Syawal — Idul Fitri"
    } else {
        "Hari terakhir Ramadhan — 1 Syawal tiba besok"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = PuasaColors.GoldSoft),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PuasaColors.Gold.copy(alpha = 0.45f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Brightness3,
                    contentDescription = null,
                    tint = PuasaColors.Gold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Ramadhan hari ke-$hijriDay dari $monthLen",
                    style = MaterialTheme.typography.titleSmall,
                    color = PuasaColors.TextDark,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(PuasaColors.GoldSoft)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(PuasaColors.Gold)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                noteText,
                style = MaterialTheme.typography.labelSmall,
                color = PuasaColors.TextMuted
            )
        }
    }
}
