package com.sholatapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.core.content.ContextCompat
import com.sholatapp.azan.AzanPlayer
import com.sholatapp.ui.theme.DarkColors
import com.sholatapp.ui.theme.SholatAppTheme
import com.sholatapp.ui.screens.*
import com.sholatapp.viewmodel.PrayerViewModel

enum class AppTab(val label: String, val icon: ImageVector) {
    BERANDA("Beranda", Icons.Default.Home),
    SALAT("Salat", Icons.Default.AccessTime),
    TASBIH("Zikir", Icons.Default.TouchApp),
    PUASA("Puasa", Icons.Default.Brightness3),
    PROFIL("Pengaturan", Icons.Default.Settings)
}

/**
 * Halaman overlay yang dibuka DI ATAS tab aktif.
 *
 * Sejak v2.2, 6 boolean overlay terpisah diganti satu state ini supaya
 * navigasi lebih aman & mudah dikembangkan. Tombol back sistem akan
 * menutup overlay (BackHandler) alih-alih keluar dari aplikasi.
 */
sealed class AppScreen {
    data object Kiblat : AppScreen()
    data object Kalender : AppScreen()
    data object MenuLainnya : AppScreen()
    data object PusatNotifikasi : AppScreen()
    data object AzanPicker : AppScreen()
    data object AlQuran : AppScreen()
}

class MainActivity : ComponentActivity() {

    private val viewModel: PrayerViewModel by viewModels()
    private val appPrefs by lazy { getSharedPreferences("sholatapp_prefs", MODE_PRIVATE) }
    private val azanPlayer by lazy { AzanPlayer(this) }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val hasLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (hasLocation) viewModel.detectLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
        checkExactAlarmPermission()

        val isFirstRun = appPrefs.getBoolean("is_first_run", true)
        val hasChosenAzan = azanPlayer.hasSelectedAzan()

