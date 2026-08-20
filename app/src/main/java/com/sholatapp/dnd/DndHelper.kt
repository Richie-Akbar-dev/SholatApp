package com.sholatapp.dnd

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

/**
 * Helper untuk mengaktifkan mode DND (Do Not Disturb)
 * selama waktu sholat agar pengguna bisa fokus beribadah.
 *
 * Membutuhkan permission ACCESS_NOTIFICATION_POLICY yang diberikan
 * melalui Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS.
 */
object DndHelper {

    /**
 * Cek apakah app punya akses untuk mengontrol DND.
 * Menggunakan NotificationManager.isNotificationPolicyAccessGranted()
 * yang merupakan API resmi untuk kontrol interruption filter.
 */
    fun hasDndPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.isNotificationPolicyAccessGranted
        } else {
            false
        }
    }

    /**
 * Buka halaman pengaturan sistem agar user memberikan izin DND.
 */
    fun requestDndPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    /**
 * Aktifkan DND mode — hanya notifikasi prioritas yang diperbolehkan.
 * Dipanggil saat alarm sholat menyala agar pengguna fokus ibadah.
 */
    fun enableDnd(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && hasDndPermission(context)) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (nm.currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_PRIORITY) {
                nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
            }
        }
    }

    /**
 * Nonaktifkan DND — kembalikan semua notifikasi seperti semula.
 * Dipanggil setelah durasi sholat berakhir (default 30 menit).
 */
    fun disableDnd(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && hasDndPermission(context)) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (nm.currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_ALL) {
                nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            }
        }
    }
}