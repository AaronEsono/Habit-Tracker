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
            is TypeNotification.Daily -> {
                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)

                val isTodayValid = (alarmItem.typeNotification as TypeNotification.Daily).days.contains(
                    getAdjustedDayOfWeek()
                )

                val isTimeValid = (alarmItem.time.hour > currentHour) ||
                        (alarmItem.time.hour == currentHour && alarmItem.time.minute > currentMinute)

                if (isTodayValid && isTimeValid) {
                    timeInMillis = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, alarmItem.time.hour)
                        set(Calendar.MINUTE, alarmItem.time.minute)
                        set(Calendar.SECOND, 0)
                    }.timeInMillis
                } else {
                    val nextDay = getNextDay(
                        (alarmItem.typeNotification as TypeNotification.Daily).days,
                        getAdjustedDayOfWeek()
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
                    getAdjustedDayOfWeek()
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

fun getNextDay(list:List<Int>, dayOfTheWeek:Int):Int{
    // Ordenamos la lista de días de la semana
    val sortedList = list.sorted()

    // Buscamos el siguiente día más cercano
    for (day in sortedList) {
        if (day > dayOfTheWeek) {
            return day - dayOfTheWeek
        }
    }

    // Si no hay un día mayor, tomamos el primero de la lista y contamos los días hasta la próxima semana
    return (7 - dayOfTheWeek) + sortedList.first()
}

fun getAdjustedDayOfWeek(): Int {
    val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    return if (day == Calendar.SUNDAY) 7 else day - 1
}