package com.sholatapp.ui.screens

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sholatapp.azan.AzanPlayer
import com.sholatapp.data.TilawahData
import com.sholatapp.dnd.DndHelper
import com.sholatapp.ui.theme.DarkColors
import com.sholatapp.viewmodel.UiState
import java.util.Locale
import kotlin.math.ceil

/** Warna aksi destruktif (konsisten dgn mockup v2.8) */
private val DangerRed = Color(0xFFB3261E)
private val DangerTint = Color(0xFFFCE8E6)
private val GoldBadgeBg = Color(0xFFF7EED0)
private val ChipBadBg = Color(0xFFFCE8E6)

/**
 * Halaman Pengaturan v2.8.0 — rombak besar sesuai mockup:
 * kartu profil + ubah nama, status perizinan Android, perbarui lokasi,
 * grid metode perhitungan, khatam dengan progress bar + konfirmasi reset,
 * riwayat versi, dan footer.
 *
 * Fix dari review v2.8:
 * F1 Suara Azan kini refresh saat kembali dari pemilih azan (key azanVersion)
 * F2 Progress khatam refresh setelah simpan target / mulai ulang (key khatamRefresh)
 * F3 Kedua aksi reset kini lewat dialog konfirmasi
 * F4 Simpan target memberi feedback Toast
 * F5 Koordinat memakai Locale.US (titik desimal konsisten)
 * F6 Status izin notifikasi / alarm tepat / DND terlihat & bisa diperbaiki dari sini
 */
