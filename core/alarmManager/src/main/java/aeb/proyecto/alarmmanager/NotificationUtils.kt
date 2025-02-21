package aeb.proyecto.alarmmanager

import aeb.proyecto.alarmmanager.service.AlarmService
import aeb.proyecto.room.model.NotificationWithNameAndColor
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

const val REMINDER = "REMINDER"
val interval = 24L * 60L * 60L * 1000L // 24 horas en milisegundos
val debug = 1000L * 5L * 60L // 5 Minutos

@Singleton
class NotificationUtils @Inject constructor(
    @ApplicationContext private val context: Context
){
    fun setUpAlarm(alarmItem: NotificationWithNameAndColor, repeated: Boolean = false) {

        var setTime = Calendar.getInstance().timeInMillis + (interval)

        if (!repeated) {
            setTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarmItem.hour)
                set(Calendar.MINUTE, alarmItem.minute)
                set(Calendar.SECOND, 0)
            }.timeInMillis

            if(setTime < System.currentTimeMillis()) setTime += interval
        }

        val intent = Intent(context, AlarmService::class.java).apply {
            putExtra(REMINDER, Gson().toJson(alarmItem))
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmItem.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            setTime,
            pendingIntent
        )
    }

    fun cancelAlarm(id: Long) {
        val intent = Intent(context, AlarmService::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}