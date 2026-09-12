package com.sholatapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sholatapp.model.MutabaahCategory
import com.sholatapp.ui.theme.DarkColors
import com.sholatapp.viewmodel.MutabaahViewModel

enum class IbadahTab(val label: String, val icon: ImageVector) {
    MUTABAAH("Mutaba\u2019ah", Icons.Default.List),
    DZIKIR("Dzikir", Icons.Default.PanTool),
    STATS("Statistik", Icons.Default.BarChart2)
}

@Composable
fun IbadahScreen(onBack: (() -> Unit)? = null) {
    var selectedTab by remember { mutableStateOf(IbadahTab.MUTABAAH) }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                // v2.10: tombol kembali — halaman kini overlay Beranda
                // (menggantikan pintu "Lainnya" yang dihapus)
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = DarkColors.Gold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Column {
                    Text(
                        text = "Ibadah Harian",
                        style = MaterialTheme.typography.headlineSmall,
                        color = DarkColors.Gold,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Pantau dan tingkatkan kualitas ibadah harianmu",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkColors.HeaderSubtitle
                    )
                }
            }
        }

        // Sub-tabs
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = DarkColors.Surface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IbadahTab.entries.forEach { tab ->
                    val isSelected = tab == selectedTab
                    val tint by animateColorAsState(
                        targetValue = if (isSelected) DarkColors.Gold else DarkColors.TextTertiary,
                        label = "ibTab"
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Icon(tab.icon, contentDescription = tab.label, tint = tint, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(tab.label, style = MaterialTheme.typography.labelSmall, color = tint, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }

        // Content
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (selectedTab) {
                IbadahTab.MUTABAAH -> MutabaahContent()
                IbadahTab.DZIKIR -> DzikirScreen()
                IbadahTab.STATS -> StatsScreen()
            }
        }
    }
}

// ==================== MUTABAAH CONTENT ====================
@Composable
private fun MutabaahContent() {
    val viewModel: MutabaahViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val grouped = uiState.allItems.groupBy { it.category }

    Column(modifier = Modifier.fillMaxSize()) {
        // Summary bar
        SummaryBar(uiState.completionPercent, uiState.completedIds.size, uiState.allItems.size, uiState.streak)

        LinearProgressIndicator(
            progress = { uiState.completionPercent / 100f },
            modifier = Modifier.fillMaxWidth().height(3.dp),
            color = DarkColors.Gold,
            trackColor = DarkColors.Border,
            strokeCap = StrokeCap.Round
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            grouped.forEach { (category, items) ->
                item {
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.titleSmall,
                        color = DarkColors.Gold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp, top = 4.dp)
                    )
                }
                items(items, key = { it.id }) { item ->
                    val isDone = item.id in uiState.completedIds
                    MutabaahItemRow(
                        title = item.title,
                        isSunnah = item.isSunnah,
                        isCompleted = isDone,
                        onClick = { viewModel.toggleItem(item.id) }
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun SummaryBar(percent: Int, completed: Int, total: Int, streak: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkColors.Surface)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SummaryItem(label = "Selesai", value = "$completed/$total", isHighlight = completed == total && total > 0)
        SummaryItem(label = "Persentase", value = "$percent%", isHighlight = percent == 100)
        SummaryItem(label = "Streak", value = "$streak hari", isHighlight = streak >= 3)
    }
}

@Composable
private fun SummaryItem(label: String, value: String, isHighlight: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = if (isHighlight) DarkColors.Gold else DarkColors.TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = DarkColors.TextTertiary
        )
    }
}

@Composable
private fun MutabaahItemRow(
    title: String,
    isSunnah: Boolean,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isCompleted) DarkColors.SurfaceVariant else Color.Transparent,
        animationSpec = tween(300), label = "mutBg"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox circle
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) DarkColors.Primary else Color.Transparent,
                    CircleShape
                )
                .border(
                    width = 1.5.dp,
                    color = if (isCompleted) DarkColors.PrimaryLight else DarkColors.Border,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    Icons.Default.Check, null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCompleted) DarkColors.PrimaryLight else DarkColors.TextPrimary,
                fontWeight = if (isCompleted) FontWeight.Medium else FontWeight.Normal
            )
        }

        if (isSunnah) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = DarkColors.Gold.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "Sunnah",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkColors.Gold
                )
            }
        }
    }
}
