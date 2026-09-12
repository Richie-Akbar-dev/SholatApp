package com.sholatapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.data.QuranRepository
import com.sholatapp.data.TilawahData

/**
 * Palet terang halaman Al-Qur'an (v2.10) — bahasa visual baru v2.4–v2.9:
 * latar hangat, header hijau tua rounded, kartu putih, aksen emas.
 */
private object QuranColors {
    val Background = Color(0xFFF5F5F0)
    val Surface = Color(0xFFFFFFFF)
    val PrimaryDark = Color(0xFF143A2E)
    val HeaderSubtitle = Color(0xFFA8C3B4)
    val Gold = Color(0xFFA97C0E)
    val IconContainer = Color(0xFFE8F0EA)
    val TextPrimary = Color(0xFF1F2E28)
    val TextSecondary = Color(0xFF5B665B)
    val TextTertiary = Color(0xFF8A938A)
    val Divider = Color(0xFFE4E8E0)
}

/**
 * Halaman Al-Qur'an — v2.10 (rombak visual penuh sesuai mockup user).
 *
 * 114 surah / 6236 ayat, 100% offline (aset terbundel). Dua tampilan:
 *   1. Browser — header hijau rounded, pil pencarian, kartu "Lanjutkan
 *      Membaca" berbingkai emas, banner Bacaan Hari Ini (pintu ke Mushaf),
 *      daftar surah dengan ornamen bintang-8 emas + nama Arab di kanan.
 *   2. Reader — kartu bismillah berarti, kartu per-ayat 3 unsur
 *      (Arab, Arab-Latin, Arti) dengan pil "Ayat N" cincin emas,
 *      bar bawah pindah surah Sebelumnya/Berikutnya.
 *
 * Perubahan v2.10 vs v2.3:
 *  - Palet terang baru (sebelumnya gelap) + ornamen bintang-8.
 *  - Banner terakhir dibaca -> kartu "Lanjutkan Membaca" beraksen emas.
 *  - Bismillah menjadi kartu tersendiri beserta artinya (keputusan user),
 *    kecuali Al-Fatihah (bismillah = ayat 1) dan At-Taubah (tanpa basmalah).
 *  - Tanpa ikon audio/bookmark per ayat (tidak diimplementasi di v2.10).
 */
@Composable
fun QuranScreen(onBack: () -> Unit, onMushafClick: () -> Unit = {}) {
    val context = LocalContext.current
    val allSurahs = remember { QuranRepository.getSurahIndex(context) }
    var selectedNumber by remember { mutableStateOf<Int?>(null) }
    val selectedMeta = selectedNumber?.let { n -> allSurahs.firstOrNull { it.number == n } }

    BackHandler(enabled = selectedMeta != null) {
        selectedNumber = null
    }

    AnimatedContent(
        targetState = selectedMeta,
        transitionSpec = {
            (slideInHorizontally(animationSpec = tween(260)) { it } +
                    fadeIn(animationSpec = tween(200))) togetherWith
                    (slideOutHorizontally(animationSpec = tween(220)) { it } +
                            fadeOut(animationSpec = tween(160)))
        },
        label = "quranContent"
    ) { surah ->
        if (surah == null) {
            QuranBrowser(
                allSurahs = allSurahs,
                onOpenSurah = { selectedNumber = it.number },
                onBack = onBack,
                onMushafClick = onMushafClick
            )
        } else {
            val currentIndex = allSurahs.indexOfFirst { it.number == surah.number }
            QuranReader(
                surahMeta = surah,
                onBack = { selectedNumber = null },
                onPrev = {
                    allSurahs.getOrNull(currentIndex - 1)?.let { selectedNumber = it.number }
                },
                onNext = {
                    allSurahs.getOrNull(currentIndex + 1)?.let { selectedNumber = it.number }
                }
            )
        }
    }
}

