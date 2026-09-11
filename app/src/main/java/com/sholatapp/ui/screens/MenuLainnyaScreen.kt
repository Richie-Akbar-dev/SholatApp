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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sholatapp.ui.theme.DarkColors

/**
 * Menu Lainnya — pintu akses untuk Mushaf, Doa Harian, Mutabaah,
 * Tilawah, dan Asmaul Husna. Sebelumnya 4 fitur ini tidak dapat
 * diakses dari mana pun di aplikasi (layar orphan).
 */
@Composable
fun MenuLainnyaScreen(onBack: () -> Unit) {
    var selectedMenu by remember { mutableStateOf<String?>(null) }

    // Tombol back sistem: kembali ke menu utama dulu, baru tutup overlay
    BackHandler(enabled = selectedMenu != null) {
        selectedMenu = null
    }

    AnimatedContent(
        targetState = selectedMenu,
        transitionSpec = {
            (slideInHorizontally(animationSpec = tween(260)) { it } +
                    fadeIn(animationSpec = tween(200))) togetherWith
                    (slideOutHorizontally(animationSpec = tween(220)) { it } +
                    fadeOut(animationSpec = tween(160)))
        },
        label = "menuLainnyaContent"
    ) { menu ->
        when (menu) {
            "mushaf" -> MushafScreenWithBack(onBack = { selectedMenu = null })
            "doa" -> DoaScreenWithBack(onBack = { selectedMenu = null })
            "mutabaah" -> IbadahScreenWithBack(onBack = { selectedMenu = null })
            "tilawah" -> TilawahScreenWithBack(onBack = { selectedMenu = null })
            "asmaulhusna" -> AsmaulHusnaScreen(onBack = { selectedMenu = null })
            else -> MenuLainnyaHome(
                onSelect = { selectedMenu = it },
                onBack = onBack
            )
        }
    }
}

/** Pembungkus dengan tombol kembali ke menu. */
@Composable
private fun MushafScreenWithBack(onBack: () -> Unit) {
    Box {
        MushafScreen()
        BackFab(onBack)
    }
}

@Composable
private fun DoaScreenWithBack(onBack: () -> Unit) {
    Box {
        DoaScreen()
        BackFab(onBack)
    }
}

@Composable
private fun IbadahScreenWithBack(onBack: () -> Unit) {
    Box {
        IbadahScreen()
        BackFab(onBack)
    }
}

@Composable
private fun TilawahScreenWithBack(onBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Box {
        TilawahScreen(context = context)
        BackFab(onBack)
    }
}

@Composable
private fun BackFab(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FloatingActionButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.BottomEnd),
            containerColor = DarkColors.Gold,
            contentColor = Color.Black,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Kembali ke Menu")
        }
    }
}

@Composable
private fun MenuLainnyaHome(
    onSelect: (String) -> Unit,
    onBack: () -> Unit
) {
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
            Column {
                Text(
                    text = "Lainnya",
                    style = MaterialTheme.typography.headlineSmall,
                    color = DarkColors.Gold,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Fitur ibadah & pembelajaran lainnya",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkColors.HeaderSubtitle
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MenuLainnyaItem(
                icon = Icons.Default.MenuBook,
                title = "Mushaf Al-Qur'an",
                subtitle = "Baca Al-Qur'an lengkap dengan penjelasan",
                tint = DarkColors.Gold,
                onClick = { onSelect("mushaf") }
            )
            MenuLainnyaItem(
                icon = Icons.Default.Spa,
                title = "Doa Harian",
                subtitle = "Kumpulan doa sehari-hari sesuai sunnah",
                tint = DarkColors.PrimaryLight,
                onClick = { onSelect("doa") }
            )
            MenuLainnyaItem(
                icon = Icons.Default.Insights,
                title = "Mutabaah",
                subtitle = "Kontrol ibadah harian & statistik istiqomah",
                tint = DarkColors.PrimaryLight,
                onClick = { onSelect("mutabaah") }
            )
            MenuLainnyaItem(
                icon = Icons.Default.AutoStories,
                title = "Tilawah",
                subtitle = "Target khatam Al-Qur'an & progres bacaan",
                tint = DarkColors.Gold,
                onClick = { onSelect("tilawah") }
            )
            MenuLainnyaItem(
                icon = Icons.Default.AutoAwesome,
                title = "Asmaul Husna",
                subtitle = "99 Nama Allah beserta artinya",
                tint = DarkColors.Gold,
                onClick = { onSelect("asmaulhusna") }
            )
        }
    }
}

@Composable
private fun MenuLainnyaItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    tint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = tint, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkColors.TextSecondary
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = DarkColors.TextTertiary
            )
        }
    }
}
