package com.sholatapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.ceil

/**
 * Palet terang halaman Mushaf (v2.10) — bahasa visual baru v2.4–v2.9.
 */
private object MushafColors {
    val Background = Color(0xFFF5F5F0)
    val Surface = Color(0xFFFFFFFF)
    val PrimaryDark = Color(0xFF143A2E)
    val Primary = Color(0xFF1B4D3E)
    val HeaderSubtitle = Color(0xFFA8C3B4)
    val Gold = Color(0xFFA97C0E)
    val IconContainer = Color(0xFFE8F0EA)
    val TextPrimary = Color(0xFF1F2E28)
    val TextSecondary = Color(0xFF5B665B)
    val TextTertiary = Color(0xFF8A938A)
    val Divider = Color(0xFFE4E8E0)
}

/**
 * Halaman Mushaf — Bacaan Terarah (v2.10, rombak visual dari v2.6).
 *
 * Halaman khusus membaca ayat yang DITENTUKAN SISTEM berdasarkan target
 * khatam (diatur di halaman Pengaturan → Bacaan Terarah). Sesuai arahan
 * user (diperkuat referensi v2.10):
 *  - Teks Arab MENGGALIR menerus satu blok paragraf gaya mushaf asli
 *    (HANYA di halaman ini; halaman Al-Qur'an tetap kartu per-ayat);
 *    nomor ayat ditandai ornamen emas medali di dalam aliran teks.
 *  - Basmalah hanya tampil bila bacaan dimulai dari ayat 1 sebuah surah,
 *    dan TIDAK untuk At-Taubah (surah satu-satunya tanpa basmalah).
 *    Al-Fatihah juga tanpa basmalah dekoratif karena basmalah = ayat 1-nya.
 *  - Tidak ada juz/halaman mushaf (data tidak tersedia) — footer memakai
 *    atribusi sumber yang benar: "Teks & Terjemahan Kemenag RI".
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
            .background(MushafColors.Background)
    ) {
        // ==================== HEADER (rounded, bahasa visual v2.10) ========
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(MushafColors.PrimaryDark)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Mushaf",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Bacaan Terarah · Ditentukan Sistem",
                        style = MaterialTheme.typography.labelMedium,
                        color = MushafColors.HeaderSubtitle
                    )
                }
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
                    colors = CardDefaults.cardColors(containerColor = MushafColors.Surface),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MushafColors.Primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Alhamdulillah, Khatam Tercapai",
                            style = MaterialTheme.typography.titleLarge,
                            color = MushafColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Mulai ulang rencana dari halaman Pengaturan untuk khatam berikutnya.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MushafColors.TextTertiary,
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

                // ---------------- HERO: BACAAN HARI INI (putih + emas) -------
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MushafColors.Gold, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = MushafColors.Surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "BACAAN HARI INI",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MushafColors.Gold,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = portionTitle(portion),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MushafColors.TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (portion.size == 1) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(
                                            color = MushafColors.IconContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "Surah ${portion.first().surahNumber}",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MushafColors.Primary
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = portionSubtitle(portion, totalAyat),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MushafColors.TextSecondary
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
                            color = MushafColors.IconContainer,
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
                                    color = MushafColors.Primary
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
                                containerColor = MushafColors.Gold,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp)
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

                // ---------------- LEMBAR MUSHAF (ARAB MENERUS, SATU BLOK) ----
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MushafColors.Surface),
                    shape = RoundedCornerShape(24.dp),
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
                            CircularProgressIndicator(color = MushafColors.Gold)
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
                                color = MushafColors.TextTertiary
                            )
                        }
                        else -> Column(modifier = Modifier.padding(18.dp)) {
                            segs.forEachIndexed { index, seg ->
                                if (index > 0) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = MushafColors.Divider)
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
                                    color = MushafColors.TextPrimary,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            // Atribusi sumber — data juz/halaman mushaf tidak
                            // tersedia, jadi footer memakai atribusi yang benar.
                            Text(
                                text = "Teks & Terjemahan Kemenag RI · 100% Offline",
                                style = MaterialTheme.typography.labelSmall,
                                color = MushafColors.TextTertiary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // ==================== TOMBOL BAWAH TETAP ====================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MushafColors.Background)
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
                        containerColor = if (todayRead) MushafColors.IconContainer else MushafColors.PrimaryDark,
                        contentColor = if (todayRead) MushafColors.Primary else Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
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

/** Judul porsi: nama surah (contoh mockup: "Al-Ikhlas"). */
private fun portionTitle(portion: List<TilawahAyahRef>): String {
    val first = portion.first()
    val last = portion.last()
    return if (portion.size == 1) first.surahName
    else "${first.surahName} – ${last.surahName}"
}

/** Subjudul: rentang ayat + jumlah ayat + estimasi menit baca. */
private fun portionSubtitle(portion: List<TilawahAyahRef>, totalAyat: Int): String {
    val first = portion.first()
    val last = portion.last()
    val range = if (first.ayahStart == last.ayahEnd && portion.size == 1)
        "Ayat ${first.ayahStart}"
    else if (portion.size == 1)
        "Ayat ${first.ayahStart} – ${first.ayahEnd}"
    else
        "Ayat ${first.ayahStart} – ${last.ayahEnd}"
    return "$range · $totalAyat ayat · ±${minutesEstimate(totalAyat)} menit baca"
}

private fun minutesEstimate(totalAyat: Int): Int =
    ceil(totalAyat * 20.0 / 60.0).toInt().coerceAtLeast(1)

/** Angka Arab-Indic untuk penanda ayat (mushaf style). */
private fun toArabicIndic(n: Int): String =
    n.toString().map { ch -> (0x0660 + (ch - '0')).toChar() }.joinToString("")

/** Aliran ayat menerus dengan penanda ornamen emas ﴿n﴾ (satu blok). */
private fun ayahFlow(seg: MushafSegment): AnnotatedString = buildAnnotatedString {
    seg.ayahTexts.forEachIndexed { index, (number, text) ->
        if (index > 0) append(" ")
        append(text)
        append(" ")
        withStyle(
            SpanStyle(
                color = MushafColors.Gold,
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
            color = MushafColors.Gold,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        if (showBasmalah) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                fontSize = 20.sp,
                color = MushafColors.TextPrimary,
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
                color = MushafColors.IconContainer,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            if (fraction > 0f) {
                drawArc(
                    color = MushafColors.Gold,
                    startAngle = -90f,
                    sweepAngle = 360f * fraction,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${percent.toInt()}%",
                fontSize = 13.sp,
                color = MushafColors.PrimaryDark,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Khatam",
                fontSize = 9.sp,
                color = MushafColors.TextTertiary
            )
        }
    }
}
