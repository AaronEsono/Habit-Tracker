package aeb.proyecto.habittracker.data.model.notification

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.di.CHANNEL
import aeb.proyecto.habittracker.utils.REMINDER
import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

class AlarmNotification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderJson = intent.getStringExtra(REMINDER)
        val reminder = Gson().fromJson(reminderJson, Notification::class.java)

        val doneIntent = Intent(context, AlarmNotification::class.java).apply {
            putExtra(REMINDER, reminderJson)
        }
        val donePendingIntent = PendingIntent.getBroadcast(
            context, reminder.timeInMillis.toInt(), doneIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val closeIntent = Intent(context, AlarmNotification::class.java).apply {
            putExtra(REMINDER, reminderJson)
        }
        val closePendingIntent = PendingIntent.getBroadcast(
            context, reminder.timeInMillis.toInt(), closeIntent, PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val notification = NotificationCompat.Builder(context, CHANNEL)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Medication Reminder")
                    .setContentText("wrerogfewjofoje joeojf eoj")
                    .build()

                NotificationManagerCompat.from(context)
                    .notify(1, notification)
            }
        } else {
            val notification = NotificationCompat.Builder(context, CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Medication Reminder")
                .setContentText("wrerogfewjofoje joeojf eoj")
                .build()

            NotificationManagerCompat.from(context)
                .notify(1, notification)

        }

    }
}
