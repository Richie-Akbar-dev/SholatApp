package com.sholatapp.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sholatapp.calculation.PrayerCalculator
import java.util.Calendar

/**
 * Fires at 00:01 every day to reschedule all prayer alarms for the new day.
 */
class MidnightRescheduleReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_LATITUDE = "latitude"
        const val EXTRA_LONGITUDE = "longitude"
        const val EXTRA_LOCATION_NAME = "location_name"
        const val REQUEST_CODE_MIDNIGHT = 9999
    }

    override fun onReceive(context: Context, intent: Intent) {
        val lat = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0)
        val lng = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0)
        val name = intent.getStringExtra(EXTRA_LOCATION_NAME) ?: ""

        if (lat == 0.0 && lng == 0.0) return

        val scheduler = AlarmScheduler(context)
        val today = Calendar.getInstance()
        val schedule = PrayerCalculator.calculatePrayerTimes(today, lat, lng, name)
        scheduler.schedulePrayerAlarms(schedule)

        // Schedule tomorrow's midnight reschedule
        scheduleMidnightReschedule(context, lat, lng, name)
    }

    /**
     * Schedule the midnight rescheduler for tomorrow at 00:01.
     */
    fun scheduleMidnightReschedule(
        context: Context,
        latitude: Double,
        longitude: Double,
        locationName: String
    ) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 1)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val intent = Intent(context, MidnightRescheduleReceiver::class.java).apply {
            putExtra(EXTRA_LATITUDE, latitude)
            putExtra(EXTRA_LONGITUDE, longitude)
            putExtra(EXTRA_LOCATION_NAME, locationName)
        }

        val pendingIntent = android.app.PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MIDNIGHT,
            intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            android.app.AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}