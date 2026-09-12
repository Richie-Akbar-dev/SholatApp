package com.sholatapp.ui.screens

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.view.Surface
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.calculation.PrayerCalculator
import java.util.Locale
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Halaman Kiblat — v2.9 (rombak penuh mengikuti bahasa visual v2.4-v2.8).
 *
 * Perbaikan SISTEM:
 * A1 Fix bug orientasi 90 derajat — semua elemen dial (tick, label, jarum)
 *    kini memakai rumus sudut yang sama: (bearing - 90 - azimuth)
 * A2 Guard lokasi: tanpa koordinat valid, tampil kartu "Menunggu Lokasi"
 *    (tidak lagi menghitung diam-diam dari koordinat 0,0)
 * A3 Deteksi sensor kompas tidak tersedia -> pesan jujur di halaman
 * A4 Chip kalibrasi saat akurasi magnetometer rendah
 * A5 Mode "Terkunci": selisih <= 4 derajat -> getar + banner hijau
 * A6 Sumber azimuth TYPE_ROTATION_VECTOR (fallback accel+mag) +
 *    remapCoordinateSystem sesuai orientasi layar
 * A7 Format angka Locale Indonesia + buang variabel mati
 *
 * Perbaikan TAMPILAN:
 * B1 Header hijau 143A2E rounded bawah (pola v2.4-v2.8), latar terang F5F5F0
 * B2 Kompas dalam kartu putih radius 24 + cincin emas + label Indonesia
 * B3 Kartu info gaya baru dengan kontainer ikon E8F0EA (pola aplikasi)
 * B4 Banner status dinamis (cari -> terkunci hijau)
 * B5 Kartu instruksi bergaya baru
 * B6 Chip lokasi di bawah header
 * B7 State sensor/lokasi dirapihkan
 * + Badge "KIBLAT xxx,x°" di dalam kartu kompas (adopsi mockup Stitch)
 */

// ==================== KIBLAT COLORS (tema terang v2.9) ====================
private object KiblatColors {
    val Background = Color(0xFFF5F5F0)
    val Surface = Color(0xFFFFFFFF)
    val Primary = Color(0xFF1B4D3E)
    val PrimaryDark = Color(0xFF143A2E)
    val HeaderSubtitle = Color(0xFFBFDCC3)
    val Gold = Color(0xFFA97C0E)
    val GoldBright = Color(0xFFE9C46A)
    val GoldSoft = Color(0x1AA97C0E)
    val IconContainer = Color(0xFFE8F0EA)
    val TextPrimary = Color(0xFF1C1B16)
    val TextSecondary = Color(0xFF5B665B)
    val TextTertiary = Color(0xFF8A938A)
    val TickMinor = Color(0xFFB9C0B9)
    val TrackGray = Color(0xFFEDEDE6)
    val AlignedGreen = Color(0xFF4CAF50)
    val DangerSoft = Color(0xFFFCE8E6)
    val DangerRed = Color(0xFFB3261E)
    val AmberSoft = Color(0xFFFFF4D6)
    val AmberText = Color(0xFF9A6A00)
}

