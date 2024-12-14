package aeb.proyecto.habittracker.data.model.notification

import aeb.proyecto.habittracker.MainActivity
import aeb.proyecto.habittracker.R
import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson

class AlarmNotification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderJson = intent.getStringExtra("name")
        val reminder = Gson().fromJson(reminderJson, AlarmItem::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

                val doneIntent = Intent(context, AlarmNotification::class.java).apply {
                    putExtra("name", reminderJson)
                    action = "REJECT"
                }

                val donePendingIntent = PendingIntent.getBroadcast(
                    context,
                    reminder.timeMilimeters.toInt(),
                    doneIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val closeIntent = Intent(context, AlarmNotification::class.java).apply {
                    putExtra("name", reminderJson)
                    action = "REJECT"
                }

                val closePendingIntent = PendingIntent.getBroadcast(
                    context,
                    reminder.timeMilimeters.toInt(),
                    closeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val notification = NotificationCompat.Builder(context, "habit_tracker")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Habit Tracker")
                    .setContentText("Es hora de")
                    .build()

                NotificationManagerCompat.from(context)
                    .notify(reminder.timeMilimeters.toInt(), notification)
            }
        }else{
            val notification = NotificationCompat.Builder(context, "habit_tracker")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Habit Tracker")
                .setContentText("Es hora de")
                .build()

            NotificationManagerCompat.from(context)
                .notify(reminder.timeMilimeters.toInt(), notification)
        }
    }
}