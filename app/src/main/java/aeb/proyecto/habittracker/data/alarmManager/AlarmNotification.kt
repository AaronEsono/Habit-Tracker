package aeb.proyecto.habittracker.data.alarmManager

import aeb.proyecto.habittracker.MainActivity
import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.application.CHANNEL
import aeb.proyecto.habittracker.utils.REMINDER
import aeb.proyecto.habittracker.utils.setUpAlarm
import aeb.proyecto.room.model.NotificationWithNameAndColor
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson

const val NOTIFICATION_ID = 1

class AlarmNotification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        createNotification(context,intent)
    }

    private fun createNotification(context: Context, intent2: Intent){
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val notificationWithName = intent2.getStringExtra(REMINDER)
        val data = Gson().fromJson(notificationWithName, NotificationWithNameAndColor::class.java)

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

        setUpAlarm(context,data,true)
    }
}
