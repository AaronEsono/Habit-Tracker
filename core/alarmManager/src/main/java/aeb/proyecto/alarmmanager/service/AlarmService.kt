package aeb.proyecto.alarmmanager.service

import aeb.proyecto.alarmmanager.NotificationUtils
import aeb.proyecto.alarmmanager.R
import aeb.proyecto.alarmmanager.REMINDER
import aeb.proyecto.alarmmanager.constants.CHANNEL
import aeb.proyecto.alarmmanager.converters.LocalTimeAdapter
import aeb.proyecto.alarmmanager.converters.TypeNotificationAdapter
import aeb.proyecto.alarmmanager.gsonProvider.GsonProvider
import aeb.proyecto.room.converters.DateConverter
import aeb.proyecto.room.converters.IconConverter
import aeb.proyecto.room.converters.TypeNotificationConverter
import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.model.classes.TypeNotification
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import javax.inject.Inject
import androidx.core.net.toUri
import com.google.android.libraries.places.api.model.LocalTime
import com.google.gson.GsonBuilder


class AlarmService : BroadcastReceiver() {

    @Inject
    lateinit var notificationUtils: NotificationUtils

    override fun onReceive(context: Context, intent: Intent) {
        createNotification(context,intent)
    }

    private fun createNotification(context: Context, intent2: Intent){
        notificationUtils = NotificationUtils(context)

        val intent = Intent(Intent.ACTION_VIEW, "app://main".toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val notificationWithName = intent2.getStringExtra(REMINDER)

        val data = GsonProvider.gson
            .fromJson(notificationWithName, NotificationWithNameAndColor::class.java)

        if (ContextCompat.checkSelfPermission(
                context,
                POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ){

            val flag = PendingIntent.FLAG_IMMUTABLE
            val pendingIntent = PendingIntent.getActivity(context,data.id.toInt(),intent,flag)

            val color = Color(data.color)

            val notification = NotificationCompat.Builder(context, CHANNEL)
                .setSmallIcon(R.drawable.ic_achievement)
                .setColor(color.toArgb())
                .setContentTitle(data.name)
                .setContentText(context.getString(R.string.notification_subtitle,data.name))
                .setContentIntent(pendingIntent)
                .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            manager.notify(data.id.toInt(),notification)
        }

        notificationUtils.setRepeatedAlarm(data)
    }
}