@Composable
fun SettingsScreen(
    uiState: UiState,
    onToggleAlarm: (Boolean) -> Unit,
    onTogglePrepAlarm: (Boolean) -> Unit,
    onToggleDnd: (Boolean) -> Unit,
    context: Context? = null,
    onResetDzikir: (() -> Unit)? = null,
    onChangeAzan: (() -> Unit)? = null,
    userName: String = "",
    onNameChange: ((String) -> Unit)? = null,
    onPusatNotifikasiClick: (() -> Unit)? = null,
    onRefreshLocation: (() -> Unit)? = null,
    azanVersion: Int = 0
) {
    var showEditName by remember { mutableStateOf(false) }
    var showResetKhatamConfirm by remember { mutableStateOf(false) }
    var showResetDzikirConfirm by remember { mutableStateOf(false) }
    var showChangelog by remember { mutableStateOf(false) }
    var khatamRefresh by remember { mutableIntStateOf(0) }

    val showToast: (String) -> Unit = { msg ->
        context?.let {
            android.widget.Toast.makeText(it, msg, android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColors.Background)
    ) {
        // ===== Header hijau tua (gaya tema hybrid v2.4-v2.8) =====
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(DarkColors.PrimaryDark)
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 52.dp)
        ) {
            Text(
                text = "Pengaturan",
                style = MaterialTheme.typography.headlineSmall,
                color = DarkColors.TextOnPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Atur aplikasi sesuai kebutuhan ibadahmu",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkColors.HeaderSubtitle
            )
        }

        // ===== Konten scroll =====
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Kartu profil — overlap header (offset naik 32dp, ruang flow tetap)
            ProfileCard(
                name = userName,
                location = uiState.locationAddress,
                onClick = { showEditName = true },
                modifier = Modifier.offset(y = (-32).dp)
            )

            // ===== ALARM & NOTIFIKASI =====
            SectionHeader(title = "Alarm & Notifikasi")
            SettingToggleRow(
                icon = Icons.Default.Notifications,
                title = "Alarm Pengingat Sholat",
                subtitle = "Notifikasi dan suara azan saat waktu sholat",
                checked = uiState.isAlarmEnabled,
                onCheckedChange = onToggleAlarm
            )
            SettingToggleRow(
                icon = Icons.Default.Alarm,
                title = "Alarm Bangunkan (20 menit sebelum)",
                subtitle = "Notifikasi 20 menit sebelum adzan untuk persiapan",
                checked = uiState.isPrepAlarmEnabled,
                onCheckedChange = onTogglePrepAlarm
            )
            SettingToggleRow(
                icon = Icons.Default.DoNotDisturbOn,
                title = "Mode Fokus saat Sholat",
                subtitle = "Aktifkan DND otomatis saat adzan selama 30 menit",
                checked = uiState.isDndEnabled,
                onCheckedChange = onToggleDnd
            )
            if (context != null) {
                PermissionStatusCard(context = context, dndEnabled = uiState.isDndEnabled)
            }
            SettingActionRow(
                icon = Icons.Default.Tune,
                title = "Pusat Notifikasi",
                subtitle = "Status alarm & pengingat ibadah",
                onClick = { onPusatNotifikasiClick?.invoke() },
                showChevron = true
            )
            AzanSettingCard(
                context = context,
                onChangeAzan = { onChangeAzan?.invoke() },
                azanVersion = azanVersion
            )

            // ===== LOKASI & METODE =====
            SectionHeader(title = "Lokasi & Metode")
            SettingInfoActionRow(
                icon = Icons.Default.LocationOn,
                title = "Lokasi Saat Ini",
                value = uiState.locationAddress,
                actionLabel = "Perbarui",
                onAction = {
                    showToast("Memperbarui lokasi...")
                    onRefreshLocation?.invoke()
                }
            )
            SettingInfoRow(
                icon = Icons.Default.Public,
                title = "Koordinat",
                value = if (uiState.latitude != 0.0 || uiState.longitude != 0.0)
                    String.format(Locale.US, "%.4f, %.4f", uiState.latitude, uiState.longitude)
                else "Belum tersedia"
            )
            MethodCard()

            // ===== BACAAN TERARAH (KHATAM) =====
            SectionHeader(title = "Bacaan Terarah (Khatam)")
            KhatamTargetCard(
                context = context,
                refreshKey = khatamRefresh,
                onSaved = {
                    khatamRefresh++
                    showToast("Target khatam disimpan")
                }
            )
            SettingDangerRow(
                icon = Icons.Default.RestartAlt,
                title = "Mulai Ulang Rencana",
                subtitle = "Kembali ke awal dan hapus riwayat khatam",
                onClick = { showResetKhatamConfirm = true }
            )

            // ===== DATA =====
            SectionHeader(title = "Data")
            SettingResetButtonRow(
                icon = Icons.Default.Refresh,
                title = "Reset Progress Dzikir",
                subtitle = "Hapus semua progress dzikir hari ini",
                buttonLabel = "Reset",
                onClick = { showResetDzikirConfirm = true }
            )

            // ===== TENTANG APLIKASI =====
            SectionHeader(title = "Tentang Aplikasi")
            SettingInfoRow(
                icon = Icons.Default.Info,
                title = "Versi",
                value = "v2.10.1",
                valueAsChip = true
            )
            SettingInfoRow(
                icon = Icons.Default.VerifiedUser,
                title = "100% Offline",
                value = "Data tidak meninggalkan perangkat Anda demi privasi ibadah maksimal."
            )
            SettingInfoRow(
                icon = Icons.Default.AccountBalance,
                title = "Sumber Waktu",
                value = "KEMENAG RI"
            )
            SettingActionRow(
                icon = Icons.Default.History,
                title = "Riwayat Versi",
                subtitle = "Perubahan pada setiap pembaruan aplikasi",
                onClick = { showChangelog = true },
                showChevron = true
            )

            // Footer
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "SholatApp v2.10.1 · Dibuat dengan cinta untuk umat",
                style = MaterialTheme.typography.labelSmall,
                color = DarkColors.TextTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
    }

    // ===== Dialog ubah nama =====
    if (showEditName) {
        var nameInput by remember { mutableStateOf(userName) }
        AlertDialog(
            onDismissRequest = { showEditName = false },
            containerColor = DarkColors.Surface,
            title = {
                Text(
                    "Ubah Nama",
                    fontWeight = FontWeight.Bold,
                    color = DarkColors.TextPrimary
                )
            },
            text = {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { if (it.length <= 24) nameInput = it },
                    label = { Text("Nama panggilan", color = DarkColors.TextTertiary) },
                    placeholder = { Text("Contoh: Rizky", color = DarkColors.TextTertiary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkColors.Gold,
                        unfocusedBorderColor = DarkColors.Border,
                        cursorColor = DarkColors.Gold,
                        focusedTextColor = DarkColors.TextPrimary,
                        unfocusedTextColor = DarkColors.TextPrimary
                    )
                )
            },
            confirmButton = {
                TextButton(
                    enabled = nameInput.trim().isNotEmpty(),
                    onClick = {
                        onNameChange?.invoke(nameInput.trim())
                        showEditName = false
                        showToast("Nama diperbarui")
                    }
                ) {
                    Text(
                        "Simpan",
                        color = if (nameInput.trim().isNotEmpty()) DarkColors.Primary else DarkColors.TextTertiary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditName = false }) {
                    Text("Batal", color = DarkColors.TextTertiary)
                }
            }
        )
    }

    // ===== Dialog konfirmasi mulai ulang khatam =====
    if (showResetKhatamConfirm) {
        AlertDialog(
            onDismissRequest = { showResetKhatamConfirm = false },
            containerColor = DarkColors.Surface,
            title = {
                Text(
                    "Mulai Ulang Rencana?",
                    fontWeight = FontWeight.Bold,
                    color = DarkColors.TextPrimary
                )
            },
            text = {
                Text(
                    "Riwayat khatam akan dihapus dan rencana dimulai kembali " +
                        "dari QS. Al-Fatihah. Tindakan ini tidak bisa dibatalkan.",
                    color = DarkColors.TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        context?.let { TilawahData.resetProgress(it) }
                        khatamRefresh++
                        showResetKhatamConfirm = false
                        showToast("Rencana khatam direset")
                    }
                ) {
                    Text("Ya, Hapus", color = DangerRed, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetKhatamConfirm = false }) {
                    Text("Batal", color = DarkColors.TextTertiary)
                }
            }
        )
    }

    // ===== Dialog konfirmasi reset dzikir =====
    if (showResetDzikirConfirm) {
        AlertDialog(
            onDismissRequest = { showResetDzikirConfirm = false },
            containerColor = DarkColors.Surface,
            title = {
                Text(
                    "Reset Progress Dzikir?",
                    fontWeight = FontWeight.Bold,
                    color = DarkColors.TextPrimary
                )
            },
            text = {
                Text(
                    "Semua progress dzikir hari ini akan dihapus.",
                    color = DarkColors.TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onResetDzikir?.invoke()
                        showResetDzikirConfirm = false
                    }
                ) {
                    Text("Ya, Reset", color = DangerRed, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDzikirConfirm = false }) {
                    Text("Batal", color = DarkColors.TextTertiary)
                }
            }
        )
    }

    // ===== Pop up riwayat versi =====
    if (showChangelog) {
        AlertDialog(
            onDismissRequest = { showChangelog = false },
            containerColor = DarkColors.Surface,
            title = {
                Text(
                    "Riwayat Versi",
                    fontWeight = FontWeight.Bold,
                    color = DarkColors.TextPrimary
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    changelogEntries.forEach { entry ->
                        Row {
                            Text(
                                text = entry.first,
                                style = MaterialTheme.typography.labelMedium,
                                color = DarkColors.Gold,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.width(52.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = entry.second,
                                style = MaterialTheme.typography.bodySmall,
                                color = DarkColors.TextSecondary
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showChangelog = false }) {
                    Text("Tutup", color = DarkColors.Primary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}

/** Entri ringkas riwayat versi (terbaru di atas). */
private val changelogEntries: List<Pair<String, String>> = listOf(
    "v2.10.1" to "Fix kritis waktu sholat meleset hingga 2,5 jam (Subuh/Isya/Ashar): mesin astronomi baru presisi Meeus, parameter KEMENAG, tervalidasi 5 kota"
    "v2.10.0" to "Dua halaman Qur'an: Al-Qur'an tampilan baru + Mushaf mengalir gaya asli; pintu Lainnya dihapus — Mutabaah naik ke Beranda, Doa & Asmaul Husna jadi kategori Zikir",
    "v2.9.0" to "Kiblat baru: kompas akurat + mode terkunci + getar; fix kritis kalender Hijriah meleset 26 tahun",
    "v2.8.0" to "Pengaturan baru: profil & ubah nama, status perizinan, perbarui lokasi, konfirmasi reset",
    "v2.7.0" to "Puasa: countdown langsung, imsak akurat, kalender tap-catat, pengingat sunnah",
    "v2.6.0" to "Mushaf Bacaan Terarah: lembar Arab menerus + setelan khatam",
    "v2.5.0" to "Zikir: chip kategori, undo 3 detik, pop up fokus, 29 konten",
    "v2.4.0" to "Salat: jam analog 5 jarum, detail waktu, error state, latar foto lokal",
    "v2.3.0" to "Beranda baru + Al-Qur'an 114 surah offline lengkap",
    "v2.2.0" to "Tema terang konsisten + navigasi lebih mulus",
    "v2.1.0" to "Update menyeluruh pertama: fitur baru, bugfix, tema"
)

/**
 * Tick yang bertambah setiap kali Activity kembali ON_RESUME.
 * Dipakai agar status perizinan (yang dicek dari pengaturan sistem)
 * selalu segar saat pengguna kembali ke aplikasi.
 */
@Composable
private fun rememberResumeTick(): Int {
    val owner = LocalLifecycleOwner.current
    var tick by remember { mutableIntStateOf(0) }
    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) tick++
        }
        owner.lifecycle.addObserver(observer)
        onDispose { owner.lifecycle.removeObserver(observer) }
    }
    return tick
}

private fun exactAlarmAllowed(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.canScheduleExactAlarms()
    } else {
        true
    }
}

private fun openAppNotificationSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        // Pengaturan tidak tersedia di perangkat ini — abaikan
    }
}

private fun openExactAlarmSettings(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        try {
            val intent = Intent(
                Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                Uri.parse("package:${context.packageName}")
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            // Pengaturan tidak tersedia di perangkat ini — abaikan
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

/** Ikon dalam kontainer hijau muda 40dp (gaya Setelan Google). */
@Composable
private fun LeadingIconBox(
    icon: ImageVector,
    tint: Color = DarkColors.Primary,
    container: Color = DarkColors.PrimaryContainer
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(container),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
    }
}

/** Kartu profil: avatar inisial + nama + lokasi · metode + ikon ubah (pensil). */
@Composable
private fun ProfileCard(
    name: String,
    location: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initial = name.trim().take(1).uppercase().ifEmpty { "S" }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(DarkColors.Primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                style = MaterialTheme.typography.titleMedium,
                color = DarkColors.TextOnPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name.ifBlank { "Sahabat SholatApp" },
                style = MaterialTheme.typography.bodyLarge,
                color = DarkColors.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$location · Metode KEMENAG RI",
                style = MaterialTheme.typography.bodySmall,
                color = DarkColors.TextTertiary,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Ubah nama",
            tint = DarkColors.Gold,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun SettingToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIconBox(icon = icon)
        Spacer(modifier = Modifier.width(12.dp))
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

/** Kartu Status Perizinan Android: chip izin notifikasi, alarm tepat, dan DND. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PermissionStatusCard(context: Context, dndEnabled: Boolean) {
    // resumeTick dibaca agar kartu refresh saat kembali dari pengaturan sistem
    val resumeTick = rememberResumeTick()
    val notifGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
    val exactOk = exactAlarmAllowed(context)
    val dndOk = DndHelper.hasDndPermission(context)
    val anyMissing = !notifGranted || !exactOk || !dndOk

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LeadingIconBox(
                icon = Icons.Default.Security,
                tint = if (anyMissing) DarkColors.Gold else DarkColors.Primary,
                container = if (anyMissing) GoldBadgeBg else DarkColors.PrimaryContainer
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Status Perizinan Android",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    if (anyMissing) "Ketuk chip merah untuk memperbaiki izin"
                    else "Semua izin penting sudah diberikan",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkColors.TextTertiary
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PermissionChip(
                label = if (notifGranted) "Izin notifikasi aktif" else "Izin notifikasi belum ada",
                ok = notifGranted
            ) { openAppNotificationSettings(context) }
            PermissionChip(
                label = if (exactOk) "Alarm tepat diizinkan" else "Alarm tepat belum diizinkan",
                ok = exactOk
            ) { openExactAlarmSettings(context) }
            PermissionChip(
                label = if (dndOk) "Izin DND ok" else "Izin DND belum ada",
                ok = dndOk
            ) { DndHelper.requestDndPermission(context) }
        }
        // resumeTick hanya trigger — nilai tak ditampilkan langsung
        @Suppress("UNUSED_EXPRESSION")
        resumeTick
    }
}

@Composable
private fun PermissionChip(label: String, ok: Boolean, onFix: () -> Unit) {
    val bg = if (ok) DarkColors.PrimaryContainer else ChipBadBg
    val fg = if (ok) DarkColors.Primary else DangerRed
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .clickable(enabled = !ok) { onFix() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (ok) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
            contentDescription = null,
            tint = fg,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = fg,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/** Baris menu dengan aksi (ikon + judul + subtitle + chevron). */
@Composable
private fun SettingActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    showChevron: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIconBox(icon = icon)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = DarkColors.TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DarkColors.TextTertiary)
        }
        if (showChevron) {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = DarkColors.TextTertiary
            )
        }
    }
}

