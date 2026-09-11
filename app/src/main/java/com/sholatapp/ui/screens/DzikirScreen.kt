package com.sholatapp.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.data.DzikirData
import com.sholatapp.model.DzikirCategory
import com.sholatapp.model.DzikirItem
import com.sholatapp.ui.theme.DarkColors
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

/**
 * Halaman Zikir — v2.5.
 *
 * Perubahan dari versi sebelumnya:
 *  - Chip kategori berlabel pendek dalam baris scroll-safe (fix overflow:
 *    chip "Umum" sebelumnya tak terjangkau di layar kecil).
 *  - Kategori terbuka otomatis sesuai jam (pagi/siang/petang).
 *  - Progres kategori berlabel di header section (menggantikan bar 3dp
 *    tanpa konteks).
 *  - Counter: ketuk lingkaran = +1 (getar), tahan ±3 detik = kurangi 1
 *    (undo salah hitung). Kartu selesai tampil centang + "Selesai".
 *  - Ketuk kartu = pop up fokus: teks penuh + counter besar item tersebut
 *    (menggantikan hero tasbih terpisah).
 *  - Palet warna tidak berubah.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DzikirScreen(onQuranClick: () -> Unit = {}) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(autoCategory()) }
    var sheetItem by remember { mutableStateOf<DzikirItem?>(null) }

    val prefs = context.getSharedPreferences("dzikir_prefs", Context.MODE_PRIVATE)
    val todayKey = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    val allItems = remember { DzikirData.getAllDzikir() }

    // Muat progres hari ini
    val progressMap = remember(todayKey) {
        mutableStateMapOf<String, Int>().apply {
            allItems.forEach { item ->
                this[item.id] = prefs.getInt("${todayKey}_${item.id}", 0)
            }
        }
    }

    val totalCompleted by remember {
        derivedStateOf {
            allItems.count { item -> (progressMap[item.id] ?: 0) >= item.targetCount }
        }
    }

    val filteredItems = remember(selectedCategory) {
        allItems.filter { it.category == selectedCategory }
    }
    val totalItems = filteredItems.size
    val completedInCategory = filteredItems.count { item ->
        (progressMap[item.id] ?: 0) >= item.targetCount
    }

    // Umpan balik getar (30ms tambah, 60ms kurangi)
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator }
    val vibrate: (Long) -> Unit = { ms ->
        if (vibrator?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(ms)
            }
        }
    }

    val setCount: (DzikirItem, Int) -> Unit = { item, value ->
        val clamped = value.coerceIn(0, item.targetCount)
        progressMap[item.id] = clamped
        prefs.edit().putInt("${todayKey}_${item.id}", clamped).apply()
    }

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
                text = "$totalCompleted/${allItems.size} dzikir selesai hari ini",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkColors.HeaderSubtitle
            )

            // Chip kategori — label pendek + baris scroll-safe (anti overflow)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DzikirCategory.entries.forEach { cat ->
                    val isSelected = cat == selectedCategory
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = cat },
                        color = if (isSelected) DarkColors.Gold else Color.White.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = cat.shortLabel,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) Color.Black else DarkColors.HeaderSubtitle,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // ==================== ENTRI AL-QUR'AN ====================
        // Mushaf lengkap 114 surah (Arab, Latin, Arti) — satu database
        // dengan halaman Mushaf Bacaan Terarah.
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

        // ==================== SECTION: PROGRES KATEGORI ====================
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedCategory.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$completedInCategory/$totalItems selesai",
                    style = MaterialTheme.typography.labelMedium,
                    color = DarkColors.Gold,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { if (totalItems > 0) completedInCategory.toFloat() / totalItems else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = DarkColors.Gold,
                trackColor = DarkColors.Border,
                strokeCap = StrokeCap.Round
            )
        }

        // ==================== DAFTAR DZIKIR ====================
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredItems, key = { it.id }) { item ->
                DzikirCard(
                    item = item,
                    currentCount = progressMap[item.id] ?: 0,
                    onIncrement = {
                        setCount(item, (progressMap[item.id] ?: 0) + 1)
                        vibrate(30)
                    },
                    onDecrement = {
                        setCount(item, (progressMap[item.id] ?: 0) - 1)
                        vibrate(60)
                    },
                    onOpenDetail = { sheetItem = item }
                )
            }
        }
    }

    // ==================== POP UP FOKUS ====================
    // Ketuk kartu -> lembar berisi teks penuh + counter besar item tersebut.
    val focusedItem = sheetItem
    if (focusedItem != null) {
        ModalBottomSheet(
            onDismissRequest = { sheetItem = null },
            containerColor = DarkColors.Surface,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            DzikirDetailSheet(
                item = focusedItem,
                currentCount = progressMap[focusedItem.id] ?: 0,
                onIncrement = {
                    setCount(focusedItem, (progressMap[focusedItem.id] ?: 0) + 1)
                    vibrate(30)
                },
                onDecrement = {
                    setCount(focusedItem, (progressMap[focusedItem.id] ?: 0) - 1)
                    vibrate(60)
                }
            )
        }
    }
}

/** Kategori awal otomatis mengikuti waktu perangkat. */
private fun autoCategory(): DzikirCategory {
    return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 4..10 -> DzikirCategory.PAGI
        in 11..15 -> DzikirCategory.SETELAH_SHOLAT
        else -> DzikirCategory.PETANG
    }
}

