package com.sholatapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
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
    TASBIH("Tasbih", Icons.Default.TouchApp),
    PUASA("Puasa", Icons.Default.Brightness3),
    PROFIL("Profil", Icons.Default.Person)
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
        val savedName = appPrefs.getString("user_name", "") ?: ""

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
                        var showKiblat by remember { mutableStateOf(false) }
                        var showKalender by remember { mutableStateOf(false) }
                        var showAzanPicker by remember { mutableStateOf(false) }
                        val uiState by viewModel.uiState.collectAsState()
                        val userName = remember {
                            appPrefs.getString("user_name", "") ?: ""
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
                                when (selectedTab) {
                                    AppTab.BERANDA -> HomeScreen(
                                        viewModel = viewModel,
                                        userName = userName,
                                        onKiblatClick = { showKiblat = true },
                                        onTasbihClick = { selectedTab = AppTab.TASBIH },
                                        onPuasaClick = { selectedTab = AppTab.PUASA },
                                        onKalenderClick = { showKalender = true },
                                        onNotificationClick = { /* TODO: Notifikasi */ },
                                        onSettingsClick = { selectedTab = AppTab.PROFIL },
                                        onSalatClick = { selectedTab = AppTab.SALAT }
                                    )
                                    AppTab.SALAT -> SalatScreen(
                                        viewModel = viewModel
                                    )
                                    AppTab.TASBIH -> DzikirScreen()
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
                                            android.widget.Toast.makeText(this, "Progress dzikir direset", android.widget.Toast.LENGTH_SHORT).show()
                                        },
                                        onChangeAzan = { showAzanPicker = true }
                                    )
                                }

                                // Kiblat overlay
                                if (showKiblat) {
                                    KiblatScreen(
                                        context = this@MainActivity,
                                        latitude = uiState.latitude,
                                        longitude = uiState.longitude,
                                        locationName = uiState.locationAddress,
                                        onBack = { showKiblat = false }
                                    )
                                }

                                // Kalender overlay
                                if (showKalender) {
                                    KalenderScreen(onBack = { showKalender = false })
                                }

                                // Azan picker overlay (from Settings)
                                if (showAzanPicker) {
                                    AzanPickerScreen(
                                        azanPlayer = azanPlayer,
                                        onComplete = { showAzanPicker = false }
                                    )
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
        color = Color(0xFFFFFFFF),
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
                    targetValue = if (isSelected) Color(0xFF1B4D3E) else Color(0xFF9CA3AF),
                    label = "tabTint"
                )
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) Color(0xFFE8F5E9) else Color.Transparent,
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