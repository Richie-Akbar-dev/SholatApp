package com.sholatapp.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.data.MahfudzotData
import com.sholatapp.model.PrayerInfo
import com.sholatapp.ui.theme.DarkColors
import com.sholatapp.viewmodel.PrayerViewModel
import com.sholatapp.viewmodel.UiState
import java.util.Calendar

/**
 * Light theme colors for Home Screen (Namaz Vakti style)
 */
private object HomeLightColors {
    val Background = Color(0xFFF5F5F0)
    val Surface = Color(0xFFFFFFFF)
    val HeroGreen = Color(0xFF1B4D3E)
    val HeroGreenDark = Color(0xFF143A2E)
    val Gold = Color(0xFFD4AF37)
    val GoldLight = Color(0xFFF0E6C8)
    val TextPrimary = Color(0xFF1A1A1A)
    val TextSecondary = Color(0xFF6B7280)
    val TextTertiary = Color(0xFF9CA3AF)
    val TextOnHero = Color(0xFFFFFFFF)
    val TextGold = Color(0xFFB8941F)
    val Divider = Color(0xFFE5E7EB)
    val CardBorder = Color(0xFFF0F0EC)
    val ActiveIndicator = Color(0xFF1B4D3E)
    val PassedText = Color(0xFF9CA3AF)
    val CheckGreen = Color(0xFF22C55E)
}

@Composable
fun HomeScreen(
    viewModel: PrayerViewModel,
    userName: String,
    onKiblatClick: () -> Unit,
    onTasbihClick: () -> Unit,
    onPuasaClick: () -> Unit,
    onKalenderClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSalatClick: () -> Unit,
    onLainnyaClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val mahfudzot = remember { MahfudzotData.getTodayMahfudzot() }
    val schedule = uiState.prayerSchedule
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeLightColors.Background)
    ) {
        if (uiState.isLoading && schedule == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = HomeLightColors.HeroGreen)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Header: Location + Date
                HomeHeaderSection(
                    locationAddress = uiState.locationAddress,
                    isLoading = uiState.isLoading,
                    onRefreshLocation = { viewModel.detectLocation() },
                    onNotificationClick = onNotificationClick,
                    onSettingsClick = onSettingsClick
                )

                // Greeting + Mahfudzot
                GreetingSection(userName = userName, mahfudzot = mahfudzot)

                // Hero Card: Next Prayer + Countdown
                if (schedule != null) {
                    NextPrayerHeroCard(
                        schedule = schedule,
                        uiState = uiState
                    )
                }

                // Prayer Times Grid (2 columns)
                if (schedule != null) {
                    PrayerTimesGrid(
                        schedule = schedule,
                        checkedPrayers = uiState.checkedPrayers,
                        onSalatClick = onSalatClick
                    )
                }

                // Quick Actions
                QuickActionsSection(
                    onKiblatClick = onKiblatClick,
                    onTasbihClick = onTasbihClick,
                    onPuasaClick = onPuasaClick,
                    onKalenderClick = onKalenderClick,
                    onLainnyaClick = onLainnyaClick
                )

                // Prayer Tracking List
                if (schedule != null) {
                    PrayerTrackingSection(
                        schedule = schedule,
                        checkedPrayers = uiState.checkedPrayers,
                        onCheckPrayer = { viewModel.checkPrayer(it) }
                    )
                }

                // Error message
                if (uiState.errorMessage != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFEE2E2)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                uiState.errorMessage!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF991B1B)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ==================== HEADER SECTION ====================
