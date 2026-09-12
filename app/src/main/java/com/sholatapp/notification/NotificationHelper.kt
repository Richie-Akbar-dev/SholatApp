package com.sholatapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.sholatapp.MainActivity

/**
 * Helper for creating and showing prayer time notifications.
 */
class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "prayer_time_channel"
        const val CHANNEL_NAME = "Waktu Sholat"
        const val CHANNEL_DESC = "Notifikasi waktu sholat dan pengingat azan"

        const val PREP_CHANNEL_ID = "prep_alarm_channel"
        const val PREP_CHANNEL_NAME = "Pengingat Sholat"
        const val PREP_CHANNEL_DESC = "Alarm 20 menit sebelum waktu sholat"

        const val SUNNAH_CHANNEL_ID = "sunnah_reminder_channel"
        const val SUNNAH_CHANNEL_NAME = "Pengingat Puasa Sunnah"
        const val SUNNAH_CHANNEL_DESC = "Pengingat malam sebelum puasa sunnah (Senin, Kamis, Ayyamul Bidh)"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * Create notification channels (required for Android 8.0+).
     */
    fun createNotificationChannel() {
        // Main prayer channel
        val mainChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CHANNEL_DESC
            enableVibration(true)
            vibrationPattern = longArrayOf(200, 300, 200, 300)
            setSound(null, null)
        }
        notificationManager.createNotificationChannel(mainChannel)

        // Prep alarm channel
        val prepChannel = NotificationChannel(
            PREP_CHANNEL_ID,
            PREP_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = PREP_CHANNEL_DESC
            enableVibration(true)
            vibrationPattern = longArrayOf(500, 200, 500, 200, 500, 200)
            setSound(null, null)
        }
        notificationManager.createNotificationChannel(prepChannel)

        // Sunnah fasting reminder channel (v2.7)
        val sunnahChannel = NotificationChannel(
            SUNNAH_CHANNEL_ID,
            SUNNAH_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = SUNNAH_CHANNEL_DESC
            enableVibration(true)
            setSound(null, null)
        }
        notificationManager.createNotificationChannel(sunnahChannel)
    }

    /**
     * Show a prayer time notification (azan time).
     */
    fun showPrayerNotification(
        prayerName: String,
        prayerTime: String,
        notificationId: Int
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Waktunya $prayerName!")
            .setContentText("Sudah masuk waktu $prayerName pukul $prayerTime. Segera laksanakan sholat.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Sudah masuk waktu $prayerName pukul $prayerTime.\nSegera laksanakan sholat.")
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    /**
     * Show a prep alarm notification (20 min before prayer).
     */
    fun showPrepNotification(
        prayerName: String,
        prayerTime: String,
        notificationId: Int
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, PREP_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Bangun! $prayerName dalam 20 menit")
            .setContentText("Waktu $prayerName pukul $prayerTime. Segera bersiap-siap dan berwudhu.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Waktu $prayerName pukul $prayerTime.\nSegera bersiap-siap dan berwudhu.")
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    /**
     * Notifikasi pengingat puasa sunnah (v2.7) — dikirim pukul 20:00
     * pada malam sebelum tanggal puasa sunnah.
     */
    fun showSunnahNotification(title: String, text: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            4001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, SUNNAH_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(4001, notification)
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }
}
