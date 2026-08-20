package com.sholatapp.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sholatapp.azan.AzanPlayer
import com.sholatapp.dnd.DndHelper
import com.sholatapp.notification.NotificationHelper

/**
 * Receiver yang menangani alarm sholat (utama & persiapan).
 *
 * Saat alarm utama menyala:
 * 1. Tampilkan notifikasi sholat
 * 2. Putar azan jika tersedia
 * 3. Aktifkan DND jika fitur Focus Mode diaktifkan di Settings
 * 4. Jadwalkan auto-disable DND setelah 30 menit
 */
class PrayerAlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val DND_DURATION_MS = 30 * 60 * 1000L // 30 menit
        private const val ACTION_DISABLE_DND = "com.sholatapp.action.DISABLE_DND"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Handle DND auto-disable callback
        if (ACTION_DISABLE_DND == intent.action) {
            DndHelper.disableDnd(context)
            return
        }

        val prayerName = intent.getStringExtra(AlarmScheduler.EXTRA_PRAYER_NAME) ?: return
        val prayerTime = intent.getStringExtra(AlarmScheduler.EXTRA_PRAYER_TIME) ?: return
        val isPrep = intent.getBooleanExtra(AlarmScheduler.EXTRA_IS_PREP, false)
        val notificationId = getNotificationId(prayerName, isPrep)

        val notificationHelper = NotificationHelper(context)
        notificationHelper.createNotificationChannel()

        if (isPrep) {
            // Prep alarm: 20 min before - wake up notification
            notificationHelper.showPrepNotification(
                prayerName = prayerName,
                prayerTime = prayerTime,
                notificationId = notificationId
            )
        } else {
            // Main alarm: azan time
            notificationHelper.showPrayerNotification(
                prayerName = prayerName,
                prayerTime = prayerTime,
                notificationId = notificationId
            )
            // Play azan audio if available
            try {
                val azanPlayer = AzanPlayer(context)
                if (azanPlayer.isAzanAvailable()) {
                    azanPlayer.playAzan()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // DND Focus Mode: aktifkan DND jika diaktifkan di settings
            val prefs = context.getSharedPreferences("sholat_prefs", Context.MODE_PRIVATE)
            val isDndEnabled = prefs.getBoolean("dnd_enabled", false)
            if (isDndEnabled) {
                DndHelper.enableDnd(context)
                scheduleDndDisable(context)
            }
        }
    }

    /**
     * Jadwalkan auto-disable DND setelah DND_DURATION_MS (30 menit).
     * Menggunakan AlarmManager agar tetap berjalan meskipun app di-background.
     */
    private fun scheduleDndDisable(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = ACTION_DISABLE_DND
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            999, // khusus untuk DND disable
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerAt = System.currentTimeMillis() + DND_DURATION_MS

        // Gunakan setAndAllowWhileIdle agar tetap jalan saat Doze
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        }
    }

    private fun getNotificationId(prayerName: String, isPrep: Boolean): Int {
        val base = when (prayerName) {
            "Subuh" -> 1
            "Dzuhur" -> 2
            "Ashar" -> 3
            "Maghrib" -> 4
            "Isya" -> 5
            else -> 0
        }
        return if (isPrep) base + 100 else base + 200
    }
}
