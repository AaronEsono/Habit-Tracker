package aeb.proyecto.habittracker.data.model.notification

import aeb.proyecto.habittracker.MainActivity
import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.di.CHANNEL
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

const val NOTIFICATION_ID = 1

class AlarmNotification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        createNotification(context)
    }

    private fun createNotification(context: Context){

        if (ContextCompat.checkSelfPermission(
                context,
                POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ){
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val flag = PendingIntent.FLAG_IMMUTABLE
            val pendingIntent = PendingIntent.getActivity(context,1,intent,flag)

            val notification = NotificationCompat.Builder(context, CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Medication Reminder")
                .setContentText("wrerogfewjofoje joeojf eoj")
                .setContentIntent(pendingIntent)
                .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            manager.notify(NOTIFICATION_ID,notification)
        }

    }

}
