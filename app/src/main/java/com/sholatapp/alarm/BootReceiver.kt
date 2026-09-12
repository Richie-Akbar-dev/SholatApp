package com.sholatapp.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sholatapp.calculation.PrayerCalculator
import com.sholatapp.notification.NotificationHelper
import java.util.Calendar

/**
 * Reschedules prayer alarms after device reboot.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Create notification channel on boot
            val notificationHelper = NotificationHelper(context)
            notificationHelper.createNotificationChannel()

            // Reschedule alarms using saved location preferences
            val prefs = context.getSharedPreferences("sholat_prefs", Context.MODE_PRIVATE)
            val lat = prefs.getFloat("last_latitude", 0f).toDouble()
            val lng = prefs.getFloat("last_longitude", 0f).toDouble()
            val locationName = prefs.getString("last_location_name", "") ?: ""

            if (lat != 0.0 || lng != 0.0) {
                val scheduler = AlarmScheduler(context)
                val schedule = PrayerCalculator.calculatePrayerTimes(
                    calendar = Calendar.getInstance(),
                    latitude = lat,
                    longitude = lng,
                    locationName = locationName
                )
                scheduler.schedulePrayerAlarms(schedule)
            }

            // Pengingat puasa sunnah juga disetel ulang setelah reboot (v2.7)
            if (prefs.getBoolean("sunnah_reminder_enabled", true)) {
                AlarmScheduler(context).scheduleSunnahReminder()
            }
        }
    }
}