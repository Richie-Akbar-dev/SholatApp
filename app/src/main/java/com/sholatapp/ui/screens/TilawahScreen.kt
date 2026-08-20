package com.sholatapp.ui.screens

import android.content.Context
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.data.TilawahAyahRef
import com.sholatapp.data.TilawahAyahText
import com.sholatapp.data.TilawahData
import com.sholatapp.data.TilawahProgressInfo
import java.text.SimpleDateFormat
import java.util.*

// ==================== TILAWAH COLORS ====================
private object TilawahColors {
    val Background = Color(0xFF0A1A0A)
    val Gold = Color(0xFFD4AF37)
    val GoldLight = Color(0xFFFFD54F)
    val GoldDim = Color(0xFFB8941F)
    val Green = Color(0xFF1B4D3E)
    val GreenLight = Color(0xFF4CAF50)
    val White = Color(0xFFFFFFFF)
    val TextMuted = Color(0xFFA5D6A7)
    val TextDim = Color(0xFF6B9B6B)
    val Surface = Color(0xFF112211)
    val CardBg = Color(0xB3112211)
    val CardBorder = Color(0x33D4AF37)
    val ArabicGreen = Color(0xFF2E7D32)
    val ReadGreen = Color(0xFF4CAF50)
}

// ==================== MAIN SCREEN ====================
@Composable
fun TilawahScreen(context: Context) {
    val progress = remember { TilawahData.getProgress(context) }
    var refreshKey by remember { mutableIntStateOf(0) }
    val currentProgress = remember(refreshKey) { TilawahData.getProgress(context) }
    val todayPortion = remember(refreshKey) { TilawahData.getTodayPortion(context) }
    val isTodayRead = remember(refreshKey) { TilawahData.isTodayRead(context) }
    val streak = remember(refreshKey) { TilawahData.getStreak(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TilawahColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            "Baca Al-Qur'an",
            style = MaterialTheme.typography.headlineMedium,
            color = TilawahColors.Gold,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Porsi harian untuk khatam",
            style = MaterialTheme.typography.bodySmall,
            color = TilawahColors.TextMuted
        )

        Spacer(modifier = Modifier.height(20.dp))

        // === PROGRESS HERO CARD ===
        Card(
            colors = CardDefaults.cardColors(containerColor = TilawahColors.CardBg),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, TilawahColors.CardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Progress Khatam",
                    style = MaterialTheme.typography.labelLarge,
                    color = TilawahColors.TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Circular progress number
                Text(
                    "${currentProgress.completionPercent}%",
                    style = MaterialTheme.typography.displaySmall,
                    color = TilawahColors.Gold,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Progress bar
                LinearProgressIndicator(
                    progress = { currentProgress.completionPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .height(8.dp),
                    color = TilawahColors.Gold,
                    trackColor = TilawahColors.Surface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "${currentProgress.versesRead}/${currentProgress.totalVerses} ayat",
                    style = MaterialTheme.typography.bodySmall,
                    color = TilawahColors.TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === STATS ROW ===
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MiniStatCard(
                modifier = Modifier.weight(1f),
                value = "${currentProgress.versesPerDay}",
                label = "Ayat/hari"
            )
            MiniStatCard(
                modifier = Modifier.weight(1f),
                value = "$streak",
                label = "Hari berturut"
            )
            MiniStatCard(
                modifier = Modifier.weight(1f),
                value = "${currentProgress.targetDays}",
                label = "Hari target"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // === TODAY'S PORTION ===
        Text(
            "Porsi Hari Ini",
            style = MaterialTheme.typography.titleMedium,
            color = TilawahColors.White,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date()),
            style = MaterialTheme.typography.bodySmall,
            color = TilawahColors.TextMuted
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Portion references
        todayPortion.forEach { ref ->
            Card(
                colors = CardDefaults.cardColors(containerColor = TilawahColors.CardBg),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TilawahColors.CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "${ref.surahName} (${ref.surahNameArabic})",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TilawahColors.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            if (ref.ayahStart == ref.ayahEnd)
                                "Ayat ${ref.ayahStart}"
                            else
                                "Ayat ${ref.ayahStart} – ${ref.ayahEnd}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TilawahColors.Gold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // === SAMPLE AYAH DISPLAY (Al-Fatihah only) ===
        if (todayPortion.any { it.surahNumber == 1 }) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Teks Ayat",
                style = MaterialTheme.typography.titleMedium,
                color = TilawahColors.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            val sampleAyahs = TilawahData.alFatihahSample
            sampleAyahs.forEach { (ayahNum, text) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = TilawahColors.Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        // Ayah number badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(TilawahColors.Gold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "$ayahNum",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TilawahColors.Gold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Al-Fatihah : $ayahNum",
                                style = MaterialTheme.typography.labelMedium,
                                color = TilawahColors.TextDim
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 1. Arabic
                        Text(
                            text.arabic,
                            style = MaterialTheme.typography.headlineSmall,
                            color = TilawahColors.ArabicGreen,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 32.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 2. Latin
                        Text(
                            text.latin,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TilawahColors.GoldDim,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // 3. Indonesian
                        Text(
                            text.translation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TilawahColors.TextMuted,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 22.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Note: untuk selain Al-Fatihah, teks lengkap akan ditambahkan
        if (!todayPortion.any { it.surahNumber == 1 }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = TilawahColors.Surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, null, tint = TilawahColors.GoldDim, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Teks lengkap untuk surah ini akan tersedia di update selanjutnya.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TilawahColors.TextDim
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // === MARK AS READ BUTTON ===
        Button(
            onClick = {
                TilawahData.markTodayAsRead(context)
                refreshKey++
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isTodayRead,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isTodayRead) TilawahColors.Green.copy(alpha = 0.5f) else TilawahColors.Green,
                contentColor = TilawahColors.White,
                disabledContainerColor = TilawahColors.Surface,
                disabledContentColor = TilawahColors.TextDim
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                if (isTodayRead) Icons.Default.CheckCircle else Icons.Default.MenuBook,
                null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                if (isTodayRead) "Sudah Dibaca Hari Ini" else "Tandai Sudah Dibaca",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ==================== MINI STAT CARD ====================
@Composable
private fun MiniStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = TilawahColors.CardBg),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TilawahColors.CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineSmall, color = TilawahColors.Gold, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = TilawahColors.TextMuted)
        }
    }
}
}