        setContent {
            SholatAppTheme {
                when {
                    // Step 1: First run — Welcome screen (enter name)
                    isFirstRun -> {
                        WelcomeScreen(
                            onComplete = { name ->
                                appPrefs.edit()
                                    .putString("user_name", name)
                                    .putBoolean("is_first_run", false)
                                    .apply()
                                recreate()
                            }
                        )
                    }
                    // Step 2: Name set but azan not chosen — Azan picker
                    !hasChosenAzan -> {
                        AzanPickerScreen(
                            azanPlayer = azanPlayer,
                            onComplete = { recreate() }
                        )
                    }
                    // Step 3: Main app
                    else -> {
                        var selectedTab by remember { mutableStateOf(AppTab.BERANDA) }
                        var overlayScreen by remember { mutableStateOf<AppScreen?>(null) }
                        val uiState by viewModel.uiState.collectAsState()
                        val userName = remember {
                            appPrefs.getString("user_name", "") ?: ""
                        }

                        // Tombol back sistem: tutup overlay dulu, jangan keluar aplikasi
                        BackHandler(enabled = overlayScreen != null) {
                            overlayScreen = null
                        }

                        Scaffold(
                            containerColor = DarkColors.Background,
                            bottomBar = {
                                BottomNavBar(
                                    selectedTab = selectedTab,
                                    onTabSelected = { selectedTab = it }
                                )
                            }
                        ) { paddingValues ->
                            Box(modifier = Modifier.padding(paddingValues)) {
                                // Konten tab — transisi fade + slide halus antar tab
                                AnimatedContent(
                                    targetState = selectedTab,
                                    transitionSpec = {
                                        (fadeIn(animationSpec = tween(220)) +
                                                slideInVertically(animationSpec = tween(220)) { it / 24 }) togetherWith
                                                (fadeOut(animationSpec = tween(150)) +
                                                slideOutVertically(animationSpec = tween(150)) { -it / 32 })
                                    },
                                    label = "tabContent"
                                ) { tab ->
                                    when (tab) {
                                        AppTab.BERANDA -> HomeScreen(
                                            viewModel = viewModel,
                                            userName = userName,
                                            onKiblatClick = { overlayScreen = AppScreen.Kiblat },
                                            onQuranClick = { overlayScreen = AppScreen.AlQuran },
                                            onKalenderClick = { overlayScreen = AppScreen.Kalender },
                                            onNotificationClick = { overlayScreen = AppScreen.PusatNotifikasi },
                                            onSettingsClick = { selectedTab = AppTab.PROFIL },
                                            onSalatClick = { selectedTab = AppTab.SALAT },
                                            onLainnyaClick = { overlayScreen = AppScreen.MenuLainnya }
                                        )
                                        AppTab.SALAT -> SalatScreen(
                                            viewModel = viewModel,
                                            onKalenderClick = { overlayScreen = AppScreen.Kalender }
                                        )
                                        AppTab.TASBIH -> DzikirScreen(
                                            onQuranClick = { overlayScreen = AppScreen.AlQuran }
                                        )
                                        AppTab.PUASA -> PuasaScreen(viewModel = viewModel)
                                        AppTab.PROFIL -> SettingsScreen(
                                            uiState = uiState,
                                            onToggleAlarm = { viewModel.toggleAlarm(it) },
                                            onTogglePrepAlarm = { viewModel.togglePrepAlarm(it) },
                                            onToggleDnd = { viewModel.toggleDnd(it) },
                                            context = this@MainActivity,
                                            onResetDzikir = {
                                                val dp = getSharedPreferences("dzikir_prefs", MODE_PRIVATE)
                                                dp.edit().clear().apply()
                                                android.widget.Toast.makeText(this@MainActivity, "Progress dzikir direset", android.widget.Toast.LENGTH_SHORT).show()
                                            },
                                            onChangeAzan = { overlayScreen = AppScreen.AzanPicker }
                                        )
                                    }
                                }

                                // Overlay — slide masuk dari kanan, slide keluar saat kembali
                                AnimatedContent(
                                    targetState = overlayScreen,
                                    transitionSpec = {
                                        (slideInHorizontally(animationSpec = tween(280)) { it } +
                                                fadeIn(animationSpec = tween(220))) togetherWith
                                                (slideOutHorizontally(animationSpec = tween(240)) { it } +
                                                fadeOut(animationSpec = tween(180)))
                                    },
                                    label = "overlayContent"
                                ) { screen ->
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        when (screen) {
                                            is AppScreen.Kiblat -> KiblatScreen(
                                                context = this@MainActivity,
                                                latitude = uiState.latitude,
                                                longitude = uiState.longitude,
                                                locationName = uiState.locationAddress,
                                                onBack = { overlayScreen = null }
                                            )
                                            is AppScreen.Kalender -> KalenderScreen(
                                                onBack = { overlayScreen = null }
                                            )
                                            is AppScreen.MenuLainnya -> MenuLainnyaScreen(
                                                onBack = { overlayScreen = null }
                                            )
                                            is AppScreen.PusatNotifikasi -> PusatNotifikasiScreen(
                                                uiState = uiState,
                                                onBack = { overlayScreen = null }
                                            )
                                            is AppScreen.AzanPicker -> AzanPickerScreen(
                                                azanPlayer = azanPlayer,
                                                onComplete = { overlayScreen = null }
                                            )
                                            is AppScreen.AlQuran -> QuranScreen(
                                                onBack = { overlayScreen = null }
                                            )
                                            null -> { /* tidak ada overlay */ }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        azanPlayer.stopAzan()
    }

    private fun requestPermissions() {
        val p = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            p.add(Manifest.permission.ACCESS_FINE_LOCATION)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            p.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                p.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (p.isNotEmpty()) permissionLauncher.launch(p.toTypedArray())
    }

    private fun checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = getSystemService(ALARM_SERVICE) as android.app.AlarmManager
            if (!am.canScheduleExactAlarms()) {
                android.widget.Toast.makeText(this, "Izinkan alarm tepat di pengaturan untuk pengingat sholat", android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
private fun BottomNavBar(selectedTab: AppTab, onTabSelected: (AppTab) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DarkColors.Surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(top = 6.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AppTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                val tint by animateColorAsState(
                    targetValue = if (isSelected) DarkColors.Primary else DarkColors.TextTertiary,
                    label = "tabTint"
                )
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) DarkColors.PrimaryContainer else Color.Transparent,
                    label = "tabBg"
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 6.dp)
                ) {
                    Icon(
                        tab.icon,
                        contentDescription = tab.label,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        tab.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = tint,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
