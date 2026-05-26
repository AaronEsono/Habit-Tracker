package aeb.proyecto.alarmmanager.service

import aeb.proyecto.alarmmanager.NotificationUtils
import aeb.proyecto.alarmmanager.R
import aeb.proyecto.alarmmanager.constants.CHANNEL
import aeb.proyecto.alarmmanager.constants.REMINDER
import aeb.proyecto.alarmmanager.gsonProvider.GsonProvider
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
import androidx.core.net.toUri
import javax.inject.Inject


/**
 * An asynchronous background platform interceptor responsible for handling scheduled hardware alarm triggers.
 *
 * Annotated with [@AndroidEntryPoint] to enable Hilt dependency provisioning within non-component framework units,
 * this [BroadcastReceiver] wakes up responsively from background context pipelines when an alert boundary conditions are met.
 *
 * The execution sequence pipeline performs three critical tasks:
 * 1. Reconstructs serialized metadata payloads using [GsonProvider].
 * 2. Validates Android runtime notification safety permission constraints (`POST_NOTIFICATIONS`).
 * 3. Builds and pushes a rich platform alert styled dynamically with the target habit's color profile,
 * wrapping an internal routing deep link (`app://main`) to manage explicit navigation resumption parameters.
 *
 * Finally, it delegates execution backward to [NotificationUtils.setRepeatedAlarm] to ensure uninterrupted
 * chronological scheduling perpetuity.
 */
class AlarmService : BroadcastReceiver() {

    @Inject
    lateinit var notificationUtils: NotificationUtils

    override fun onReceive(context: Context, intent: Intent) {
        createNotification(context,intent)
    }

    private fun createNotification(context: Context, incomingIntent: Intent){

        val deepLinkIntent = Intent(Intent.ACTION_VIEW, "app://main".toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val rawReminderJson = incomingIntent.getStringExtra(REMINDER)
        val data = GsonProvider.gson.fromJson(rawReminderJson, NotificationWithNameAndColor::class.java)

        if (ContextCompat.checkSelfPermission(
                context,
                POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ){

            val pendingIntentFlags = PendingIntent.FLAG_IMMUTABLE
            val pendingIntent = PendingIntent.getActivity(
                context,
                data.id.toInt(),
                deepLinkIntent,
                pendingIntentFlags
            )

            val habitCustomColor = Color(data.color)

            val notification = NotificationCompat.Builder(context, CHANNEL)
                .setSmallIcon(R.drawable.ic_achievement)
                .setColor(habitCustomColor.toArgb())
                .setContentTitle(data.name)
                .setContentText(context.getString(R.string.notification_subtitle, data.name))
                .setContentIntent(pendingIntent)
                .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            manager.notify(data.id.toInt(), notification)
        }

        notificationUtils.setRepeatedAlarm(data)
    }
}
