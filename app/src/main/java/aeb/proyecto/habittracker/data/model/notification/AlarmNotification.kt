package aeb.proyecto.habittracker.data.model.notification

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.di.CHANNEL
import aeb.proyecto.habittracker.utils.REMINDER
import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson

class AlarmNotification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderJson = intent.getStringExtra(REMINDER)
        val reminder = Gson().fromJson(reminderJson, Notification::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

                Log.d("entro", "entro")
                val notification = NotificationCompat.Builder(context, CHANNEL)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Habit Tracker")
                    .setContentText("Es hora de")
                    .build()

                NotificationManagerCompat.from(context)
                    .notify(1, notification)
            }
        }else{
            val notification = NotificationCompat.Builder(context, CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Habit Tracker")
                .setContentText("Es hora de")
                .build()

            NotificationManagerCompat.from(context)
                .notify(1, notification)
        }
    }
}