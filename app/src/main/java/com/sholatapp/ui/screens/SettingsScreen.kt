package com.sholatapp.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.content.Context
import com.sholatapp.azan.AzanPlayer
import com.sholatapp.dnd.DndHelper
import com.sholatapp.data.TilawahData
import com.sholatapp.ui.theme.DarkColors
import com.sholatapp.viewmodel.UiState
import kotlin.math.ceil

@Composable
fun SettingsScreen(
    uiState: UiState,
    onToggleAlarm: (Boolean) -> Unit,
    onTogglePrepAlarm: (Boolean) -> Unit,
    onToggleDnd: (Boolean) -> Unit,
    context: Context? = null,
    onResetDzikir: (() -> Unit)? = null,
    onChangeAzan: (() -> Unit)? = null
) {
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
                text = "Pengaturan",
                style = MaterialTheme.typography.headlineSmall,
                color = DarkColors.Gold,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Konfigurasi aplikasi SholatApp",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkColors.HeaderSubtitle
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Notifikasi Section
            SectionHeader(title = "Notifikasi")
            SettingToggleRow(
                title = "Alarm Pengingat Sholat",
                subtitle = "Notifikasi dan suara azan saat waktu sholat",
                checked = uiState.isAlarmEnabled,
                onCheckedChange = onToggleAlarm
            )
            SettingToggleRow(
                title = "Alarm Bangunkan (20 menit sebelum)",
                subtitle = "Notifikasi 20 menit sebelum adzan untuk persiapan",
                checked = uiState.isPrepAlarmEnabled,
                onCheckedChange = onTogglePrepAlarm
            )

            // DND Focus Mode Section
            SectionHeader(title = "Fokus Mode (DND)")
            SettingToggleRow(
                title = "Mode Fokus saat Sholat",
                subtitle = "Aktifkan DND otomatis saat adzan selama 30 menit",
                checked = uiState.isDndEnabled,
                onCheckedChange = onToggleDnd
            )
            val hasDndPerm = context?.let { DndHelper.hasDndPermission(it) } ?: false
            if (uiState.isDndEnabled && !hasDndPerm) {
                SettingActionRow(
                    title = "Izin DND Belum Diberikan",
                    subtitle = "Tap untuk membuka pengaturan sistem",
                    icon = Icons.Default.Security,
                    onClick = {
                        context?.let { DndHelper.requestDndPermission(it) }
                    }
                )
            }
            if (uiState.isDndEnabled && hasDndPerm) {
                SettingInfoRow(
                    title = "Status DND",
                    value = "Izin diberikan — DND otomatis aktif saat adzan"
                )
            }

            // Azan Section
            SectionHeader(title = "Suara Azan")
            AzanSettingCard(
                context = context,
                onChangeAzan = { onChangeAzan?.invoke() }
            )

            // Location Section
            SectionHeader(title = "Lokasi")
            SettingInfoRow(
                title = "Lokasi Saat Ini",
                value = uiState.locationAddress
            )
            SettingInfoRow(
                title = "Koordinat",
                value = if (uiState.latitude != 0.0 || uiState.longitude != 0.0)
                    String.format("%.4f, %.4f", uiState.latitude, uiState.longitude)
                else "Belum tersedia"
            )

            // Method Section
            SectionHeader(title = "Metode Perhitungan")
            SettingInfoRow(title = "Metode", value = "KEMENAG RI")
            SettingInfoRow(title = "Sudut Fajr", value = "20.0°")
            SettingInfoRow(title = "Sudut Isya", value = "18.0°")
            SettingInfoRow(title = "Metode Asar", value = "Syafi'i (bayangan = 1x)")

            // Target Khatam Section
            SectionHeader(title = "Target Khatam Al-Qur'an")
            KhatamTargetSetting(context = context)

            // Data Section
            SectionHeader(title = "Data")
            SettingActionRow(
                title = "Reset Progress Dzikir",
                subtitle = "Hapus semua progress dzikir hari ini",
                icon = Icons.Default.Refresh,
                onClick = { onResetDzikir?.invoke() }
            )

            // About Section
            SectionHeader(title = "Tentang")
            SettingInfoRow(title = "Versi", value = "2.5.0")
            SettingInfoRow(title = "Perhitungan", value = "Berdasarkan posisi matahari astronomis")
            SettingInfoRow(title = "Fitur", value = "Sholat, Ibadah, Tilawah, Doa, Mushaf, Asmaul Husna, DND Fokus")

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = DarkColors.Gold,
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
    )
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = DarkColors.TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DarkColors.TextTertiary)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = DarkColors.Primary,
                checkedThumbColor = DarkColors.Gold,
                uncheckedTrackColor = DarkColors.Border,
                uncheckedThumbColor = DarkColors.TextTertiary
            )
        )
    }
}