/** Baris aksi destruktif (merah) — dipakai Mulai Ulang Rencana. */
@Composable
private fun SettingDangerRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIconBox(icon = icon, tint = DangerRed, container = DangerTint)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                color = DangerRed,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DarkColors.TextTertiary)
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = DarkColors.TextTertiary
        )
    }
}

/** Baris info dengan tombol kecil di kanan — dipakai Lokasi + Perbarui. */
@Composable
private fun SettingInfoActionRow(
    icon: ImageVector,
    title: String,
    value: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIconBox(icon = icon)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = DarkColors.TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                value,
                style = MaterialTheme.typography.bodySmall,
                color = DarkColors.TextSecondary,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedButton(
            onClick = onAction,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, DarkColors.Primary),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkColors.Primary),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text(actionLabel, style = MaterialTheme.typography.labelMedium)
        }
    }
}

/** Baris info dengan tombol reset merah kecil — dipakai Reset Progress Dzikir. */
@Composable
private fun SettingResetButtonRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    buttonLabel: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIconBox(icon = icon)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = DarkColors.TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DarkColors.TextTertiary)
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, DangerRed),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text(buttonLabel, style = MaterialTheme.typography.labelMedium)
        }
    }
}

/** Kartu Suara Azan — nilai selalu segar berkat key azanVersion (fix F1). */
@Composable
private fun AzanSettingCard(
    context: Context?,
    onChangeAzan: () -> Unit,
    azanVersion: Int
) {
    val azanPlayer = remember { context?.let { AzanPlayer(it) } }
    val currentAzan = remember(azanVersion) { azanPlayer?.getSelectedAzanOption() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .clickable { onChangeAzan() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIconBox(icon = Icons.Default.NotificationsActive)
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

/** Baris informasi statis dengan ikon (opsional nilai berbentuk chip). */
@Composable
private fun SettingInfoRow(
    icon: ImageVector,
    title: String,
    value: String,
    valueAsChip: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIconBox(icon = icon)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = DarkColors.TextPrimary)
            if (!valueAsChip) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(value, style = MaterialTheme.typography.bodySmall, color = DarkColors.TextSecondary)
            }
        }
        if (valueAsChip) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkColors.SurfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelMedium,
                    color = DarkColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/** Kartu grid 2x2 metode perhitungan (KEMENAG). */
@Composable
private fun MethodCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LeadingIconBox(icon = Icons.Default.Calculate)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Metode Perhitungan",
                style = MaterialTheme.typography.bodyLarge,
                color = DarkColors.TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MethodCell(modifier = Modifier.weight(1f), label = "OTORITAS", value = "KEMENAG RI")
            MethodCell(modifier = Modifier.weight(1f), label = "FAJR", value = "Sudut Fajr 20°")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MethodCell(modifier = Modifier.weight(1f), label = "ISYA", value = "Sudut Isya 18°")
            MethodCell(modifier = Modifier.weight(1f), label = "MASZHAB ASHAR", value = "Syafi'i (1x)")
        }
    }
}

