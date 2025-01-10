package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.data.alarmManager.AlarmNotification
import aeb.proyecto.habittracker.data.model.notification.NotificationWithName
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import java.util.Calendar

const val REMINDER = "REMINDER"
val interval = 1000L * 60L * 3L //24L * 60L * 60L * 1000L // 24 horas en milisegundos

@SuppressLint("ScheduleExactAlarm", "MissingPermission")
fun setUpAlarm(context: Context, alarmItem: NotificationWithName, repeated: Boolean = false) {

    var calendar = Calendar.getInstance().timeInMillis + (interval)

    if (!repeated) {
        calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarmItem.notification.hour)
            set(Calendar.MINUTE, alarmItem.notification.minute)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        if(calendar < System.currentTimeMillis()) calendar += interval
    }

    val intent = Intent(context, AlarmNotification::class.java).apply {
        putExtra(REMINDER, Gson().toJson(alarmItem))
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        alarmItem.notification.id.toInt(),
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar,
        pendingIntent
    )
}

fun cancelAlarm(context: Context, id: Long) {
    val intent = Intent(context, AlarmNotification::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        id.toInt(),
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}