@Composable
private fun AzanSettingCard(
    context: Context?,
    onChangeAzan: () -> Unit
) {
    val azanPlayer = remember { context?.let { AzanPlayer(it) } }
    val currentAzan = remember { azanPlayer?.getSelectedAzanOption() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkColors.Surface)
            .clickable { onChangeAzan() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.NotificationsActive,
            contentDescription = null,
            tint = DarkColors.PrimaryLight
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Suara Azan",
                style = MaterialTheme.typography.bodyLarge,
                color = DarkColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                if (currentAzan != null) "Aktif: ${currentAzan.name} — ${currentAzan.description}"
                else "Tap untuk memilih suara azan",
                style = MaterialTheme.typography.bodySmall,
                color = if (currentAzan != null) DarkColors.PrimaryLight else DarkColors.TextTertiary
            )
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = "Ganti azan",
            tint = DarkColors.TextTertiary
        )
    }
}

@Composable
private fun SettingInfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = DarkColors.TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, style = MaterialTheme.typography.bodySmall, color = DarkColors.TextSecondary)
        }
    }
}

@Composable
private fun SettingActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkColors.Surface)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = DarkColors.TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DarkColors.TextTertiary)
        }
        Icon(icon, contentDescription = null, tint = DarkColors.TextTertiary)
    }
}

@Composable
private fun KhatamTargetSetting(context: Context?) {
    val prefs = remember { context?.getSharedPreferences("tilawah_prefs", android.content.Context.MODE_PRIVATE) }
    val currentTarget = remember { prefs?.getInt("target_days", 365) ?: 365 }
    var targetDays by remember { mutableIntStateOf(currentTarget) }
    val versesPerDay = remember(targetDays) { ceil(6236.0 / targetDays).toInt() }
    val progress = remember { context?.let { TilawahData.getProgress(it) } }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkColors.Surface)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Target Khatam",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "$targetDays hari ($versesPerDay ayat/hari)",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkColors.TextSecondary
                )
                if (progress != null) {
                    val versesRead = progress.totalReadDays * progress.versesPerDay
                    Text(
                        "Progress: ${"%.1f".format(progress.completionPercent)}% ($versesRead/6236)",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkColors.Gold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Preset buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PresetButton(label = "30 hari", isActive = targetDays == 30, onClick = { targetDays = 30 })
            PresetButton(label = "6 bulan", isActive = targetDays == 180, onClick = { targetDays = 180 })
            PresetButton(label = "1 tahun", isActive = targetDays == 365, onClick = { targetDays = 365 })
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Custom input
        OutlinedTextField(
            value = if (targetDays == 0) "" else "$targetDays",
            onValueChange = { input ->
                val num = input.toIntOrNull()
                if (num != null && num in 1..3650) targetDays = num
            },
            label = { Text("Custom (hari)", color = DarkColors.TextTertiary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(color = DarkColors.TextPrimary),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkColors.Gold,
                unfocusedBorderColor = DarkColors.Border,
                cursorColor = DarkColors.Gold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Save button
        Button(
            onClick = {
                prefs?.edit()?.putInt("target_days", targetDays)?.apply()
                TilawahData.setTargetDays(context ?: return@Button, targetDays)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = DarkColors.Primary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Simpan Target", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun PresetButton(label: String, isActive: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) DarkColors.Gold else DarkColors.SurfaceVariant,
            contentColor = if (isActive) Color.Black else DarkColors.TextSecondary
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        modifier = Modifier.weight(1f)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}
