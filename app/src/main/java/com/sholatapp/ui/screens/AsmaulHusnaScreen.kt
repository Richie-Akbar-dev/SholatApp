package com.sholatapp.ui.screens

import androidx.compose.foundation.background
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
import com.sholatapp.data.AsmaulHusnaData
import com.sholatapp.ui.theme.DarkColors

/**
 * Layar Asmaul Husna — 99 Nama Allah dengan teks Arab,
 * transliterasi Latin, arti, dan pencarian.
 */
@Composable
fun AsmaulHusnaScreen(onBack: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val results = remember(query) { AsmaulHusnaData.search(query) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColors.Background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkColors.PrimaryDark)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = DarkColors.Gold)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Asmaul Husna",
                    style = MaterialTheme.typography.headlineSmall,
                    color = DarkColors.Gold,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "99 Nama Allah yang Indah",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkColors.TextSecondary
                )
            }
        }

        // Search bar
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            placeholder = { Text("Cari nama atau arti...", color = DarkColors.TextTertiary) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = DarkColors.Gold) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, null, tint = DarkColors.TextTertiary)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkColors.Gold,
                unfocusedBorderColor = DarkColors.Border,
                cursorColor = DarkColors.Gold,
                focusedTextColor = DarkColors.TextPrimary,
                unfocusedTextColor = DarkColors.TextPrimary
            )
        )

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(results, key = { it.number }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Nomor bulat
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(19.dp))
                                .background(DarkColors.Gold.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "${item.number}",
                                style = MaterialTheme.typography.labelMedium,
                                color = DarkColors.Gold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.arabic,
                                style = MaterialTheme.typography.headlineSmall,
                                color = DarkColors.TextPrimary,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 26.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.latin,
                                style = MaterialTheme.typography.titleSmall,
                                color = DarkColors.Gold,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = item.meaning,
                                style = MaterialTheme.typography.bodySmall,
                                color = DarkColors.TextSecondary
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                if (results.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.SearchOff, null,
                            tint = DarkColors.TextTertiary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Tidak ada hasil untuk \"$query\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DarkColors.TextSecondary
                        )
                    }
                }
            }
        }
    }
}