@Composable
private fun DzikirCard(
    item: DzikirItem,
    currentCount: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onOpenDetail: () -> Unit
) {
    val isCompleted = currentCount >= item.targetCount
    val progress = if (item.targetCount > 0) currentCount.toFloat() / item.targetCount else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(300),
        label = "dzikirProgress"
    )
    val cardColor by animateColorAsState(
        targetValue = if (isCompleted) DarkColors.SurfaceVariant else DarkColors.Surface,
        animationSpec = tween(300),
        label = "dzikirCardColor"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Area teks — ketuk untuk membuka pop up fokus
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onOpenDetail)
                ) {
                    if (item.title.isNotBlank()) {
                        Surface(
                            color = DarkColors.Gold.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = item.title.uppercase(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = DarkColors.Gold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    Text(
                        text = item.arabic,
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkColors.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\u201C${item.latin}\u201D",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkColors.TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.translation,
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkColors.TextTertiary,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                DzikirCounterCircle(
                    size = 64.dp,
                    item = item,
                    currentCount = currentCount,
                    onIncrement = onIncrement,
                    onDecrement = onDecrement
                )
            }

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

/**
 * Lingkaran counter: ketuk = +1, tahan ±3 detik = kurangi 1 (undo).
 * Dipakai di kartu daftar (64dp) dan di pop up fokus (96dp).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DzikirCounterCircle(
    size: Dp,
    item: DzikirItem,
    currentCount: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val isCompleted = currentCount >= item.targetCount
    var longFired by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Deteksi tahan ±3 detik (bukan long-press bawaan yang ~0,5 detik)
    LaunchedEffect(isPressed) {
        if (isPressed) {
            longFired = false
            delay(3000)
            if (isPressed) {
                longFired = true
                onDecrement()
            }
        }
    }

    Box(
        modifier = Modifier
            .size(size)
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
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { if (!longFired && !isCompleted) onIncrement() }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selesai",
                    tint = DarkColors.PrimaryLight,
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = "Selesai",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkColors.PrimaryLight
                )
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$currentCount",
                    style = if (size >= 80.dp) MaterialTheme.typography.displaySmall
                    else MaterialTheme.typography.headlineSmall,
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

/** Isi pop up fokus: judul, teks penuh (Arab/Latin/Arti), counter besar. */
@Composable
private fun DzikirDetailSheet(
    item: DzikirItem,
    currentCount: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (item.title.isNotBlank()) {
                Surface(
                    color = DarkColors.Gold.copy(alpha = 0.14f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = item.title.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkColors.Gold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${currentCount.coerceAtMost(item.targetCount)} / ${item.targetCount}",
                style = MaterialTheme.typography.titleSmall,
                color = DarkColors.Gold,
                fontWeight = FontWeight.Bold
            )
        }

        // Teks penuh bisa digulir (aman untuk wirid panjang seperti Ayat Kursi)
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp)
        ) {
            Text(
                text = item.arabic,
                fontSize = 24.sp,
                lineHeight = 42.sp,
                color = DarkColors.TextPrimary,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "\u201C${item.latin}\u201D",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.translation,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkColors.TextTertiary
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            DzikirCounterCircle(
                size = 96.dp,
                item = item,
                currentCount = currentCount,
                onIncrement = onIncrement,
                onDecrement = onDecrement
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ketuk lingkaran untuk menambah · tahan 3 detik untuk mengurangi",
            style = MaterialTheme.typography.labelSmall,
            color = DarkColors.TextTertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
