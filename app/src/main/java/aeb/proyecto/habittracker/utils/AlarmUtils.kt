package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.data.model.notification.AlarmItem
import aeb.proyecto.habittracker.data.model.notification.AlarmNotification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.gson.Gson

fun setUpAlarm(context: Context, alarmItem: AlarmItem){

    val intent = Intent(context, AlarmNotification::class.java).apply {
        putExtra("name", Gson().toJson(alarmItem))
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        alarmItem.timeMilimeters.toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
    try {
        val interval = 2L * 60L * 1000L
        alarmManager.setRepeating(
            android.app.AlarmManager.RTC_WAKEUP,
            alarmItem.timeMilimeters,
            interval,
            pendingIntent
        )
    }catch (e:Exception){
        e.printStackTrace()
    }

}

fun cancelAlarm(context: Context, alarmItem: AlarmItem){

    val intent = Intent(context, AlarmNotification::class.java).apply {
        putExtra("name",Gson().toJson(alarmItem))
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        alarmItem.timeMilimeters.toInt(),
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