// ==================== MAIN SCREEN ====================
@Composable
fun KiblatScreen(
    context: Context,
    latitude: Double,
    longitude: Double,
    locationName: String,
    onBack: () -> Unit,
    onDetectLocation: () -> Unit = {}
) {
    val isLocationValid = latitude != 0.0 || longitude != 0.0

    val qiblaDirection = remember(latitude, longitude) {
        PrayerCalculator.calculateQiblaDirection(latitude, longitude)
    }
    val distanceToMecca = remember(latitude, longitude) {
        calculateDistanceToMecca(latitude, longitude)
    }

    // Sensor kompas (A3/A4/A6)
    val compass = rememberCompassState(context)
    val azimuth = compass.azimuth

    // A5: selisih arah perangkat vs kiblat, dinormalisasi -180..180
    val diffToQibla = ((qiblaDirection - azimuth + 540.0) % 360.0) - 180.0
    val isAligned = isLocationValid && compass.hasSensor && abs(diffToQibla) <= 4.0

    // A5: getar sekali setiap kali masuk kondisi terkunci
    val wasAligned = remember { mutableStateOf(false) }
    LaunchedEffect(isAligned) {
        if (isAligned && !wasAligned.value) vibrateOnce(context)
        wasAligned.value = isAligned
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KiblatColors.Background)
    ) {
        // ===== B1: Header pola v2.4-v2.8 =====
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(KiblatColors.PrimaryDark)
                .padding(start = 8.dp, end = 20.dp, top = 10.dp, bottom = 18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Kiblat",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Penunjuk arah Ka'bah",
                        style = MaterialTheme.typography.bodySmall,
                        color = KiblatColors.HeaderSubtitle
                    )
                }
            }
        }

        // ===== Konten scroll =====
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // B6: chip lokasi
            if (isLocationValid) {
                LocationChip(
                    locationName = locationName,
                    distanceKm = distanceToMecca
                )
            }

            when {
                // A3: perangkat tanpa sensor kompas
                !compass.hasSensor -> {
                    SensorErrorCard()
                }
                // A2: lokasi belum valid
                !isLocationValid -> {
                    WaitingLocationCard(
                        isLoading = false,
                        onDetectLocation = onDetectLocation
                    )
                }
                else -> {
                    // B2: kompas utama
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = KiblatColors.Surface),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                            ) {
                                KiblatDial(
                                    azimuth = azimuth,
                                    qiblaDirection = qiblaDirection.toFloat(),
                                    isAligned = isAligned
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            // Badge sudut kiblat (adopsi mockup Stitch)
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = KiblatColors.GoldSoft,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp, KiblatColors.Gold.copy(alpha = 0.45f)
                                )
                            ) {
                                Text(
                                    text = "KIBLAT ${formatDegrees(qiblaDirection)}\u00B0",
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = KiblatColors.Gold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // A4: chip kalibrasi akurasi rendah
                    if (compass.lowAccuracy) {
                        AccuracyChip()
                    }

                    // B4: banner status dinamis
                    StatusBanner(isAligned = isAligned)

                    // B3: kartu info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        KiblatInfoCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Explore,
                            label = "Arah Kiblat",
                            value = "${formatDegrees(qiblaDirection)}\u00B0",
                            subtitle = getDirectionName(qiblaDirection)
                        )
                        KiblatInfoCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Place,
                            label = "Jarak ke Ka'bah",
                            value = "${formatDistance(distanceToMecca)} km",
                            subtitle = "Makkah Al-Mukarramah"
                        )
                    }

                    // B5: kartu instruksi
                    HelpCard()
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ==================== CHIP LOKASI (B6) ====================
@Composable
private fun LocationChip(locationName: String, distanceKm: Double) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Surface(
            shape = RoundedCornerShape(50),
            color = KiblatColors.Surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, KiblatColors.TrackGray)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(KiblatColors.IconContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = KiblatColors.Primary,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$locationName \u00B7 ${formatDistance(distanceKm)} km dari Ka'bah",
                    style = MaterialTheme.typography.labelMedium,
                    color = KiblatColors.TextSecondary
                )
            }
        }
    }
}

