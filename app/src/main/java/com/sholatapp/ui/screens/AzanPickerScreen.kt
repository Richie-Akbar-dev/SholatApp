package com.sholatapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.sholatapp.azan.AzanPlayer
import com.sholatapp.ui.theme.DarkColors
import kotlinx.coroutines.delay

/**
 * First-time azan picker screen shown after WelcomeScreen.
 * User selects one of 3 azan sounds bundled with the app.
 */
@Composable
fun AzanPickerScreen(
    azanPlayer: AzanPlayer,
    onComplete: () -> Unit
) {
    var selectedId by remember { mutableStateOf(azanPlayer.getSelectedAzanId()) }
    var previewId by remember { mutableStateOf<String?>(null) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        isVisible = true
    }

    // Auto-stop preview after 20 seconds
    LaunchedEffect(previewId) {
        if (previewId != null) {
            delay(20000)
            azanPlayer.stopAzan()
            previewId = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Icon
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(600)) + scaleIn(animationSpec = tween(600))
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = DarkColors.Gold,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(600, delayMillis = 100)) + slideInVertically(
                    initialOffsetY = { -20 },
                    animationSpec = tween(600, delayMillis = 100)
                )
            ) {
                Text(
                    text = "Pilih Suara Adzan",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DarkColors.Gold,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(600, delayMillis = 200)) + slideInVertically(
                    initialOffsetY = { -15 },
                    animationSpec = tween(600, delayMillis = 200)
                )
            ) {
                Text(
                    text = "Suara adzan akan diputar saat waktu sholat tiba",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkColors.TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Azan options list
            AzanPlayer.ALL_AZAN_OPTIONS.forEachIndexed { index, option ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(500, delayMillis = 300 + index * 100L)) +
                            slideInVertically(
                                initialOffsetY = { 30 },
                                animationSpec = tween(500, delayMillis = 300 + index * 100L)
                            )
                ) {
                    AzanOptionCard(
                        option = option,
                        isSelected = selectedId == option.id,
                        isPreviewing = previewId == option.id,
                        onSelect = {
                            azanPlayer.stopAzan()
                            selectedId = it
                            previewId = null
                        },
                        onPreviewClick = {
                            if (previewId == option.id) {
                                azanPlayer.stopAzan()
                                previewId = null
                            } else {
                                azanPlayer.stopAzan()
                                azanPlayer.playPreview(option.id)
                                previewId = option.id
                            }
                        }
                    )

                    if (index < AzanPlayer.ALL_AZAN_OPTIONS.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Confirm button
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(500, delayMillis = 600)) + slideInVertically(
                    initialOffsetY = { 20 },
                    animationSpec = tween(500, delayMillis = 600)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            azanPlayer.stopAzan()
                            azanPlayer.setSelectedAzan(selectedId)
                            onComplete()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkColors.Gold,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Pilih dan Lanjutkan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = {
                        azanPlayer.stopAzan()
                        onComplete()
                    }) {
                        Text(
                            text = "Lewati",
                            style = MaterialTheme.typography.labelMedium,
                            color = DarkColors.TextTertiary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun AzanOptionCard(
    option: AzanPlayer.AzanOption,
    isSelected: Boolean,
    isPreviewing: Boolean,
    onSelect: (String) -> Unit,
    onPreviewClick: () -> Unit
) {
    val borderColor = when {
        isSelected -> DarkColors.Gold
        isPreviewing -> DarkColors.PrimaryLight
        else -> Color.Transparent
    }
    val bgColor = when {
        isSelected -> Color(0x1AD4AF37)
        else -> DarkColors.Surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(option.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        ),
        border = if (isSelected || isPreviewing) {
            BorderStroke(2.dp, borderColor)
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radio indicator
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) DarkColors.Gold else Color.Transparent
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) DarkColors.Gold else DarkColors.Border,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Azan info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = option.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected) DarkColors.Gold else DarkColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkColors.TextSecondary
                )
            }

            // Preview button
            IconButton(
                onClick = onPreviewClick,
                modifier = Modifier.size(44.dp)
            ) {
                if (isPreviewing) {
                    Icon(
                        Icons.Default.Stop,
                        contentDescription = "Stop preview",
                        tint = DarkColors.Gold,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Preview adzan",
                        tint = if (isSelected) DarkColors.Gold else DarkColors.TextTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
