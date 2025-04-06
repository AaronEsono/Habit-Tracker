package aeb.proyecto.alarmmanager

import aeb.proyecto.alarmmanager.gsonProvider.GsonProvider
import aeb.proyecto.alarmmanager.service.AlarmService
import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.model.classes.TypeNotification
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalDate
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
    fun setUpAlarm(alarmItem: NotificationWithNameAndColor) {

        var timeInMillis:Long

        when(alarmItem.typeNotification){
            //Si es diaria, comprobamos si tenemos que setear alarma para hoy mismo
            is TypeNotification.Daily -> {
                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)

                val isTodayValid = (alarmItem.typeNotification as TypeNotification.Daily)
                    .days.contains(LocalDate.now().dayOfWeek)

                val isTimeValid = (alarmItem.time.hour > currentHour) ||
                        (alarmItem.time.hour == currentHour && alarmItem.time.minute > currentMinute)

                // Si el dia esta en la lista y si no ha pasado la hora
                if (isTodayValid && isTimeValid) {
                    timeInMillis = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, alarmItem.time.hour)
                        set(Calendar.MINUTE, alarmItem.time.minute)
                        set(Calendar.SECOND, 0)
                    }.timeInMillis
                } else {
                    //Si no, buscamos el siguiente dia que sea valido
                    val nextDay = getNextDay(
                        (alarmItem.typeNotification as TypeNotification.Daily).days,
                        LocalDate.now().dayOfWeek
                    )

                    timeInMillis = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, alarmItem.time.hour)
                        set(Calendar.MINUTE, alarmItem.time.minute)
                        set(Calendar.SECOND, 0)
                    }.timeInMillis + (interval * nextDay)
                }

            }

            is TypeNotification.Recurring -> {

                val intervalDays = (alarmItem.typeNotification as TypeNotification.Recurring).interval

                timeInMillis = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, alarmItem.time.hour)
                    set(Calendar.MINUTE, alarmItem.time.minute)
                    set(Calendar.SECOND, 0)
                }.timeInMillis

                if(timeInMillis < System.currentTimeMillis()) timeInMillis += (interval * intervalDays)
            }
        }

        val intent = Intent(context, AlarmService::class.java).apply {
            putExtra(REMINDER, GsonProvider.gson.toJson(alarmItem))
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
            timeInMillis,
            pendingIntent
        )
    }

    fun setRepeatedAlarm(alarmItem: NotificationWithNameAndColor) {

        val intervalDays: Int = when (alarmItem.typeNotification) {
            is TypeNotification.Daily -> {
                getNextDay(
                    (alarmItem.typeNotification as TypeNotification.Daily).days,
                    LocalDate.now().dayOfWeek
                )
            }

            is TypeNotification.Recurring -> {
                (alarmItem.typeNotification as TypeNotification.Recurring).interval
            }
        }

        val timeInMillis = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarmItem.time.hour)
            set(Calendar.MINUTE, alarmItem.time.minute)
            set(Calendar.SECOND, 0)
        }.timeInMillis + (interval * intervalDays)

        val intent = Intent(context, AlarmService::class.java).apply {
            putExtra(REMINDER, GsonProvider.gson.toJson(alarmItem))
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
            timeInMillis,
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

fun getNextDay(list: List<DayOfWeek>, today: DayOfWeek): Int {
    val sorted = list.sortedBy { it.value }
    for (day in sorted) {
        if (day.value > today.value) {
            return day.value - today.value
        }
    }
    // Si no hay uno mayor, volvemos al primero la siguiente semana
    return (7 - today.value) + sorted.first().value
}