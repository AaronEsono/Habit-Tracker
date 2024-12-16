package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.MainActivity
import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.model.notification.AlarmNotification
import aeb.proyecto.habittracker.data.model.notification.NOTIFICATION_ID
import aeb.proyecto.habittracker.di.CHANNEL
import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

const val REMINDER = "REMINDER"
val interval = 24L * 60L * 60L * 1000L // 24 horas en milisegundos

@SuppressLint("ScheduleExactAlarm", "MissingPermission")
fun setUpAlarm(context: Context, alarmItem: Notification,name:String) {

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, alarmItem.hour)
        set(Calendar.MINUTE, alarmItem.minute)
        set(Calendar.SECOND, 0)
    }.timeInMillis

    val intent = Intent(context, AlarmNotification::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        NOTIFICATION_ID,
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