// ==================== BROWSER: DAFTAR 114 SURAH ====================
@Composable
private fun QuranBrowser(
    allSurahs: List<QuranRepository.Surah>,
    onOpenSurah: (QuranRepository.Surah) -> Unit,
    onBack: () -> Unit,
    onMushafClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }

    // Bacaan Terarah (v2.6) — banner menuju halaman Mushaf (pintu B)
    val portion = remember { TilawahData.getTodayPortion(context) }

    val mushafPrefs = remember {
        context.getSharedPreferences("mushaf_prefs", android.content.Context.MODE_PRIVATE)
    }
    val lastReadNumber = remember { mushafPrefs.getInt("last_surah_number", -1) }
    val lastRead = remember(lastReadNumber) {
        if (lastReadNumber > 0) allSurahs.firstOrNull { it.number == lastReadNumber } else null
    }

    val filtered = remember(query, allSurahs) {
        if (query.isBlank()) allSurahs
        else allSurahs.filter {
            it.latinName.contains(query.trim(), ignoreCase = true) ||
                    it.meaning.contains(query.trim(), ignoreCase = true) ||
                    it.number.toString() == query.trim()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuranColors.Background)
    ) {
        // Header hijau rounded (bahasa visual v2.10)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(QuranColors.PrimaryDark)
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
                        text = "Al-Qur'an",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "114 Surah · 6.236 Ayat · Offline",
                        style = MaterialTheme.typography.labelMedium,
                        color = QuranColors.HeaderSubtitle
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Pil pencarian
            item(key = "search") {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Cari surah...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = QuranColors.TextTertiary
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = QuranColors.TextTertiary)
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Hapus", tint = QuranColors.TextTertiary)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = QuranColors.Gold,
                        unfocusedBorderColor = QuranColors.Divider,
                        focusedContainerColor = QuranColors.Surface,
                        unfocusedContainerColor = QuranColors.Surface,
                        cursorColor = QuranColors.Gold
                    )
                )
            }

            // Kartu "Lanjutkan Membaca" — bingkai emas (mockup A1)
            if (lastRead != null) {
                item(key = "lastRead") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, QuranColors.Gold, RoundedCornerShape(20.dp))
                            .clickable { onOpenSurah(lastRead) },
                        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(QuranColors.IconContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = QuranColors.Gold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Lanjutkan Membaca",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = QuranColors.PrimaryDark,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${lastRead.latinName} · ${lastRead.ayahCount} ayat",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = QuranColors.TextSecondary
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = QuranColors.Gold
                            ) {
                                Text(
                                    text = "Lanjut",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Banner Bacaan Hari Ini — pintu satu arah ke halaman Mushaf (B)
            if (portion.isNotEmpty()) {
                item(key = "mushafBanner") {
                    val first = portion.first()
                    val last = portion.last()
                    val totalAyat = portion.sumOf { it.ayahEnd - it.ayahStart + 1 }
                    val rangeText = if (portion.size == 1)
                        "QS. ${first.surahName} · Ayat ${first.ayahStart} – ${first.ayahEnd}"
                    else
                        "QS. ${first.surahName} ${first.ayahStart} – ${last.surahName} ${last.ayahEnd}"
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onMushafClick),
                        colors = CardDefaults.cardColors(containerColor = QuranColors.PrimaryDark),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "BACAAN HARI INI",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = QuranColors.HeaderSubtitle,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = rangeText,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "$totalAyat ayat · buka halaman Mushaf",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = QuranColors.HeaderSubtitle
                                )
                            }
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = "Buka Mushaf",
                                tint = QuranColors.Gold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Label seksi
            item(key = "label") {
                Text(
                    text = "Daftar Surah",
                    style = MaterialTheme.typography.titleMedium,
                    color = QuranColors.PrimaryDark,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Daftar 114 surah — ornamen bintang-8 emas + nama Arab
            items(filtered, key = { it.number }) { surah ->
                SurahRow(surah = surah, onClick = { onOpenSurah(surah) })
            }

            if (filtered.isEmpty()) {
                item(key = "empty") {
                    Text(
                        text = "Surah tidak ditemukan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuranColors.TextTertiary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp)
                    )
                }
            }
        }
    }
}

