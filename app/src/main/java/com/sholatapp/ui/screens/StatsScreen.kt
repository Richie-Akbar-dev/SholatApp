package com.sholatapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.ui.theme.DarkColors
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StatsScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val heatmapData = remember { loadHeatmapData(context) }
    val today = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())

    // Calculate summary
    val allItems = com.sholatapp.data.MutabaahData.getAllItems()
    val totalRequired = allItems.size
    val totalDays = heatmapData.count { it.second }
    val completedDays = heatmapData.count { it.second >= totalRequired }

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
                text = "Statistik Ibadah",
                style = MaterialTheme.typography.headlineSmall,
                color = DarkColors.Gold,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = today,
                style = MaterialTheme.typography.bodySmall,
                color = DarkColors.HeaderSubtitle
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Summary cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Hari Aktif",
                    value = "$totalDays",
                    color = DarkColors.Gold
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Lengkap (100%)",
                    value = "$completedDays",
                    color = DarkColors.PrimaryLight
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Total Checklist",
                    value = "$totalRequired",
                    color = DarkColors.TextPrimary
                )
            }

            // Heatmap card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Kalender Ibadah (90 Hari)",
                        style = MaterialTheme.typography.titleSmall,
                        color = DarkColors.TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Warna menunjukkan persentase penyelesaian harian",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkColors.TextTertiary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HeatmapCanvas(heatmapData, totalRequired)
                    Spacer(modifier = Modifier.height(10.dp))
                    HeatmapLegend()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier, label: String, value: String, color: Color) {
    Surface(
        modifier = modifier,
        color = DarkColors.Surface,
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = DarkColors.TextTertiary
            )
        }
    }
}

@Composable
private fun HeatmapCanvas(
    data: List<Pair<String, Int>>,
    totalRequired: Int
) {
    val textMeasurer = rememberTextMeasurer()
    val dayLabels = listOf("S", "S", "R", "K", "J", "S", "M")
    val cellSize = 14.dp
    val cellGap = 3.dp
    val leftMargin = 18.dp

    // Group data into weeks (columns)
    val weeks = mutableListOf<MutableList<Pair<String, Int>>>()
    var currentWeek = mutableListOf<Pair<String, Int>>()

    // Find start: go back 90 days, align to Sunday
    val startCal = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, -(89 + get(Calendar.DAY_OF_WEEK) - 1))
    }
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val dataMap = data.toMap()

    for (i in 0 until 91) {
        val cal = Calendar.getInstance().apply {
            timeInMillis = startCal.timeInMillis
            add(Calendar.DAY_OF_MONTH, i)
        }
        val key = sdf.format(cal.time)
        val count = dataMap[key] ?: 0
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        currentWeek.add(key to count)
        if (dayOfWeek == Calendar.SATURDAY) {
            weeks.add(currentWeek)
            currentWeek = mutableListOf()
        }
    }
    if (currentWeek.isNotEmpty()) weeks.add(currentWeek)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height((7 * (cellSize + cellGap)).value.dp())
    ) {
        val cellPx = cellSize.toPx()
        val gapPx = cellGap.toPx()
        val leftPx = leftMargin.toPx()

        // Day labels
        dayLabels.forEachIndexed { index, label ->
            val y = index * (cellPx + gapPx)
            val layout = textMeasurer.measure(
                text = label,
                style = TextStyle(color = DarkColors.TextTertiary, fontSize = 9.sp)
            )
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(0f, y + cellPx / 2f - layout.size.height / 2f)
            )
        }

        // Cells
        weeks.forEachIndexed { weekIdx, week ->
            week.forEachIndexed { dayIdx, (_, count) ->
                val x = leftPx + weekIdx * (cellPx + gapPx)
                val y = dayIdx * (cellPx + gapPx)
                val percent = if (totalRequired > 0) count.toFloat() / totalRequired else 0f
                val color = when {
                    count == 0 -> DarkColors.SurfaceVariant
                    percent < 0.25f -> Color(0xFF1B4D1B)
                    percent < 0.5f -> Color(0xFF2E7D32)
                    percent < 0.75f -> Color(0xFF4CAF50)
                    percent < 1f -> Color(0xFF81C784)
                    else -> DarkColors.Gold
                }
                drawRoundRect(
                    color = color,
                    topLeft = Offset(x, y),
                    size = Size(cellPx, cellPx),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            }
        }
    }
}

@Composable
private fun HeatmapLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Sedikit", style = MaterialTheme.typography.labelSmall, color = DarkColors.TextTertiary)
        Spacer(modifier = Modifier.width(4.dp))
        val colors = listOf(
            DarkColors.SurfaceVariant, Color(0xFF1B4D1B), Color(0xFF2E7D32),
            Color(0xFF4CAF50), Color(0xFF81C784), DarkColors.Gold
        )
        colors.forEach { c ->
            Canvas(modifier = Modifier.size(12.dp)) {
                drawRoundRect(
                    color = c,
                    size = Size(12.dp.toPx(), 12.dp.toPx()),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            }
            Spacer(modifier = Modifier.width(2.dp))
        }
        Text("Lengkap", style = MaterialTheme.typography.labelSmall, color = DarkColors.TextTertiary)
    }
}

private fun loadHeatmapData(context: Context): List<Pair<String, Int>> {
    val prefs = context.getSharedPreferences("mutabaah_prefs", 0)
    val result = mutableListOf<Pair<String, Int>>()
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    for (i in 0 until 91) {
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -i) }
        val key = sdf.format(cal.time)
        val count = prefs.getStringSet("${key}_completed", emptySet())?.size ?: 0
        if (count > 0) result.add(key to count)
    }
    return result.reversed()
}