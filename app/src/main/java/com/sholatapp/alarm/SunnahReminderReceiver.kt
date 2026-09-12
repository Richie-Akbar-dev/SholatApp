package com.sholatapp.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sholatapp.notification.NotificationHelper
import java.util.Calendar

/**
 * Pengingat puasa sunnah (v2.7).
 * Dijadwalkan pukul 20:00 pada MALAM SEBELUM tanggal puasa sunnah berikutnya
 * (Senin/Kamis/Ayyamul Bidh). Setelah menampilkan notifikasi, receiver
 * menjadwalkan sendiri siklus berikutnya (rantai mandiri) sehingga tidak
 * bergantung pada jadwal sholat maupun lokasi.
 */
class SunnahReminderReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_SUNNAH_LABEL = "sunnah_label"
        const val EXTRA_SUNNAH_TARGET_MILLIS = "sunnah_target_millis"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences("sholat_prefs", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("sunnah_reminder_enabled", true)) return

        val label = intent.getStringExtra(EXTRA_SUNNAH_LABEL) ?: "Puasa Sunnah"
        NotificationHelper(context).showSunnahNotification(
            title = "Besok $label",
            text = "Tanamkan niat malam ini dan jangan lupa sahur. Semoga puasamu diterima Allah."
        )

        // Rantai berikutnya: pindai kejadian sunnah SETELAH target yang baru dinotifikasikan
        val targetMillis = intent.getLongExtra(EXTRA_SUNNAH_TARGET_MILLIS, 0L)
        val base = Calendar.getInstance()
        if (targetMillis > 0L) base.timeInMillis = targetMillis
        AlarmScheduler(context).scheduleSunnahReminder(baseAfter = base)
    }
}
