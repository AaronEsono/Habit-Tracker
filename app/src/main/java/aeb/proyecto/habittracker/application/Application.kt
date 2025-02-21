package aeb.proyecto.habittracker.application

import aeb.proyecto.alarmmanager.constants.CHANNEL
import aeb.proyecto.alarmmanager.constants.NAME
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL, NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager.getNotificationChannel(CHANNEL) == null){
            notificationManager.createNotificationChannel(channel)
        }
    }
}