/** Ornamen bintang-8 (dua persegi disilang 45°) dengan nomor surah di tengah. */
@Composable
private fun OctagramBadge(number: Int) {
    Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val side = size.minDimension * 0.66f
            val stroke = 1.4.dp.toPx()
            val topLeft = Offset(center.x - side / 2f, center.y - side / 2f)
            val rectSize = Size(side, side)
            val corner = CornerRadius(3.dp.toPx())
            listOf(0f, 45f).forEach { angle ->
                withTransform({ rotate(angle, pivot = center) }) {
                    drawRoundRect(
                        color = QuranColors.Gold,
                        topLeft = topLeft,
                        size = rectSize,
                        cornerRadius = corner,
                        style = Stroke(width = stroke)
                    )
                }
            }
        }
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = QuranColors.PrimaryDark,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SurahRow(surah: QuranRepository.Surah, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OctagramBadge(number = surah.number)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.latinName,
                    style = MaterialTheme.typography.titleMedium,
                    color = QuranColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${surah.revelation} · ${surah.ayahCount} ayat",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuranColors.TextTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = surah.arabicName,
                style = MaterialTheme.typography.titleLarge,
                color = QuranColors.PrimaryDark,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ==================== READER: 3 UNSUR PER AYAT ====================
@Composable
private fun QuranReader(
    surahMeta: QuranRepository.Surah,
    onBack: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val surah = remember(surahMeta.number) {
        QuranRepository.getSurah(context, surahMeta.number)
    }

    // Simpan posisi baca terakhir (level surah) agar kartu "Lanjutkan Membaca" aktif
    LaunchedEffect(surahMeta.number) {
        context.getSharedPreferences("mushaf_prefs", android.content.Context.MODE_PRIVATE)
            .edit()
            .putInt("last_surah_number", surahMeta.number)
            .apply()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuranColors.Background)
    ) {
        // Header hijau rounded
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(QuranColors.PrimaryDark)
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = surahMeta.latinName,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${surahMeta.revelation} · ${surahMeta.ayahCount} ayat",
                        style = MaterialTheme.typography.labelMedium,
                        color = QuranColors.HeaderSubtitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = surahMeta.arabicName,
                    style = MaterialTheme.typography.titleLarge,
                    color = QuranColors.Gold,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (surah == null) {
            // Data tidak termuat (jarang terjadi — aset lengkap dibundel)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Teks surah tidak dapat dimuat",
                    color = QuranColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Kartu bismillah + artinya (keputusan user v2.10 — catatan n3).
                // Al-Fatihah: bismillah = ayat 1; At-Taubah: satu-satunya surah
                // tanpa basmalah.
                if (surah.number != 1 && surah.number != 9) {
                    item(key = "bismillah") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp, horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ",
                                    fontSize = 22.sp,
                                    lineHeight = 38.sp,
                                    color = QuranColors.PrimaryDark,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontStyle = FontStyle.Italic,
                                    color = QuranColors.TextTertiary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                items(surah.ayahs, key = { it.number }) { ayah ->
                    AyahCard(ayahNumber = ayah.number, arabic = ayah.arabic, latin = ayah.latin, translation = ayah.translation)
                }

                // Penutup surah
                item(key = "end") {
                    Text(
                        text = "· ${surah.latinName} selesai · ${surah.ayahCount} ayat ·",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuranColors.TextTertiary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    )
                }
            }

            // Bar bawah: pindah surah sebelumnya / berikutnya (mockup A2)
            Surface(color = QuranColors.Surface, shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val prevEnabled = surahMeta.number > 1
                    val nextEnabled = surahMeta.number < 114
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (prevEnabled) QuranColors.IconContainer else QuranColors.Divider.copy(alpha = 0.4f),
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)).clickable(enabled = prevEnabled, onClick = onPrev)
                    ) {
                        Text(
                            text = "‹ Sebelumnya",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (prevEnabled) QuranColors.PrimaryDark else QuranColors.TextTertiary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (nextEnabled) QuranColors.PrimaryDark else QuranColors.Divider.copy(alpha = 0.4f),
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)).clickable(enabled = nextEnabled, onClick = onNext)
                    ) {
                        Text(
                            text = "Berikutnya ›",
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (nextEnabled) Color.White else QuranColors.TextTertiary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Kartu ayat dengan 3 unsur (dipertahankan dari v2.3, tampilan baru v2.10):
 *  A. Tulisan Arab   — besar, rata kanan
 *  B. Arab-Latin     — transliterasi untuk yang belum lancar membaca Arab
 *  C. Arti           — terjemahan Kemenag RI
 * Tanpa ikon audio/bookmark (keputusan user v2.10 — catatan n2).
 */
@Composable
private fun AyahCard(
    ayahNumber: Int,
    arabic: String,
    latin: String,
    translation: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = QuranColors.Surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Pil "Ayat N" cincin emas + garis
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.Transparent,
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuranColors.Gold)
                ) {
                    Text(
                        text = "Ayat $ayahNumber",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = QuranColors.PrimaryDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = QuranColors.Divider
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // A. Tulisan Arab
            Text(
                text = arabic,
                fontSize = 26.sp,
                lineHeight = 46.sp,
                color = QuranColors.TextPrimary,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // B. Arab-Latin
            Text(
                text = latin,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = QuranColors.TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // C. Arti (Kemenag RI)
            Text(
                text = translation,
                style = MaterialTheme.typography.bodyMedium,
                color = QuranColors.TextPrimary,
                lineHeight = 21.sp
            )
        }
    }
}