@Composable
private fun MethodCell(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkColors.Background)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = DarkColors.TextTertiary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = DarkColors.TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/** Kartu target khatam: badge %, progress bar emas, preset, custom, simpan (fix F2 & F4). */
@Composable
private fun KhatamTargetCard(
    context: Context?,
    refreshKey: Int,
    onSaved: () -> Unit
) {
    val prefs = remember { context?.getSharedPreferences("tilawah_prefs", Context.MODE_PRIVATE) }
    val savedTarget = remember(refreshKey) { prefs?.getInt("target_days", 365) ?: 365 }
    var targetDays by remember(savedTarget) { mutableIntStateOf(savedTarget) }
    val versesPerDay = remember(targetDays) { ceil(6236.0 / targetDays).toInt() }
    val progress = remember(refreshKey) { context?.let { TilawahData.getProgress(it) } }
    val percent = (progress?.completionPercent ?: 0f).coerceIn(0f, 100f)
    val versesRead = (progress?.totalReadDays ?: 0) * versesPerDay

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkColors.Surface)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Target Khatam",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "$targetDays hari · $versesPerDay ayat/hari",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkColors.TextSecondary
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(GoldBadgeBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${percent.toInt()}% Selesai",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkColors.GoldDark,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Progress bar emas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(DarkColors.Border)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent / 100f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(DarkColors.Gold)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "$versesRead dari 6.236 ayat tercapai",
            style = MaterialTheme.typography.labelSmall,
            color = DarkColors.TextTertiary
        )

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Pilih Durasi Target:",
            style = MaterialTheme.typography.labelMedium,
            color = DarkColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PresetButton(
                label = if (targetDays == 30) "30 hari ✓" else "30 hari",
                isActive = targetDays == 30,
                onClick = { targetDays = 30 }
            )
            PresetButton(
                label = if (targetDays == 180) "6 bulan ✓" else "6 bulan",
                isActive = targetDays == 180,
                onClick = { targetDays = 180 }
            )
            PresetButton(
                label = if (targetDays == 365) "1 tahun ✓" else "1 tahun",
                isActive = targetDays == 365,
                onClick = { targetDays = 365 }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = if (targetDays == 0) "" else "$targetDays",
            onValueChange = { input ->
                val num = input.toIntOrNull()
                if (num != null && num in 1..3650) targetDays = num
            },
            label = { Text("Custom Durasi (hari)", color = DarkColors.TextTertiary) },
            placeholder = { Text("Contoh: 100 hari", color = DarkColors.TextTertiary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(color = DarkColors.TextPrimary),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkColors.Gold,
                unfocusedBorderColor = DarkColors.Border,
                cursorColor = DarkColors.Gold
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Simpan target: simpan target HARI + hitung ulang ayat/hari (integrasi v2.6)
        Button(
            onClick = {
                val ctx = context ?: return@Button
                prefs?.edit()?.putInt("target_days", targetDays)?.apply()
                TilawahData.setTargetDays(ctx, targetDays)
                TilawahData.setVersesPerDay(ctx, ceil(6236.0 / targetDays).toInt())
                onSaved()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkColors.PrimaryDark,
                contentColor = DarkColors.TextOnPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
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
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        modifier = Modifier.weight(1f)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, maxLines = 1)
    }
}
