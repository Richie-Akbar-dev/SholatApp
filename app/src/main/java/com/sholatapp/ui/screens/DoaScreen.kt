package com.sholatapp.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.data.DoaData
import com.sholatapp.model.DoaCategory
import com.sholatapp.model.DoaItem
import com.sholatapp.ui.theme.DarkColors

/**
 * Konten Doa Harian — v2.10 (pindah dari halaman "Lainnya").
 *
 * Sejak v2.10 halaman "Lainnya" dihapus (keputusan user): konten Doa kini
 * ditanamkan sebagai KATEGORI di halaman Zikir (chip "Doa"). Karena itu
 * composable ini TANPA header sendiri — header milik halaman Zikir.
 *
 * Perbaikan bawaan dari audit:
 *  - Chip kategori ("Semua" + 8 DoaCategory) kini dalam baris
 *    horizontalScroll — bug overflow lama (chip tak terjangkau) tuntas.
 *  - Warna teks input pencarian dibetulkan (dulu PrimaryContainer yang
 *    kontrasnya aneh) -> TextPrimary.
 */
@Composable
fun DoaContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf<DoaCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val prefs = context.getSharedPreferences("doa_favorites", Context.MODE_PRIVATE)

    val allDoa = remember { DoaData.getAllDoa().toMutableList() }

    // Favorite status stored as Compose state so toggling triggers recomposition
    val favoriteIds = remember {
        mutableStateOf<Set<String>>(
            allDoa.mapNotNull { item ->
                if (prefs.getBoolean("fav_${item.id}", false)) item.id else null
            }.toSet()
        )
    }

    val filteredDoa = remember(selectedCategory, searchQuery, favoriteIds.value) {
        var list = allDoa
        if (selectedCategory != null) list = list.filter { it.category == selectedCategory }
        if (searchQuery.isNotBlank()) {
            val q = searchQuery.lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                        it.latin.lowercase().contains(q) ||
                        it.translation.lowercase().contains(q)
            }
        }
        list.map { it.copy(isFavorite = it.id in favoriteIds.value) }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkColors.Background)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari doa...", color = DarkColors.HeaderPlaceholder) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkColors.Gold,
                unfocusedBorderColor = DarkColors.HeaderPlaceholder,
                cursorColor = DarkColors.Gold,
                focusedTextColor = DarkColors.TextPrimary,
                unfocusedTextColor = DarkColors.TextPrimary
            ),
            leadingIcon = {
                Icon(Icons.Default.Search, null, tint = DarkColors.HeaderPlaceholder)
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    Icon(
                        Icons.Default.Close,
                        null,
                        tint = DarkColors.HeaderPlaceholder,
                        modifier = Modifier.clickable { searchQuery = "" }
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category chips — baris scroll-safe (fix bug overflow: dulu Row
        // statis tanpa horizontalScroll sehingga 5+ kategori tak terjangkau)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // "Semua" chip
            val allSelected = selectedCategory == null
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { selectedCategory = null },
                color = if (allSelected) DarkColors.Gold else DarkColors.Surface,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Semua",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (allSelected) Color.Black else DarkColors.TextSecondary
                )
            }

            DoaCategory.entries.forEach { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { selectedCategory = if (isSelected) null else cat },
                    color = if (isSelected) DarkColors.Gold else DarkColors.Surface,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = cat.displayName,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) Color.Black else DarkColors.TextSecondary,
                        maxLines = 1
                    )
                }
            }
        }

        // Results count
        Text(
            text = "${filteredDoa.size} doa",
            style = MaterialTheme.typography.labelSmall,
            color = DarkColors.TextTertiary,
            modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 4.dp)
        )

        // Doa list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredDoa, key = { it.id }) { item ->
                DoaCard(
                    item = item,
                    onToggleFavorite = {
                        val isFav = item.id !in favoriteIds.value
                        prefs.edit().putBoolean("fav_${item.id}", isFav).apply()
                        favoriteIds.value = if (isFav) favoriteIds.value + item.id else favoriteIds.value - item.id
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun DoaCard(
    item: DoaItem,
    onToggleFavorite: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category badge
                Text(
                    text = item.category.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkColors.Gold,
                    modifier = Modifier
                        .background(DarkColors.Gold.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                // Favorite icon
                IconButton(
                    onClick = { onToggleFavorite() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (item.isFavorite) "Hapus favorit" else "Favorit",
                        tint = if (item.isFavorite) DarkColors.Gold else DarkColors.TextTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = DarkColors.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            // Expand/collapse indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = if (expanded) "Tutup" else "Buka",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkColors.Gold
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = DarkColors.Gold,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Expanded content
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Arabic text
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = DarkColors.SurfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = item.arabic,
                            style = MaterialTheme.typography.titleMedium,
                            color = DarkColors.Gold,
                            textAlign = TextAlign.Right,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(16.dp),
                            lineHeight = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Latin
                    Text(
                        text = item.latin,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Translation
                    Row(modifier = Modifier.padding(horizontal = 4.dp)) {
                        Text(
                            text = "Arti: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkColors.Gold,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.translation,
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkColors.TextSecondary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
