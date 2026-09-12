package com.sholatapp.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sholatapp.model.PrayerSchedule
import com.sholatapp.ui.theme.DarkColors
import com.sholatapp.viewmodel.UiState

/**
 * Pusat Notifikasi — menggantikan TODO tombol lonceng di Beranda.
 * Menampilkan status alarm sholat, alarm persiapan, mode fokus (DND),
 * serta daftar notifikasi yang akan dikirim untuk setiap waktu sholat.
 */
@Composable
fun PusatNotifikasiScreen(
    uiState: UiState,
    onBack: () -> Unit,
    onToggleSunnahReminder: (Boolean) -> Unit = {}
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
                    text = "Pusat Notifikasi",
                    style = MaterialTheme.typography.headlineSmall,
                    color = DarkColors.Gold,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Status pengingat sholat Anda",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ringkasan status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatusCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Alarm,
                    title = "Alarm Sholat",
                    isActive = uiState.isAlarmEnabled,
                    activeText = "Aktif",
                    inactiveText = "Mati"
                )
                StatusCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Snooze,
                    title = "Alarm 20 Menit",
                    isActive = uiState.isPrepAlarmEnabled,
                    activeText = "Aktif",
                    inactiveText = "Mati"
                )
                StatusCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.DoNotDisturbOn,
                    title = "Fokus (DND)",
                    isActive = uiState.isDndEnabled,
                    activeText = "Aktif",
                    inactiveText = "Mati"
                )
            }

            // Info notifikasi otomatis
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = DarkColors.Gold,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Notifikasi Otomatis",
                            style = MaterialTheme.typography.bodyLarge,
                            color = DarkColors.TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = buildString {
                                append("Saat waktu sholat tiba, aplikasi mengirim notifikasi")
                                if (uiState.isPrepAlarmEnabled) append(" dan pengingat 20 menit sebelumnya")
                                if (uiState.isDndEnabled) append(", lalu mengaktifkan Mode Fokus (DND) selama 30 menit")
                                append(". Atur di menu Pengaturan.")
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkColors.TextSecondary
                        )
                    }
                }
            }

            // Toggle pengingat puasa sunnah (v2.7)
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.EventRepeat,
                        contentDescription = null,
                        tint = DarkColors.Gold,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Pengingat Puasa Sunnah",
                            style = MaterialTheme.typography.bodyLarge,
                            color = DarkColors.TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Notifikasi pukul 20:00 pada malam sebelum Senin, Kamis, dan Ayyamul Bidh",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkColors.TextSecondary
                        )
                    }
                    Switch(
                        checked = uiState.isSunnahReminderEnabled,
                        onCheckedChange = onToggleSunnahReminder
                    )
                }
            }

            // Daftar jadwal + status notifikasi
            Text(
                text = "JADWAL NOTIFIKASI HARI INI",
                style = MaterialTheme.typography.labelLarge,
                color = DarkColors.Gold,
                modifier = Modifier.padding(top = 4.dp)
            )

            val schedule = uiState.prayerSchedule
            if (schedule != null) {
                NotifikasiScheduleList(schedule, uiState)
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkColors.Surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                color = DarkColors.Gold,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Menunggu lokasi & jadwal sholat...",
                                style = MaterialTheme.typography.bodySmall,
                                color = DarkColors.TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isActive: Boolean,
    activeText: String,
    inactiveText: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) DarkColors.PrimaryContainer else DarkColors.Surface
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = if (isActive) DarkColors.PrimaryLight else DarkColors.TextTertiary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                title,
                style = MaterialTheme.typography.labelSmall,
                color = DarkColors.TextSecondary
            )
            Text(
                if (isActive) activeText else inactiveText,
                style = MaterialTheme.typography.labelMedium,
                color = if (isActive) DarkColors.PrimaryLight else DarkColors.TextTertiary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun NotifikasiScheduleList(schedule: PrayerSchedule, uiState: UiState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        schedule.prayerList.forEach { prayer ->
            val isNext = schedule.getNextPrayer(
                java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY),
                java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE)
            )?.nameKey == prayer.nameKey

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isNext) DarkColors.PrimaryContainer else DarkColors.Surface
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (uiState.isAlarmEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                        contentDescription = null,
                        tint = if (uiState.isAlarmEnabled) DarkColors.PrimaryLight else DarkColors.TextTertiary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            prayer.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = DarkColors.TextPrimary,
                            fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold
                        )
                        Text(
                            if (uiState.isAlarmEnabled)
                                "Azan akan berbunyi${if (uiState.isPrepAlarmEnabled) " • pengingat 20 menit sebelumnya" else ""}"
                            else
                                "Notifikasi mati — aktifkan di Pengaturan",
                            style = MaterialTheme.typography.labelSmall,
                            color = DarkColors.TextSecondary
                        )
                    }
                    Text(
                        prayer.timeString,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isNext) DarkColors.Gold else DarkColors.TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
