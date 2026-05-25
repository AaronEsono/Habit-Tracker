package aeb.proyecto.habittracker.application

import aeb.proyecto.alarmmanager.constants.CHANNEL
import aeb.proyecto.alarmmanager.constants.NAME
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp


/**
 * The primary [Application] entry point for the entire lifecycle process.
 *
 * This class triggers the compile-time generation of the Dagger Hilt dependency injection
 * graph via [@HiltAndroidApp] and acts as the baseline initialization hub.
 * Centralized, application-wide system configurations—such as notification frameworks—are
 * established here before any UI components or background workers are instantiated.
 */
@HiltAndroidApp
class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    /**
     * Initializes and registers the primary application [NotificationChannel] with the system
     * [NotificationManager].
     * * Starting with Android 8.0 (API level 26), all notifications must be assigned to a channel.
     * This function runs an itemized idempotency check to verify if the targeted channel ID already
     * exists; if missing, it safe-registers the channel to enable local and scheduled background reminder
     * alerts seamlessly.
     */
    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL, NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager.getNotificationChannel(CHANNEL) == null){
            notificationManager.createNotificationChannel(channel)
        }
    }
}