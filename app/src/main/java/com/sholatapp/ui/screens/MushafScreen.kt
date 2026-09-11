package com.sholatapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.data.QuranRepository
import com.sholatapp.data.TilawahAyahRef
import com.sholatapp.data.TilawahData
import com.sholatapp.ui.theme.DarkColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.ceil

/**
 * Halaman Mushaf — Bacaan Terarah (v2.6).
 *
 * Halaman khusus membaca ayat yang DITENTUKAN SISTEM berdasarkan target
 * khatam (diatur di halaman Pengaturan → Bacaan Terarah). Sesuai arahan user:
 *  - Hanya teks Arab, mengalir menerus tanpa kartu terpisah (gaya mushaf);
 *    nomor ayat ditandai ornamen emas ﴿n﴾ di dalam aliran teks.
 *  - Basmalah hanya tampil bila bacaan dimulai dari ayat 1 sebuah surah,
 *    dan TIDAK untuk At-Taubah (surah satu-satunya tanpa basmalah).
 *    Al-Fatihah juga tanpa basmalah dekoratif karena basmalah = ayat 1-nya.
 *  - Tanpa tautan ke halaman Al-Qur'an surat lengkap.
 *  - Satu database dengan pembaca Al-Qur'an (assets/quran via QuranRepository).
 */