// ==================== DIAL KOMPAS (B2 + fix A1) ====================
@Composable
private fun KiblatDial(
    azimuth: Float,
    qiblaDirection: Float,
    isAligned: Boolean
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = size.minDimension / 2f - 6.dp.toPx()

        // Cincin luar — emas; berubah hijau saat terkunci
        drawCircle(
            color = if (isAligned) KiblatColors.AlignedGreen else KiblatColors.Gold,
            radius = radius,
            center = Offset(cx, cy),
            style = Stroke(width = (if (isAligned) 4.dp else 3.dp).toPx())
        )
        drawCircle(
            color = KiblatColors.Gold.copy(alpha = 0.3f),
            radius = radius - 4.dp.toPx(),
            center = Offset(cx, cy),
            style = Stroke(width = 1.dp.toPx())
        )

        /**
         * FIX A1: satu rumus sudut untuk SEMUA elemen dial.
         * Kanvas: 0 rad = kanan (timur layar); untuk menarik bearing X
         * pada kompas yang berputar mengikuti azimuth perangkat:
         * sudut = radians(bearing - 90 - azimuth).
         * Dulu tick & label TANPA -90 sedangkan jarum memakai -90 ->
         * dial dan jarum tidak pernah cocok (meleset 90 derajat).
         */
        fun angleFor(bearing: Double): Float =
            Math.toRadians(bearing - 90.0 - azimuth.toDouble()).toFloat()

        // Tick marks tiap 2 derajat
        for (i in 0..358 step 2) {
            val angle = angleFor(i.toDouble())
            val isCardinal = i % 90 == 0
            val isMajor = i % 30 == 0
            val isMinor = i % 10 == 0

            val innerR = when {
                isCardinal -> radius - 24.dp.toPx()
                isMajor -> radius - 19.dp.toPx()
                isMinor -> radius - 14.dp.toPx()
                else -> radius - 9.dp.toPx()
            }
            val outerR = radius - 5.dp.toPx()

            val color = when {
                isCardinal -> KiblatColors.Gold
                isMajor -> KiblatColors.TextTertiary
                else -> KiblatColors.TickMinor
            }
            val width = when {
                isCardinal -> 2.5.dp.toPx()
                isMajor -> 1.5.dp.toPx()
                else -> 0.8.dp.toPx()
            }

            drawLine(
                color,
                Offset(cx + innerR * cos(angle), cy + innerR * sin(angle)),
                Offset(cx + outerR * cos(angle), cy + outerR * sin(angle)),
                strokeWidth = width
            )
        }

        // Label mata angin Indonesia
        val directions = listOf(
            "U" to 0f, "TL" to 45f, "T" to 90f, "TG" to 135f,
            "S" to 180f, "BD" to 225f, "B" to 270f, "BL" to 315f
        )
        for ((label, deg) in directions) {
            val angle = angleFor(deg)
            val textR = radius - 36.dp.toPx()
            val x = cx + textR * cos(angle)
            val y = cy + textR * sin(angle)
            val isCardinal = deg % 90f == 0f
            val layout = textMeasurer.measure(
                text = label,
                style = TextStyle(
                    color = if (isCardinal) KiblatColors.Gold else KiblatColors.TextTertiary,
                    fontSize = if (isCardinal) 14.sp else 10.sp,
                    fontWeight = if (isCardinal) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            )
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(x - layout.size.width / 2f, y - layout.size.height / 2f)
            )
        }

        // Marker target hijau di puncak dial (arah hadapan perangkat)
        val markerY = cy - radius - 1.dp.toPx()
        drawPath(
            path = Path().apply {
                moveTo(cx, markerY + 9.dp.toPx())
                lineTo(cx - 6.dp.toPx(), markerY - 2.dp.toPx())
                lineTo(cx + 6.dp.toPx(), markerY - 2.dp.toPx())
                close()
            },
            color = KiblatColors.AlignedGreen
        )

        // Lingkaran deko dalam
        drawCircle(
            KiblatColors.TrackGray, radius * 0.38f, center = Offset(cx, cy),
            style = Stroke(width = 1.dp.toPx())
        )

        // ===== Jarum kiblat (rumus sama dgn dial — fix A1) =====
        val needle = angleFor(qiblaDirection.toDouble())
        drawLine(
            KiblatColors.Gold,
            Offset(cx, cy),
            Offset(
                cx + radius * 0.75f * cos(needle),
                cy + radius * 0.75f * sin(needle)
            ),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Kepala panah jarum
        val tipX = cx + radius * 0.78f * cos(needle)
        val tipY = cy + radius * 0.78f * sin(needle)
        val left = needle + Math.toRadians(150.0).toFloat()
        val right = needle - Math.toRadians(150.0).toFloat()
        val arrow = 12.dp.toPx()
        drawPath(
            path = Path().apply {
                moveTo(tipX, tipY)
                lineTo(tipX + arrow * cos(left), tipY + arrow * sin(left))
                lineTo(tipX + arrow * cos(right), tipY + arrow * sin(right))
                close()
            },
            color = KiblatColors.Gold
        )

        // Label Ka'bah di dekat ujung jarum
        val labelR = radius * 0.62f
        val lx = cx + labelR * cos(needle)
        val ly = cy + labelR * sin(needle)
        val kaaba = textMeasurer.measure(
            text = "\u0643\u0639\u0628\u0629",
            style = TextStyle(
                color = if (isAligned) KiblatColors.AlignedGreen else KiblatColors.GoldBright,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
        drawText(
            textLayoutResult = kaaba,
            topLeft = Offset(lx - kaaba.size.width / 2f, ly - kaaba.size.height / 2f)
        )

        // Titik pusat
        drawCircle(KiblatColors.Gold, 6.dp.toPx(), center = Offset(cx, cy))
        drawCircle(KiblatColors.Surface, 3.dp.toPx(), center = Offset(cx, cy))
    }
}

// ==================== BANNER STATUS (B4) ====================
@Composable
private fun StatusBanner(isAligned: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isAligned) KiblatColors.IconContainer else KiblatColors.Surface
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (!isAligned) androidx.compose.foundation.BorderStroke(1.dp, KiblatColors.TrackGray) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isAligned) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = KiblatColors.AlignedGreen,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Arah Kiblat Terkunci — Anda menghadap Ka'bah",
                    style = MaterialTheme.typography.bodyMedium,
                    color = KiblatColors.Primary,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Icon(
                    Icons.Default.Navigation,
                    contentDescription = null,
                    tint = KiblatColors.TextTertiary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Putar perangkat hingga jarum sejajar tanda hijau",
                    style = MaterialTheme.typography.bodyMedium,
                    color = KiblatColors.TextSecondary
                )
            }
        }
    }
}

