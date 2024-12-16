package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.model.notification.AlarmNotification
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

const val REMINDER = "REMINDER"
val interval = 24L * 60L * 60L * 1000L // 24 horas en milisegundos

fun setUpAlarm(context: Context, alarmItem: Notification){
    val intent = Intent(context, AlarmNotification::class.java).apply {
        putExtra(REMINDER, Gson().toJson(alarmItem))
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, alarmItem.timeInMillis.toInt(),
        intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    try {
        val interval = 2L * 60L * 1000L
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmItem.timeInMillis,
            interval,
            pendingIntent
        )
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}