@Composable
private fun HomeHeaderSection(
    locationAddress: String,
    isLoading: Boolean,
    onRefreshLocation: () -> Unit,
    onNotificationClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val today = Calendar.getInstance()
    val dayNames = arrayOf("Ahad", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
    val monthNames = arrayOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )
    val dateStr = "${dayNames[today.get(Calendar.DAY_OF_WEEK) - 1]}, ${today.get(Calendar.DAY_OF_MONTH)} ${monthNames[today.get(Calendar.MONTH)]} ${today.get(Calendar.YEAR)}"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(HomeLightColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = HomeLightColors.HeroGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = locationAddress,
                style = MaterialTheme.typography.labelMedium,
                color = HomeLightColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            // Ikon kanan atas: notifikasi + pengaturan
            Row {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notifikasi",
                        tint = HomeLightColors.TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Pengaturan",
                        tint = HomeLightColors.TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = dateStr,
            style = MaterialTheme.typography.bodySmall,
            color = HomeLightColors.TextTertiary
        )
    }
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = HomeLightColors.Divider
    )
}

// ==================== GREETING + MAHFUDZOT ====================
@Composable
private fun GreetingSection(
    userName: String,
    mahfudzot: com.sholatapp.data.Mahfudzot
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Greeting
        Text(
            text = "Assalamualaikum,",
            style = MaterialTheme.typography.bodyMedium,
            color = HomeLightColors.TextSecondary
        )
        Text(
            text = if (userName.isNotBlank()) userName else "Muslim",
            style = MaterialTheme.typography.headlineMedium,
            color = HomeLightColors.TextPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Mahfudzot card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = HomeLightColors.GoldLight
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Text(
                    text = mahfudzot.arabic,
                    style = MaterialTheme.typography.bodyLarge,
                    color = HomeLightColors.TextGold,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = mahfudzot.meaning,
                    style = MaterialTheme.typography.bodySmall,
                    color = HomeLightColors.TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ==================== HERO CARD: NEXT PRAYER + COUNTDOWN ====================
@Composable
private fun NextPrayerHeroCard(
    schedule: com.sholatapp.model.PrayerSchedule,
    uiState: UiState
) {
    val cal = Calendar.getInstance()
    val nextPrayer = schedule.getNextPrayer(
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE)
    )
    val countdown = uiState.countdownSeconds

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = HomeLightColors.HeroGreen
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "Sholat berikutnya" label
            Text(
                text = "Sholat Berikutnya",
                style = MaterialTheme.typography.labelLarge,
                color = HomeLightColors.TextOnHero.copy(alpha = 0.8f),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Next prayer name
            Text(
                text = nextPrayer?.name ?: "--",
                style = MaterialTheme.typography.headlineLarge,
                color = HomeLightColors.TextOnHero,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Prayer time
            Text(
                text = nextPrayer?.timeString ?: "--:--",
                style = MaterialTheme.typography.titleMedium,
                color = HomeLightColors.Gold,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Countdown
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CountdownUnit(value = countdown / 3600)
                Text(
                    text = ":",
                    style = MaterialTheme.typography.headlineMedium,
                    color = HomeLightColors.TextOnHero.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold
                )
                CountdownUnit(value = (countdown % 3600) / 60)
                Text(
                    text = ":",
                    style = MaterialTheme.typography.headlineMedium,
                    color = HomeLightColors.TextOnHero.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold
                )
                CountdownUnit(value = countdown % 60)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "menuju adzan",
                style = MaterialTheme.typography.labelSmall,
                color = HomeLightColors.TextOnHero.copy(alpha = 0.6f)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun CountdownUnit(value: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = HomeLightColors.HeroGreenDark
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = String.format("%02d", value),
                style = MaterialTheme.typography.headlineMedium,
                color = HomeLightColors.TextOnHero,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ==================== PRAYER TIMES GRID (2 COLUMNS) ====================
@Composable
private fun PrayerTimesGrid(
    schedule: com.sholatapp.model.PrayerSchedule,
    checkedPrayers: Set<String>,
    onSalatClick: () -> Unit
) {
    val cal = Calendar.getInstance()
    val currentSeconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60
    val nextPrayer = schedule.getNextPrayer(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onSalatClick() }
    ) {
        Text(
            text = "Jadwal Sholat Hari Ini",
            style = MaterialTheme.typography.titleMedium,
            color = HomeLightColors.TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))

        // 2-column grid: 3 rows for 6 times (Subuh+Terbit, Dzuhur+Ashar, Maghrib+Isya)
        val allTimes = schedule.allTimes

        // Row 1: Subuh + Syuruq
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrayerTimeCard(
                prayer = allTimes[0], // Fajr
                isNext = nextPrayer?.nameKey == allTimes[0].nameKey,
                isPassed = allTimes[0].totalSeconds <= currentSeconds,
                isChecked = allTimes[0].nameKey in checkedPrayers,
                imsakTime = schedule.imsak?.timeString,
                modifier = Modifier.weight(1f)
            )
            PrayerTimeCard(
                prayer = allTimes[1], // Sunrise
                isNext = false,
                isPassed = allTimes[1].totalSeconds <= currentSeconds,
                isChecked = false,
                isSunrise = true,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Row 2: Dzuhur + Ashar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrayerTimeCard(
                prayer = allTimes[2], // Dhuhr
                isNext = nextPrayer?.nameKey == allTimes[2].nameKey,
                isPassed = allTimes[2].totalSeconds <= currentSeconds,
                isChecked = allTimes[2].nameKey in checkedPrayers,
                modifier = Modifier.weight(1f)
            )
            PrayerTimeCard(
                prayer = allTimes[3], // Asr
                isNext = nextPrayer?.nameKey == allTimes[3].nameKey,
                isPassed = allTimes[3].totalSeconds <= currentSeconds,
                isChecked = allTimes[3].nameKey in checkedPrayers,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Row 3: Maghrib + Isya
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrayerTimeCard(
                prayer = allTimes[4], // Maghrib
                isNext = nextPrayer?.nameKey == allTimes[4].nameKey,
                isPassed = allTimes[4].totalSeconds <= currentSeconds,
                isChecked = allTimes[4].nameKey in checkedPrayers,
                modifier = Modifier.weight(1f)
            )
            PrayerTimeCard(
                prayer = allTimes[5], // Isha
                isNext = nextPrayer?.nameKey == allTimes[5].nameKey,
                isPassed = allTimes[5].totalSeconds <= currentSeconds,
                isChecked = allTimes[5].nameKey in checkedPrayers,
                modifier = Modifier.weight(1f)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun PrayerTimeCard(
    prayer: PrayerInfo,
    isNext: Boolean,
    isPassed: Boolean,
    isChecked: Boolean,
    isSunrise: Boolean = false,
    imsakTime: String? = null,
    modifier: Modifier = Modifier
) {
    val bgColor = when {
        isChecked -> Color(0xFFDCFCE7)
        isNext -> HomeLightColors.HeroGreen
        else -> HomeLightColors.Surface
    }
    val nameColor = when {
        isChecked -> Color(0xFF166534)
        isNext -> HomeLightColors.TextOnHero
        isPassed -> HomeLightColors.PassedText
        else -> HomeLightColors.TextPrimary
    }
    val timeColor = when {
        isChecked -> Color(0xFF166534)
        isNext -> HomeLightColors.Gold
        isPassed -> HomeLightColors.PassedText
        else -> HomeLightColors.TextSecondary
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(16.dp),
        elevation = if (isNext) CardDefaults.cardElevation(2.dp) else CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSunrise) {
                    Icon(
                        Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = if (isPassed) HomeLightColors.PassedText else Color(0xFFF59E0B),
                        modifier = Modifier.size(16.dp)
                    )
                } else if (isChecked) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF22C55E),
                        modifier = Modifier.size(16.dp)
                    )
                } else if (isNext) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(HomeLightColors.Gold)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = prayer.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = nameColor,
                    fontWeight = if (isNext) FontWeight.Bold else FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = prayer.timeString,
                style = MaterialTheme.typography.titleLarge,
                color = timeColor,
                fontWeight = FontWeight.Bold
            )
            if (imsakTime != null) {
                Text(
                    text = "Imsak $imsakTime",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isPassed) HomeLightColors.PassedText else HomeLightColors.TextSecondary
                )
            }
        }
    }
}

// ==================== QUICK ACTIONS ====================
@Composable
private fun QuickActionsSection(
    onKiblatClick: () -> Unit,
    onTasbihClick: () -> Unit,
    onPuasaClick: () -> Unit,
    onKalenderClick: () -> Unit,
    onLainnyaClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Tindakan Cepat",
            style = MaterialTheme.typography.titleMedium,
            color = HomeLightColors.TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
        QuickActionItem(
                icon = Icons.Default.Explore,
                label = "Kiblat",
                onClick = onKiblatClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.TouchApp,
                label = "Tasbih",
                onClick = onTasbihClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.Brightness3,
                label = "Puasa",
                onClick = onPuasaClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.CalendarMonth,
                label = "Kalender",
                onClick = onKalenderClick,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Menu Lainnya: Mushaf, Doa, Mutabaah, Tilawah, Asmaul Husna
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onLainnyaClick),
            colors = CardDefaults.cardColors(containerColor = HomeLightColors.Surface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(HomeLightColors.GoldLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Apps,
                        contentDescription = "Lainnya",
                        tint = HomeLightColors.HeroGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Lainnya",
                        style = MaterialTheme.typography.labelMedium,
                        color = HomeLightColors.TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Mushaf, Doa, Mutabaah, Tilawah, Asmaul Husna",
                        style = MaterialTheme.typography.labelSmall,
                        color = HomeLightColors.TextSecondary,
                        maxLines = 1
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = HomeLightColors.TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = HomeLightColors.Surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(HomeLightColors.GoldLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = HomeLightColors.HeroGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = HomeLightColors.TextPrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ==================== PRAYER TRACKING LIST ====================
@Composable
private fun PrayerTrackingSection(
    schedule: com.sholatapp.model.PrayerSchedule,
    checkedPrayers: Set<String>,
    onCheckPrayer: (String) -> Unit
) {
    val cal = Calendar.getInstance()
    val currentSeconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60
    val prayers = schedule.prayerList
    val checkedCount = prayers.count { it.nameKey in checkedPrayers }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Section header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pelacakan Sholat",
                style = MaterialTheme.typography.titleMedium,
                color = HomeLightColors.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$checkedCount/${prayers.size}",
                style = MaterialTheme.typography.labelLarge,
                color = HomeLightColors.HeroGreen,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Progress bar
        val progress = if (prayers.isNotEmpty()) checkedCount.toFloat() / prayers.size else 0f
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .height(6.dp),
            color = HomeLightColors.HeroGreen,
            trackColor = HomeLightColors.Divider,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Prayer list
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = HomeLightColors.Surface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            prayers.forEachIndexed { index, prayer ->
                val isChecked = prayer.nameKey in checkedPrayers
                val isPassed = prayer.totalSeconds <= currentSeconds && !isChecked

                PrayerTrackingItem(
                    prayer = prayer,
                    isChecked = isChecked,
                    isLast = index == prayers.lastIndex,
                    onCheck = { onCheckPrayer(prayer.nameKey) }
                )
            }
        }
    }
}

@Composable
private fun PrayerTrackingItem(
    prayer: PrayerInfo,
    isChecked: Boolean,
    isLast: Boolean,
    onCheck: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheck() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (isChecked) HomeLightColors.CheckGreen
                        else Color.Transparent
                    )
                    .border(
                        width = 2.dp,
                        color = if (isChecked) HomeLightColors.CheckGreen
                        else HomeLightColors.Divider,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Prayer name
            Text(
                text = prayer.name,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isChecked) HomeLightColors.PassedText else HomeLightColors.TextPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            // Time
            Text(
                text = prayer.timeString,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isChecked) HomeLightColors.PassedText else HomeLightColors.TextSecondary
            )
        }

        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 40.dp),
                color = HomeLightColors.Divider,
                thickness = 1.dp
            )
        }
    }
}