// ==================== CHIP KALIBRASI (A4) ====================
@Composable
private fun AccuracyChip() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = KiblatColors.AmberSoft),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = KiblatColors.AmberText,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Akurasi kompas rendah — gerakkan perangkat membentuk angka 8",
                style = MaterialTheme.typography.labelSmall,
                color = KiblatColors.AmberText
            )
        }
    }
}

// ==================== KARTU INFO (B3) ====================
@Composable
private fun KiblatInfoCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    subtitle: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = KiblatColors.Surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(KiblatColors.IconContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = KiblatColors.Primary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = KiblatColors.TextTertiary
            )
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                color = KiblatColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = KiblatColors.TextTertiary
            )
        }
    }
}

// ==================== KARTU INSTRUKSI (B5) ====================
@Composable
private fun HelpCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = KiblatColors.Surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(KiblatColors.IconContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = KiblatColors.Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Letakkan perangkat datar, jauhkan dari benda logam, lalu gerakkan perangkat membentuk angka 8 untuk kalibrasi.",
                style = MaterialTheme.typography.bodySmall,
                color = KiblatColors.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

// ==================== KARTU MENUNGGU LOKASI (A2) ====================
@Composable
private fun WaitingLocationCard(
    isLoading: Boolean,
    onDetectLocation: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = KiblatColors.Surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(KiblatColors.IconContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = KiblatColors.Primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Menunggu Lokasi",
                style = MaterialTheme.typography.titleMedium,
                color = KiblatColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Arah kiblat dihitung dari posisi Anda. Aktifkan GPS lalu deteksi lokasi agar hasilnya akurat.",
                style = MaterialTheme.typography.bodySmall,
                color = KiblatColors.TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onDetectLocation,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = KiblatColors.PrimaryDark,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mendeteksi...")
                } else {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Deteksi Lokasi")
                }
            }
        }
    }
}

