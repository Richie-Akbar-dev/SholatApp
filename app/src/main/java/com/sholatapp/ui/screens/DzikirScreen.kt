package com.sholatapp.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.data.DzikirData
import com.sholatapp.model.DzikirCategory
import com.sholatapp.model.DzikirItem
import com.sholatapp.ui.theme.DarkColors
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DzikirScreen(onQuranClick: () -> Unit = {}) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(DzikirCategory.PAGI) }
    val prefs = context.getSharedPreferences("dzikir_prefs", Context.MODE_PRIVATE)
    val todayKey = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    
    // Load today's progress
    val progressMap = remember(todayKey) {
        mutableStateMapOf<String, Int>().apply {
            DzikirData.getAllDzikir().forEach { item ->
                val saved = prefs.getInt("${todayKey}_${item.id}", 0)
                this[item.id] = saved
            }
        }
    }
    
    // Total completed
    val totalCompleted by remember {
        derivedStateOf {
            DzikirData.getAllDzikir().count { item ->
                (progressMap[item.id] ?: 0) >= item.targetCount
            }
        }
    }
    val totalItems = DzikirData.getAllDzikir().filter { it.category == selectedCategory }.size
    val completedInCategory = DzikirData.getAllDzikir().filter { 
        it.category == selectedCategory && (progressMap[it.id] ?: 0) >= it.targetCount 
    }.size

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
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Dzikir Harian",
                style = MaterialTheme.typography.headlineSmall,
                color = DarkColors.Gold,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$totalCompleted/${DzikirData.getAllDzikir().size} dzikir selesai hari ini",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkColors.HeaderSubtitle
            )
            
            // Category tabs
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DzikirCategory.entries.forEach { cat ->
                    val isSelected = cat == selectedCategory
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = cat },
                        color = if (isSelected) DarkColors.Gold else DarkColors.Surface,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = cat.displayName,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) Color.Black else DarkColors.TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Entri Al-Qur'an (v2.3): mushaf lengkap 114 surah kini disatukan
        // di halaman Zikir — juga bisa dibuka dari tindakan cepat Beranda
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clickable(onClick = onQuranClick),
            colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
            shape = RoundedCornerShape(16.dp),
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
                        .background(DarkColors.PrimaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = "Al-Qur'an",
                        tint = DarkColors.Primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Al-Qur'an",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkColors.TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "114 Surah lengkap · Arab, Latin & Arti",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkColors.TextTertiary,
                        maxLines = 1
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = DarkColors.TextTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Progress bar for category
        LinearProgressIndicator(
            progress = { if (totalItems > 0) completedInCategory.toFloat() / totalItems else 0f },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp),
            color = DarkColors.Gold,
            trackColor = DarkColors.Border,
            strokeCap = StrokeCap.Round
        )

        // Dzikir list
        val filteredItems = DzikirData.getAllDzikir().filter { it.category == selectedCategory }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredItems, key = { it.id }) { item ->
                DzikirCard(
                    item = item,
                    currentCount = progressMap[item.id] ?: 0,
                    onCount = { newCount ->
                        progressMap[item.id] = newCount
                        prefs.edit().putInt("${todayKey}_${item.id}", newCount).apply()
                        // Vibrate on tap
                        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                        if (vibrator?.hasVibrator() == true) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(30, 100))
                            } else {
                                @Suppress("DEPRECATION")
                                vibrator.vibrate(30)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun DzikirCard(
    item: DzikirItem,
    currentCount: Int,
    onCount: (Int) -> Unit,
) {
    val isCompleted = currentCount >= item.targetCount
    val progress = if (item.targetCount > 0) currentCount.toFloat() / item.targetCount else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(300),
        label = "progress"
    )
    val cardColor by animateColorAsState(
        targetValue = if (isCompleted) DarkColors.SurfaceVariant else DarkColors.Surface,
        animationSpec = tween(300),
        label = "cardColor"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top row: Arabic + counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.arabic,
                        style = MaterialTheme.typography.titleLarge,
                        color = if (isCompleted) DarkColors.Gold else DarkColors.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.latin,
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkColors.TextSecondary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Counter button
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCompleted) DarkColors.Primary else DarkColors.Gold.copy(alpha = 0.15f),
                            CircleShape
                        )
                        .border(
                            2.dp,
                            if (isCompleted) DarkColors.PrimaryLight else DarkColors.Gold,
                            CircleShape
                        )
                        .clickable(enabled = !isCompleted) {
                            onCount(currentCount + 1)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selesai",
                            tint = DarkColors.PrimaryLight,
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$currentCount",
                                style = MaterialTheme.typography.headlineSmall,
                                color = DarkColors.Gold,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "/${item.targetCount}",
                                style = MaterialTheme.typography.labelSmall,
                                color = DarkColors.TextTertiary
                            )
                        }
                    }
                }
            }

            // Translation
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.translation,
                style = MaterialTheme.typography.bodySmall,
                color = DarkColors.TextTertiary
            )

            // Progress bar
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = if (isCompleted) DarkColors.PrimaryLight else DarkColors.Gold,
                trackColor = DarkColors.Border,
                strokeCap = StrokeCap.Round
            )
        }
    }
}
