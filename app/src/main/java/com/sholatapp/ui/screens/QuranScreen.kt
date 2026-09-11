package com.sholatapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.data.QuranRepository
import com.sholatapp.ui.theme.DarkColors

/**
 * Al-Qur'an lengkap (v2.3) — 114 surah / 6236 ayat, 100% offline.
 *
 * Dua tampilan dalam satu layar (dibuka dari halaman Zikir maupun
 * tindakan cepat Beranda):
 *   1. Browser : daftar 114 surah + pencarian + banner terakhir dibaca
 *   2. Reader  : tiap ayat tampil 3 unsur — Arab, Arab-Latin, Arti
 */
@Composable
fun QuranScreen(onBack: () -> Unit) {
    var selectedSurah by remember { mutableStateOf<QuranRepository.Surah?>(null) }

    BackHandler(enabled = selectedSurah != null) {
        selectedSurah = null
    }

    AnimatedContent(
        targetState = selectedSurah,
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
                onOpenSurah = { selectedSurah = it },
                onBack = onBack
            )
        } else {
            QuranReader(
                surahMeta = surah,
                onBack = { selectedSurah = null }
            )
        }
    }
}

// ==================== BROWSER: DAFTAR 114 SURAH ====================
@Composable
private fun QuranBrowser(
    onOpenSurah: (QuranRepository.Surah) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val allSurahs = remember { QuranRepository.getSurahIndex(context) }
    var query by remember { mutableStateOf("") }

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
            .background(DarkColors.Background)
    ) {
        // Header
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
                        text = "Al-Qur'an",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkColors.TextOnPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "114 Surah · 6236 Ayat · Offline",
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Banner terakhir dibaca
            if (lastRead != null) {
                item(key = "lastRead") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenSurah(lastRead) },
                        colors = CardDefaults.cardColors(
                            containerColor = DarkColors.PrimaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = DarkColors.Primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Terakhir Dibaca",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DarkColors.TextSecondary
                                )
                                Text(
                                    text = "${lastRead.latinName} · Ayat ${lastRead.ayahCount}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = DarkColors.Primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = DarkColors.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Pencarian surah
            item(key = "search") {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Cari surah ...", style = MaterialTheme.typography.bodyMedium)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = DarkColors.TextTertiary)
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Hapus", tint = DarkColors.TextTertiary)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkColors.Primary,
                        unfocusedBorderColor = DarkColors.Border,
                        focusedContainerColor = DarkColors.Surface,
                        unfocusedContainerColor = DarkColors.Surface
                    )
                )
            }

            // Daftar 114 surah
            items(filtered, key = { it.number }) { surah ->
                SurahRow(surah = surah, onClick = { onOpenSurah(surah) })
            }

            if (filtered.isEmpty()) {
                item(key = "empty") {
                    Text(
                        text = "Surah tidak ditemukan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkColors.TextTertiary,
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

@Composable
private fun SurahRow(surah: QuranRepository.Surah, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nomor surah dalam kotak belah ketupat lembut
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkColors.PrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = surah.number.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = DarkColors.Primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.latinName,
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${surah.revelation} · ${surah.ayahCount} ayat · ${surah.meaning}",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkColors.TextTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = surah.arabicName,
                style = MaterialTheme.typography.titleLarge,
                color = DarkColors.Primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ==================== READER: 3 UNSUR PER AYAT ====================
@Composable
private fun QuranReader(
    surahMeta: QuranRepository.Surah,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val surah = remember(surahMeta.number) {
        QuranRepository.getSurah(context, surahMeta.number)
    }

    // Simpan posisi baca terakhir (level surah) agar banner "Terakhir Dibaca" aktif
    LaunchedEffect(surahMeta.number) {
        context.getSharedPreferences("mushaf_prefs", android.content.Context.MODE_PRIVATE)
            .edit()
            .putInt("last_surah_number", surahMeta.number)
            .apply()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColors.Background)
    ) {
        // Header
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
                        text = surahMeta.latinName,
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkColors.TextOnPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${surahMeta.revelation} · ${surahMeta.ayahCount} ayat · ${surahMeta.meaning}",
                        style = MaterialTheme.typography.labelMedium,
                        color = DarkColors.HeaderSubtitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = surahMeta.arabicName,
                    style = MaterialTheme.typography.titleLarge,
                    color = DarkColors.GoldLight,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (surah == null) {
            // Data tidak termuat (jarang terjadi — aset lengkap dibundel)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Teks surah tidak dapat dimuat",
                    color = DarkColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Basmalah dekoratif (kecuali Al-Fatihah & At-Taubah)
                if (surah.number != 1 && surah.number != 9) {
                    item(key = "bismillah") {
                        Text(
                            text = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ",
                            style = MaterialTheme.typography.titleLarge,
                            color = DarkColors.Primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )
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
                        color = DarkColors.TextTertiary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}

/**
 * Kartu ayat dengan 3 unsur (v2.3):
 *  A. Tulisan Arab   — besar, rata kanan
 *  B. Arab-Latin     — transliterasi untuk yang belum lancar membaca Arab
 *  C. Arti           — terjemahan Kemenag RI
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
        colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Nomor ayat + garis
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(DarkColors.PrimaryContainer)
                        .border(1.dp, DarkColors.Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ayahNumber.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkColors.Primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = DarkColors.Divider
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // A. Tulisan Arab
            Text(
                text = arabic,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 26.sp,
                lineHeight = 46.sp,
                color = DarkColors.TextPrimary,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // B. Arab-Latin
            Text(
                text = latin,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = DarkColors.TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // C. Arti (Kemenag RI)
            Text(
                text = translation,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkColors.TextPrimary,
                lineHeight = 21.sp
            )
        }
    }
}
