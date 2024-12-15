package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.data.entities.Notification
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

    val intent = Intent(context, Notification::class.java).apply {
        putExtra(REMINDER, Gson().toJson(alarmItem))
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        3463565,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    try {
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().epochSecond + 10,
            2L * 60L * 1000L,
            pendingIntent
        )
    }catch (e:Exception){
        e.printStackTrace()
    }

}

fun cancelAlarm(context: Context, alarmItem: Notification){

    val intent = Intent(context, Notification::class.java).apply {
        putExtra("name",Gson().toJson(alarmItem))
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
    try {
        alarmManager.cancel(pendingIntent)
    }catch (e:Exception){
        e.printStackTrace()
    }

}