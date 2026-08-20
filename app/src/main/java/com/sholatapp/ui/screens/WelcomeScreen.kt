package com.sholatapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholatapp.ui.theme.DarkColors
import kotlinx.coroutines.delay

/**
 * Welcome screen shown on first app launch.
 * Collects user name for personalized greeting.
 */
@Composable
fun WelcomeScreen(
    onComplete: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
        delay(500)
        focusRequester.requestFocus()
    }

    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Islamic crescent symbol
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(800)) + slideInVertically(
                    initialOffsetY = { -40 },
                    animationSpec = tween(800)
                )
            ) {
                Text(
                    text = "\u262A",
                    style = TextStyle(
                        fontSize = 64.sp,
                        color = DarkColors.Gold
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App name
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(800, delayMillis = 200)) + slideInVertically(
                    initialOffsetY = { -30 },
                    animationSpec = tween(800, delayMillis = 200)
                )
            ) {
                Text(
                    text = "SholatApp",
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
                enter = fadeIn(animationSpec = tween(800, delayMillis = 400)) + slideInVertically(
                    initialOffsetY = { -20 },
                    animationSpec = tween(800, delayMillis = 400)
                )
            ) {
                Text(
                    text = "Assalamualaikum Warahmatullahi Wabarakatuh",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkColors.TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Name input section
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(800, delayMillis = 600)) + slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(800, delayMillis = 600)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Siapa nama Anda?",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkColors.TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Nama Anda akan digunakan untuk salam di beranda",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkColors.TextTertiary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Input field
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        placeholder = {
                            Text(
                                "Masukkan nama Anda",
                                color = DarkColors.TextTertiary
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = DarkColors.Gold
                            )
                        },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = DarkColors.TextPrimary,
                            fontSize = 16.sp
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkColors.Gold,
                            unfocusedBorderColor = DarkColors.Border,
                            focusedContainerColor = DarkColors.SurfaceVariant,
                            unfocusedContainerColor = DarkColors.SurfaceVariant,
                            cursorColor = DarkColors.Gold
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (name.isNotBlank()) {
                                    onComplete(name.trim())
                                }
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Continue button
                    Button(
                        onClick = { onComplete(name.trim()) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = name.isNotBlank(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkColors.Gold,
                            contentColor = Color.Black,
                            disabledContainerColor = DarkColors.Border,
                            disabledContentColor = DarkColors.TextTertiary
                        )
                    ) {
                        Text(
                            text = "Mulai",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Anda bisa mengubah nama ini di Pengaturan",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkColors.TextTertiary
                    )
                }
            }
        }
    }
}
