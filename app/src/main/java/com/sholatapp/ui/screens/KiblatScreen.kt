package com.sholatapp.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.calculation.PrayerCalculator
import com.sholatapp.ui.theme.DarkColors
import kotlin.math.*

// ==================== KIBLAT COLORS ====================
private object KiblatColors {
    val Background = Color(0xFF0A1A0A)
    val Gold = Color(0xFFD4AF37)
    val GoldLight = Color(0xFFFFD54F)
    val Green = Color(0xFF1B4D3E)
    val GreenLight = Color(0xFF4CAF50)
    val White = Color(0xFFFFFFFF)
    val TextMuted = Color(0xFFA5D6A7)
    val TextDim = Color(0xFF6B9B6B)
    val Surface = Color(0xFF112211)
    val SurfaceLight = Color(0xFF1A2E1A)
    val CardBg = Color(0xB3112211)
    val CardBorder = Color(0x33D4AF37)
}

// ==================== MAIN SCREEN ====================
@Composable
fun KiblatScreen(
    context: Context,
    latitude: Double,
    longitude: Double,
    locationName: String,
    onBack: () -> Unit
) {
    val qiblaDirection = remember(latitude, longitude) {
        PrayerCalculator.calculateQiblaDirection(latitude, longitude)
    }
    val distanceToMecca = remember(latitude, longitude) {
        calculateDistanceToMecca(latitude, longitude)
    }

    // Sensor state
    val azimuth = rememberSensorAzimuth(context)
    val qiblaRelative = (qiblaDirection - azimuth + 360) % 360f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KiblatColors.Background)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = KiblatColors.Gold
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Arah Kiblat",
                    style = MaterialTheme.typography.titleLarge,
                    color = KiblatColors.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    locationName,
                    style = MaterialTheme.typography.bodySmall,
                    color = KiblatColors.TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Compass
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            KiblatCompassCanvas(
                azimuth = azimuth,
                qiblaDirection = qiblaDirection.toFloat()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Qibla info cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KiblatInfoCard(
                modifier = Modifier.weight(1f),
                icon = { Icon(Icons.Default.Explore, null, tint = KiblatColors.Gold, modifier = Modifier.size(20.dp)) },
                label = "Arah Kiblat",
                value = "${String.format("%.1f", qiblaDirection)}\u00B0",
                subtitle = getDirectionName(qiblaDirection)
            )
            KiblatInfoCard(
                modifier = Modifier.weight(1f),
                icon = { Icon(Icons.Default.Place, null, tint = KiblatColors.Gold, modifier = Modifier.size(20.dp)) },
                label = "Jarak ke Kaabah",
                value = String.format("%,.0f km", distanceToMecca),
                subtitle = "Mekkah Al-Mukarramah"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        KiblatInfoCard(
            modifier = Modifier.fillMaxWidth(),
            icon = { Icon(Icons.Default.Navigation, null, tint = KiblatColors.GoldLight, modifier = Modifier.size(20.dp)) },
            label = "Posisi Perangkat",
            value = "${String.format("%.1f", azimuth)}\u00B0 ${getDirectionName(azimuth.toDouble())}",
            subtitle = "Arah perangkat Anda saat ini"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Instruction
        Card(
            colors = CardDefaults.cardColors(containerColor = KiblatColors.CardBg),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, KiblatColors.CardBorder)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    null,
                    tint = KiblatColors.TextMuted,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Arahkan perangkat ke depan Anda. Jarum emas menunjuk arah Kaabah di Mekkah.",
                    style = MaterialTheme.typography.bodySmall,
                    color = KiblatColors.TextMuted,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ==================== COMPASS CANVAS ====================
@Composable
private fun KiblatCompassCanvas(
    azimuth: Float,
    qiblaDirection: Float
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier.fillMaxSize().clip(CircleShape)
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = size.minDimension / 2f

        // Background circle
        drawCircle(KiblatColors.Surface, radius, center = Offset(cx, cy))

        // Outer ring
        drawCircle(
            KiblatColors.Green, radius, center = Offset(cx, cy),
            style = Stroke(width = 3.dp.toPx())
        )
        drawCircle(
            KiblatColors.Gold.copy(alpha = 0.3f), radius - 3.dp.toPx(), center = Offset(cx, cy),
            style = Stroke(width = 1.dp.toPx())
        )

        // Rotate all elements based on azimuth (top of screen = device facing direction)
        val azimuthRad = Math.toRadians(-azimuth.toDouble())

        // Degree tick marks
        for (i in 0..359 step 2) {
            val angle = Math.toRadians(i.toDouble()) + azimuthRad
            val isCardinal = i % 90 == 0
            val isMajor = i % 30 == 0
            val isMinor = i % 10 == 0

            val innerR = when {
                isCardinal -> radius - 28.dp.toPx()
                isMajor -> radius - 22.dp.toPx()
                isMinor -> radius - 16.dp.toPx()
                else -> radius - 10.dp.toPx()
            }
            val outerR = radius - 6.dp.toPx()

            val color = when {
                isCardinal -> KiblatColors.Gold
                isMajor -> KiblatColors.TextMuted
                else -> KiblatColors.TextDim
            }
            val width = when {
                isCardinal -> 2.5.dp.toPx()
                isMajor -> 1.5.dp.toPx()
                else -> 0.8.dp.toPx()
            }

            drawLine(
                color,
                Offset(cx + innerR * cos(angle).toFloat(), cy + innerR * sin(angle).toFloat()),
                Offset(cx + outerR * cos(angle).toFloat(), cy + outerR * sin(angle).toFloat()),
                strokeWidth = width
            )
        }

        // Cardinal direction labels
        val directions = listOf(
            "U" to 0f, "TL" to 45f, "T" to 90f, "TG" to 135f,
            "S" to 180f, "BD" to 225f, "B" to 270f, "BL" to 315f
        )
        for ((label, deg) in directions) {
            val angle = Math.toRadians(deg.toDouble()) + azimuthRad
            val textR = radius - 40.dp.toPx()
            val x = cx + textR * cos(angle).toFloat()
            val y = cy + textR * sin(angle).toFloat()
            val isCardinal = deg % 90f == 0f
            val textLayout = textMeasurer.measure(
                text = label,
                style = TextStyle(
                    color = if (isCardinal) KiblatColors.Gold else KiblatColors.TextMuted,
                    fontSize = if (isCardinal) 14.sp else 10.sp,
                    fontWeight = if (isCardinal) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            )
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(x - textLayout.size.width / 2f, y - textLayout.size.height / 2f)
            )
        }

        // Inner decorative circles
        drawCircle(
            KiblatColors.TextDim.copy(alpha = 0.2f), radius * 0.38f, center = Offset(cx, cy),
            style = Stroke(width = 1.dp.toPx())
        )
        drawCircle(
            KiblatColors.TextDim.copy(alpha = 0.15f), radius * 0.15f, center = Offset(cx, cy),
            style = Stroke(width = 1.dp.toPx())
        )

        // ===== QIBLA NEEDLE (doesn't rotate with compass) =====
        val qiblaAngle = Math.toRadians(qiblaDirection.toDouble() - 90.0)

        // Qibla line from center to edge
        drawLine(
            KiblatColors.Gold,
            Offset(cx, cy),
            Offset(
                cx + radius * 0.75f * cos(qiblaAngle).toFloat(),
                cy + radius * 0.75f * sin(qiblaAngle).toFloat()
            ),
            strokeWidth = 3.dp.toPx()
        )

        // Qibla arrowhead (triangle at tip)
        val arrowTipX = cx + radius * 0.78f * cos(qiblaAngle).toFloat()
        val arrowTipY = cy + radius * 0.78f * sin(qiblaAngle).toFloat()
        val arrowAngle = qiblaAngle
        val arrowSize = 12.dp.toPx()
        val leftAngle = arrowAngle + Math.toRadians(150.0)
        val rightAngle = arrowAngle - Math.toRadians(150.0)

        drawPath(
            path = Path().apply {
                moveTo(arrowTipX, arrowTipY)
                lineTo(
                    arrowTipX + arrowSize * cos(leftAngle).toFloat(),
                    arrowTipY + arrowSize * sin(leftAngle).toFloat()
                )
                lineTo(
                    arrowTipX + arrowSize * cos(rightAngle).toFloat(),
                    arrowTipY + arrowSize * sin(rightAngle).toFloat()
                )
                close()
            },
            color = KiblatColors.Gold
        )

        // Kaabah icon text at needle tip
        val iconX = cx + radius * 0.62f * cos(qiblaAngle).toFloat()
        val iconY = cy + radius * 0.62f * sin(qiblaAngle).toFloat()
        val kaabahLayout = textMeasurer.measure(
            text = "\u0643\u0639\u0628\u0629", // Kaabah in Arabic
            style = TextStyle(
                color = KiblatColors.GoldLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
        drawText(
            textLayoutResult = kaabahLayout,
            topLeft = Offset(iconX - kaabahLayout.size.width / 2f, iconY - kaabahLayout.size.height / 2f)
        )

        // Center dot
        drawCircle(KiblatColors.Gold, 6.dp.toPx(), center = Offset(cx, cy))
        drawCircle(KiblatColors.Background, 3.dp.toPx(), center = Offset(cx, cy))
    }
}

// ==================== INFO CARD ====================
@Composable
private fun KiblatInfoCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    subtitle: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = KiblatColors.CardBg),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, KiblatColors.CardBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            icon()
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = KiblatColors.TextDim)
            Text(
                value, style = MaterialTheme.typography.titleLarge,
                color = KiblatColors.White, fontWeight = FontWeight.Bold
            )
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = KiblatColors.TextMuted)
        }
    }
}

// ==================== SENSOR HELPER ====================
@Composable
private fun rememberSensorAzimuth(context: Context): Float {
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    var azimuth by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (accelerometer == null || magnetometer == null) {
            onDispose { }
            return@DisposableEffect
        }

        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)
        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)
        var lastUpdateTime = 0L

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    System.arraycopy(event.values, 0, gravity, 0, 3)
                } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    System.arraycopy(event.values, 0, geomagnetic, 0, 3)
                }

                // Low-pass filter: update every 50ms
                val now = System.currentTimeMillis()
                if (now - lastUpdateTime < 50) return
                lastUpdateTime = now

                val success = SensorManager.getRotationMatrix(
                    rotationMatrix, null, gravity, geomagnetic
                )
                if (success) {
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    // Convert radians to degrees, adjust so 0=north, 90=east
                    var azimuthDeg = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    azimuthDeg = (azimuthDeg + 360) % 360
                    azimuth = azimuthDeg
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return azimuth
}

// ==================== UTILITY FUNCTIONS ====================
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