// ==================== KARTU SENSOR TIDAK TERSEDIA (A3) ====================
@Composable
private fun SensorErrorCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = KiblatColors.Surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(KiblatColors.DangerSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = KiblatColors.DangerRed,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Sensor Kompas Tidak Tersedia",
                style = MaterialTheme.typography.titleMedium,
                color = KiblatColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Perangkat ini tidak memiliki sensor magnetometer, sehingga penunjuk arah kiblat tidak dapat ditampilkan.",
                style = MaterialTheme.typography.bodySmall,
                color = KiblatColors.TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

// ==================== SENSOR HELPER (A3/A4/A6) ====================
private data class CompassData(
    val azimuth: Float,
    val hasSensor: Boolean,
    val lowAccuracy: Boolean
)

@Composable
private fun rememberCompassState(context: Context): CompassData {
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    var azimuth by remember { mutableFloatStateOf(0f) }
    var hasSensor by remember { mutableStateOf(true) }
    var lowAccuracy by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        // A3: tanpa kombinasi sensor arah -> halaman tampil pesan jujur
        if (rotationVector == null && (accelerometer == null || magnetometer == null)) {
            hasSensor = false
            return@DisposableEffect onDispose { }
        }

        val matrix = FloatArray(9)
        val adjusted = FloatArray(9)
        val orientation = FloatArray(3)
        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)
        var lastUpdate = 0L

        // A6: remap matriks rotasi sesuai orientasi layar (portrait/landscape)
        fun applyMatrix(src: FloatArray) {
            val rotation = try {
                (context as? Activity)?.windowManager?.defaultDisplay?.rotation
                    ?: Surface.ROTATION_0
            } catch (e: Exception) {
                Surface.ROTATION_0
            }
            when (rotation) {
                Surface.ROTATION_90 ->
                    SensorManager.remapCoordinateSystem(src, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, adjusted)
                Surface.ROTATION_270 ->
                    SensorManager.remapCoordinateSystem(src, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, adjusted)
                Surface.ROTATION_180 ->
                    SensorManager.remapCoordinateSystem(src, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, adjusted)
                else -> System.arraycopy(src, 0, adjusted, 0, 9)
            }
            SensorManager.getOrientation(adjusted, orientation)
            var deg = Math.toDegrees(orientation[0].toDouble()).toFloat()
            deg = (deg + 360f) % 360f
            azimuth = deg
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ROTATION_VECTOR -> {
                        SensorManager.getRotationMatrixFromVector(matrix, event.values)
                        applyMatrix(matrix)
                    }
                    Sensor.TYPE_ACCELEROMETER -> System.arraycopy(event.values, 0, gravity, 0, 3)
                    Sensor.TYPE_MAGNETIC_FIELD -> System.arraycopy(event.values, 0, geomagnetic, 0, 3)
                }
                // Jalur fallback accel+mag dengan low-pass filter 50ms
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER ||
                    event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD
                ) {
                    val now = System.currentTimeMillis()
                    if (now - lastUpdate < 50) return
                    lastUpdate = now
                    if (SensorManager.getRotationMatrix(matrix, null, gravity, geomagnetic)) {
                        applyMatrix(matrix)
                    }
                }
            }

            // A4: pantau akurasi magnetometer utk chip kalibrasi
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                if (sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    lowAccuracy = accuracy < SensorManager.SENSOR_STATUS_ACCURACY_HIGH
                }
            }
        }

        if (rotationVector != null) {
            sensorManager.registerListener(listener, rotationVector, SensorManager.SENSOR_DELAY_GAME)
        } else {
            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
            sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_GAME)
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return CompassData(azimuth, hasSensor, lowAccuracy)
}

// ==================== GETAR SAAT TERKUNCI (A5) ====================
private fun vibrateOnce(context: Context) {
    try {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? android.os.VibratorManager
            vm?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? android.os.Vibrator
        }
        if (vibrator == null) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(150)
        }
    } catch (e: Exception) {
        // Getaran bersifat pelengkap — gagalnya diabaikan
    }
}

// ==================== UTILITY ====================
private fun getDirectionName(degrees: Double): String {
    val d = ((degrees % 360) + 360) % 360
    return when (d) {
        in 0.0..22.5, in 337.5..360.0 -> "Utara"
        in 22.5..67.5 -> "Timur Laut"
        in 67.5..112.5 -> "Timur"
        in 112.5..157.5 -> "Tenggara"
        in 157.5..202.5 -> "Selatan"
        in 202.5..247.5 -> "Barat Daya"
        in 247.5..292.5 -> "Barat"
        in 292.5..337.5 -> "Barat Laut"
        else -> "Utara"
    }
}

/** Format derajat gaya Indonesia: koma desimal (sesuai mockup yang disetujui). */
private fun formatDegrees(deg: Double): String =
    String.format(Locale("id", "ID"), "%.1f", deg)

/** Format jarak gaya Indonesia: pemisah ribuan titik. */
private fun formatDistance(km: Double): String =
    String.format(Locale("id", "ID"), "%,.0f", km)

/**
 * Hitung jarak dari lokasi ke Kaabah menggunakan formula Haversine.
 */
private fun calculateDistanceToMecca(lat: Double, lng: Double): Double {
    val R = 6371.0 // Earth radius in km
    val lat1Rad = Math.toRadians(lat)
    val lat2Rad = Math.toRadians(PrayerCalculator.KAABA_LAT)
    val deltaLat = Math.toRadians(PrayerCalculator.KAABA_LAT - lat)
    val deltaLng = Math.toRadians(PrayerCalculator.KAABA_LNG - lng)

    val a = sin(deltaLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(deltaLng / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c
}