@Composable
fun MushafScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Porsi hari ini dibekukan saat halaman dibuka agar tidak melompat
    // setelah "Tandai Selesai" menaikkan posisi rencana.
    val portion = remember { TilawahData.getTodayPortion(context) }
    var progressInfo by remember { mutableStateOf(TilawahData.getProgress(context)) }
    var todayRead by remember { mutableStateOf(TilawahData.isTodayRead(context)) }
    var segments by remember { mutableStateOf<List<MushafSegment>?>(null) }

    LaunchedEffect(portion) {
        segments = withContext(Dispatchers.IO) { loadSegments(context, portion) }
    }

    val totalAyat = portion.sumOf { it.ayahEnd - it.ayahStart + 1 }
    val isKhatam = portion.isEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColors.Background)
    ) {
        // ==================== HEADER ====================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkColors.PrimaryDark)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = DarkColors.TextOnPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mushaf",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkColors.Gold,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isKhatam) "Alhamdulillah — target tercapai"
                        else "Target Khatam ${progressInfo.targetDays} Hari · sisa ${(progressInfo.targetDays - progressInfo.totalReadDays).coerceAtLeast(0)} hari",
                        style = MaterialTheme.typography.labelMedium,
                        color = DarkColors.HeaderSubtitle
                    )
                }
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = DarkColors.GoldLight,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        if (isKhatam) {
            // ==================== KHATAM TERCAPAI ====================
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = DarkColors.Primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Alhamdulillah, Khatam Tercapai",
                            style = MaterialTheme.typography.titleLarge,
                            color = DarkColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Mulai ulang rencana dari halaman Pengaturan untuk khatam berikutnya.",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkColors.TextTertiary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            // ==================== KONTEN SCROLL ====================
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // ---------------- HERO: BACAAN HARI INI ----------------
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkColors.PrimaryDark),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "BACAAN HARI INI",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DarkColors.GoldLight,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = rangeTitle(portion),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = DarkColors.TextOnPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$totalAyat ayat · ±${minutesEstimate(totalAyat)} menit baca",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = DarkColors.HeaderSubtitle
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            ProgressRing(
                                percent = progressInfo.completionPercent,
                                size = 64.dp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "\uD83D\uDD25", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${progressInfo.currentStreak} hari beruntun",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DarkColors.HeaderSubtitle
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                scope.launch { scrollState.animateScrollTo(scrollState.maxValue) }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkColors.Gold,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Mulai Membaca",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ---------------- LEMBAR MUSHAF (ARAB MENERUS) ----------------
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    val segs = segments
                    when {
                        segs == null -> Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = DarkColors.Gold)
                        }
                        segs.isEmpty() -> Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Teks bacaan belum tersedia",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkColors.TextTertiary
                            )
                        }
                        else -> Column(modifier = Modifier.padding(18.dp)) {
                            segs.forEachIndexed { index, seg ->
                                if (index > 0) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = DarkColors.Border)
                                    Spacer(modifier = Modifier.height(14.dp))
                                }
                                SurahStartHeader(
                                    segment = seg,
                                    showBasmalah = seg.ayahStart == 1 &&
                                            seg.surahNumber != 9 &&  // At-Taubah: tanpa basmalah
                                            seg.surahNumber != 1     // Al-Fatihah: basmalah = ayat 1
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = ayahFlow(seg),
                                    style = TextStyle(
                                        fontSize = 26.sp,
                                        lineHeight = 46.sp,
                                        textAlign = TextAlign.Right,
                                        textDirection = TextDirection.Rtl
                                    ),
                                    color = DarkColors.TextPrimary,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // ==================== TOMBOL BAWAH TETAP ====================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkColors.Background)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        if (!todayRead) {
                            TilawahData.markTodayAsRead(context)
                            todayRead = true
                            progressInfo = TilawahData.getProgress(context)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (todayRead) DarkColors.Primary else DarkColors.Gold,
                        contentColor = if (todayRead) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (todayRead) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Selesai · Sampai Besok",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "Tandai Bacaan Selesai",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ==================== MODEL SEGMENT + PEMUATAN ====================

/** Satu segmen = porsi ayat dari satu surah di bacaan hari ini. */
private data class MushafSegment(
    val surahNumber: Int,
    val surahName: String,
    val surahNameArabic: String,
    val ayahStart: Int,
    val ayahEnd: Int,
    val ayahTexts: List<Pair<Int, String>> // (nomor ayat, teks Arab)
)

/** Ambil teks Arab tiap ayat dari database bersama (assets/quran). */
private fun loadSegments(
    context: android.content.Context,
    portion: List<TilawahAyahRef>
): List<MushafSegment> {
    return portion.mapNotNull { ref ->
        val surah = QuranRepository.getSurah(context, ref.surahNumber) ?: return@mapNotNull null
        val texts = (ref.ayahStart..ref.ayahEnd).mapNotNull { n ->
            surah.ayahs.getOrNull(n - 1)?.let { n to it.arabic }
        }
        if (texts.isEmpty()) null
        else MushafSegment(
            surahNumber = ref.surahNumber,
            surahName = ref.surahName,
            surahNameArabic = ref.surahNameArabic,
            ayahStart = ref.ayahStart,
            ayahEnd = ref.ayahEnd,
            ayahTexts = texts
        )
    }
}

// ==================== HELPER TAMPILAN ====================

private fun rangeTitle(portion: List<TilawahAyahRef>): String {
    val first = portion.first()
    val last = portion.last()
    return if (portion.size == 1)
        "QS. ${first.surahName} · Ayat ${first.ayahStart} – ${first.ayahEnd}"
    else
        "QS. ${first.surahName} ${first.ayahStart} – ${last.surahName} ${last.ayahEnd}"
}

private fun minutesEstimate(totalAyat: Int): Int =
    ceil(totalAyat * 20.0 / 60.0).toInt().coerceAtLeast(1)

/** Angka Arab-Indic untuk penanda ayat (mushaf style). */
private fun toArabicIndic(n: Int): String =
    n.toString().map { ch -> (0x0660 + (ch - '0')).toChar() }.joinToString("")

/** Aliran ayat menerus dengan penanda ornamen emas ﴿n﴾. */
private fun ayahFlow(seg: MushafSegment): AnnotatedString = buildAnnotatedString {
    seg.ayahTexts.forEachIndexed { index, (number, text) ->
        if (index > 0) append(" ")
        append(text)
        append(" ")
        withStyle(
            SpanStyle(
                color = DarkColors.Gold,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        ) {
            append("\uFD3F${toArabicIndic(number)}\uFD3E")
        }
    }
}

@Composable
private fun SurahStartHeader(segment: MushafSegment, showBasmalah: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "QS. ${segment.surahName} · ${segment.surahNameArabic}",
            style = MaterialTheme.typography.labelMedium,
            color = DarkColors.Gold,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        if (showBasmalah) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                fontSize = 20.sp,
                color = DarkColors.TextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProgressRing(percent: Float, size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 6.dp.toPx()
            val fraction = (percent / 100f).coerceIn(0f, 1f)
            drawArc(
                color = Color.White.copy(alpha = 0.15f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            if (fraction > 0f) {
                drawArc(
                    color = DarkColors.GoldLight,
                    startAngle = -90f,
                    sweepAngle = 360f * fraction,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
        }
        Text(
            text = "${percent.toInt()}%",
            fontSize = 13.sp,
            color = DarkColors.TextOnPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}
