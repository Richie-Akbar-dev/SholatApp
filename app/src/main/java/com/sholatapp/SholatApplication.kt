package com.sholatapp

import android.app.Application
import com.sholatapp.notification.NotificationHelper

class SholatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Create notification channel on app start
        NotificationHelper(this).createNotificationChannel()
    }
}
