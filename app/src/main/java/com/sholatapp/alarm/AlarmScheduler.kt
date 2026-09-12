package com.sholatapp.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.sholatapp.calculation.SunnahDayCalculator
import com.sholatapp.model.PrayerInfo
import com.sholatapp.model.PrayerSchedule
import java.util.Calendar

/**
 * Schedules exact alarms for each prayer time.
 * Also schedules prep-alarams (20 min before) and a daily midnight rescheduler.
 */
class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val prefs = context.getSharedPreferences("sholat_prefs", Context.MODE_PRIVATE)
    private val PREP_MINUTES = 20

    /**
     * Schedule alarms for all 5 daily prayers + prep-alarms + midnight rescheduler.
     */
    fun schedulePrayerAlarms(schedule: PrayerSchedule) {
        cancelAllAlarms()

        val prepEnabled = prefs.getBoolean("prep_alarm_enabled", true)

        schedule.prayerList.forEach { prayer ->
            // Main prayer alarm (azan)
            schedulePrayerAlarm(prayer, schedule.locationName)

            // Prep alarm (20 min before) to wake the user
            if (prepEnabled) {
                schedulePrepAlarm(prayer, schedule.locationName)
            }
        }

        // Schedule midnight reschedule for the next day
        MidnightRescheduleReceiver().scheduleMidnightReschedule(
            context,
            schedule.latitude,
            schedule.longitude,
            schedule.locationName
        )
    }

    /**
     * Schedule the main prayer alarm (azan time).
     */
    private fun schedulePrayerAlarm(prayer: PrayerInfo, locationName: String) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, prayer.hour)
            set(Calendar.MINUTE, prayer.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            putExtra(EXTRA_PRAYER_NAME, prayer.name)
            putExtra(EXTRA_PRAYER_TIME, prayer.timeString)
            putExtra(EXTRA_LOCATION, locationName)
            putExtra(EXTRA_IS_PREP, false)
        }

        val requestCode = getRequestCode(prayer.nameKey)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    /**
     * Schedule a prep alarm 20 minutes before the prayer.
     */
    private fun schedulePrepAlarm(prayer: PrayerInfo, locationName: String) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, prayer.hour)
            set(Calendar.MINUTE, prayer.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.MINUTE, -PREP_MINUTES)
            // If prep time already passed, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            putExtra(EXTRA_PRAYER_NAME, prayer.name)
            putExtra(EXTRA_PRAYER_TIME, prayer.timeString)
            putExtra(EXTRA_LOCATION, locationName)
            putExtra(EXTRA_IS_PREP, true)
        }

        val requestCode = getRequestCode(prayer.nameKey, isPrep = true)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    /**
     * Cancel all prayer alarms (main + prep) including midnight rescheduler.
     */
    fun cancelAllAlarms() {
        val prayerKeys = listOf(
            PrayerInfo.FAJR,
            PrayerInfo.DHUHR,
            PrayerInfo.ASR,
            PrayerInfo.MAGHRIB,
            PrayerInfo.ISHA
        )

        prayerKeys.forEach { key ->
            // Cancel main alarm
            cancelAlarm(key, isPrep = false)
            // Cancel prep alarm
            cancelAlarm(key, isPrep = true)
        }

        // Cancel midnight rescheduler
        val midnightIntent = Intent(context, MidnightRescheduleReceiver::class.java)
        val midnightPi = PendingIntent.getBroadcast(
            context,
            MidnightRescheduleReceiver.REQUEST_CODE_MIDNIGHT,
            midnightIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        midnightPi?.let { alarmManager.cancel(it) }
    }

    private fun cancelAlarm(prayerKey: String, isPrep: Boolean) {
        val intent = Intent(context, PrayerAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            getRequestCode(prayerKey, isPrep),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let { alarmManager.cancel(it) }
    }

    /**
     * Jadwalkan pengingat puasa sunnah (v2.7): alarm pukul 20:00 pada malam
     * sebelum tanggal puasa sunnah berikutnya (Senin/Kamis/Ayyamul Bidh).
     * [baseAfter] = titik mulai pindai (receiver mengirim target yang baru
     * diberitahu agar rantai tidak mengulang hari yang sama).
     * Bila momen alarm terdekat sudah lewat, otomatis lanjut ke kejadian berikutnya.
     */
    fun scheduleSunnahReminder(baseAfter: Calendar = Calendar.getInstance()) {
        cancelSunnahReminder()
        if (!prefs.getBoolean("sunnah_reminder_enabled", true)) return

        var base = baseAfter.clone() as Calendar
        for (attempt in 1..45) {
            val target = SunnahDayCalculator.nextSunnahAfter(base) ?: return
            val alarmCal = (target.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, -1)
                set(Calendar.HOUR_OF_DAY, 20)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            if (alarmCal.timeInMillis > System.currentTimeMillis()) {
                val isBidh = SunnahDayCalculator.isAyyamulBidh(
                    target.get(Calendar.YEAR),
                    target.get(Calendar.MONTH),
                    target.get(Calendar.DAY_OF_MONTH)
                )
                val label = if (isBidh) "Puasa Ayyamul Bidh"
                else "Puasa Sunnah " + SunnahDayCalculator.weekdayFull(
                    target.get(Calendar.YEAR),
                    target.get(Calendar.MONTH),
                    target.get(Calendar.DAY_OF_MONTH)
                )
                val intent = Intent(context, SunnahReminderReceiver::class.java).apply {
                    putExtra(SunnahReminderReceiver.EXTRA_SUNNAH_LABEL, label)
                    putExtra(SunnahReminderReceiver.EXTRA_SUNNAH_TARGET_MILLIS, target.timeInMillis)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    REQUEST_CODE_SUNNAH,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmCal.timeInMillis,
                    pendingIntent
                )
                return
            }
            base = target
        }
    }

    /** Batalkan alarm pengingat puasa sunnah. */
    fun cancelSunnahReminder() {
        val intent = Intent(context, SunnahReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_SUNNAH,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let { alarmManager.cancel(it) }
    }

    fun rescheduleAlarms(schedule: PrayerSchedule) {
        schedulePrayerAlarms(schedule)
    }

    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun getRequestCode(prayerKey: String, isPrep: Boolean = false): Int {
        val base = when (prayerKey) {
            PrayerInfo.FAJR -> 1001
            PrayerInfo.DHUHR -> 1002
            PrayerInfo.ASR -> 1003
            PrayerInfo.MAGHRIB -> 1004
            PrayerInfo.ISHA -> 1005
            else -> 1000
        }
        return if (isPrep) base + 1000 else base // Prep alarms: 2001-2005
    }

    companion object {
        const val EXTRA_PRAYER_NAME = "prayer_name"
        const val EXTRA_PRAYER_TIME = "prayer_time"
        const val EXTRA_LOCATION = "location"
        const val EXTRA_IS_PREP = "is_prep_alarm"
        private const val REQUEST_CODE_SUNNAH = 3001
    }
}
