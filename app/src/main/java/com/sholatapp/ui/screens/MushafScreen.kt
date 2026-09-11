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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.sholatapp.data.QuranMetadata
import com.sholatapp.data.QuranTextData
import com.sholatapp.model.SurahInfo
import com.sholatapp.ui.theme.DarkColors

@Composable
fun MushafScreen() {
    var selectedSurah by remember { mutableStateOf<SurahInfo?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val mushafPrefs = remember {
        context.getSharedPreferences("mushaf_prefs", android.content.Context.MODE_PRIVATE)
    }
    val dailyJuz = remember { QuranMetadata.getDailyJuz() }
    val todaySurahs = remember { QuranMetadata.getSurahsInJuz(dailyJuz) }
    val lastReadNumber = remember { mushafPrefs.getInt("last_surah_number", -1) }
    val lastReadSurah = remember(lastReadNumber) {
        if (lastReadNumber > 0) QuranMetadata.getAllSurahs().firstOrNull { it.number == lastReadNumber } else null
    }

    // Tombol back sistem: kembali ke daftar surah dulu
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
        label = "mushafContent"
    ) { surah ->
        if (surah != null) {
            // Simpan posisi baca terakhir saat surah dibuka
            LaunchedEffect(surah.number) {
                mushafPrefs.edit().putInt("last_surah_number", surah.number).apply()
            }
            SurahReaderScreen(
                surah = surah,
                onBack = { selectedSurah = null }
            )
        } else {
            MushafBrowser(
                dailyJuz = dailyJuz,
                todaySurahs = todaySurahs,
                lastReadSurah = lastReadSurah,
                onSelectSurah = { selectedSurah = it }
            )
        }
    }
}

@Composable
private fun MushafBrowser(
    dailyJuz: Int,
    todaySurahs: List<SurahInfo>,
    lastReadSurah: SurahInfo?,
    onSelectSurah: (SurahInfo) -> Unit
) {
    val allSurahs = remember { QuranMetadata.getAllSurahs() }

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
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Mushaf Al-Qur'an",
                style = MaterialTheme.typography.headlineSmall,
                color = DarkColors.Gold,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Baca Al-Qur'an langsung di aplikasi",
                style = MaterialTheme.typography.bodySmall,
                color = DarkColors.HeaderSubtitle
            )
        }

        // Banner lanjutkan bacaan terakhir
        if (lastReadSurah != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkColors.SurfaceVariant)
                    .clickable { onSelectSurah(lastReadSurah) },
                color = DarkColors.SurfaceVariant
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Bookmark, null, tint = DarkColors.Gold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Terakhir Dibaca",
                            style = MaterialTheme.typography.labelMedium,
                            color = DarkColors.Gold,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${lastReadSurah.number}. ${lastReadSurah.nameIndonesian} (${lastReadSurah.nameArabic})",
                            style = MaterialTheme.typography.labelSmall,
                            color = DarkColors.TextSecondary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DarkColors.Gold
                    ) {
                        Text(
                            "Lanjutkan",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Daily portion banner
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkColors.Surface)
                .clickable {
                    if (todaySurahs.isNotEmpty()) onSelectSurah(todaySurahs.first())
                },
            color = DarkColors.Surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.MenuBook, null, tint = DarkColors.Gold, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Juz Hari Ini: $dailyJuz",
                        style = MaterialTheme.typography.labelMedium,
                        color = DarkColors.Gold,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        todaySurahs.joinToString(", ") { it.nameIndonesian },
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkColors.TextSecondary
                    )
                }
                Icon(Icons.Default.ChevronRight, null, tint = DarkColors.TextTertiary, modifier = Modifier.size(20.dp))
            }
        }

        // Surah list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(allSurahs, key = { it.number }) { surah ->
                val hasText = QuranTextData.isTextAvailable(surah.number)
                val isToday = todaySurahs.any { it.number == surah.number }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isToday) DarkColors.Gold.copy(alpha = 0.08f) else Color.Transparent
                        )
                        .clickable { onSelectSurah(surah) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Number
                    Text(
                        text = "${surah.number}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isToday) DarkColors.Gold else DarkColors.TextTertiary,
                        modifier = Modifier.width(36.dp)
                    )

                    // Arabic name
                    Text(
                        text = surah.nameArabic,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkColors.TextPrimary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.width(120.dp)
                    )

                    // Indonesian name
                    Text(
                        text = surah.nameIndonesian,
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkColors.TextSecondary,
                        modifier = Modifier.weight(1f)
                    )

                    // Ayah count
                    Text(
                        text = "${surah.ayahCount} ayat",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkColors.TextTertiary
                    )

                    // Badge
                    if (hasText) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DarkColors.Primary.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "Tersedia",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = DarkColors.PrimaryLight,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun SurahReaderScreen(surah: SurahInfo, onBack: () -> Unit) {
    val ayahs = remember { QuranTextData.getSurahText(surah.number) }
    val summary = remember { QuranTextData.getTranslationSummary(surah.number) }
    val hasText = ayahs.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColors.Background)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkColors.PrimaryDark)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, null, tint = DarkColors.Gold)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "$surah.number. ${surah.nameArabic}",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkColors.Gold,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${surah.nameIndonesian} - ${surah.ayahCount} Ayat - ${surah.revelationType}",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkColors.HeaderSubtitle
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (hasText) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Bismillah
                item {
                    Text(
                        text = "\u0628\u0650\u0633\u0652\u0645\u0650 \u0627\u0644\u0644\u0651\u0647\u0650 \u0627\u0644\u0631\u064E\u062D\u0645\u064E\u0646\u0650 \u0627\u0644\u0631\u064E\u062D\u0650\u064A\u0645\u0650",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkColors.Gold,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Ayahs
                items(ayahs, key = { it.number }) { ayah ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Ayah number badge
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DarkColors.SurfaceVariant
                        ) {
                            Text(
                                text = "${ayah.number}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = DarkColors.Gold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Arabic text
                        Text(
                            text = ayah.arabic,
                            style = MaterialTheme.typography.headlineMedium,
                            color = DarkColors.TextPrimary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 36.sp
                        )
                    }
                }

                // Translation summary
                if (summary.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = DarkColors.Border, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Penjelasan:",
                            style = MaterialTheme.typography.labelMedium,
                            color = DarkColors.Gold,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DarkColors.TextSecondary,
                            lineHeight = 22.sp
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        } else {
            // No text available
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.MenuBook, null,
                        tint = DarkColors.TextTertiary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Teks surah ${surah.nameIndonesian}",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkColors.TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Teks lengkap ${surah.nameArabic} akan tersedia\npada update selanjutnya.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkColors.TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
