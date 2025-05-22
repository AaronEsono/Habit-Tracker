package aeb.proyecto.stopwatch.notification

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.constants.NOTIFICATION_CHANNEL_ID
import aeb.proyecto.stopwatch.helper.StopWatchHelper
import android.content.Context
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationBuilderHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stopWatchHelper: StopWatchHelper,
    private val notificationBuilder: NotificationCompat.Builder
) {

    fun createNotification(): NotificationCompat.Builder {
        return notificationBuilder.setWhen(System.currentTimeMillis())
    }

    fun updateNotification(
        newTitle:String,
        newTime: String,
        showStop: Boolean = false,
        showResume: Boolean = false,
        showCancel: Boolean = false,
        showFinish: Boolean = false
    ): NotificationCompat.Builder {
        notificationBuilder.setContentTitle(newTitle)
        notificationBuilder.setContentText(newTime)
        notificationBuilder.clearActions()

        if (showStop) {
            notificationBuilder.addAction(
                NotificationCompat.Action.Builder(
                    0,
                    context.getString(R.string.service_stop),
                    stopWatchHelper.stopPendingIntent()
                ).build()
            )
        }

        if (showResume) {
            notificationBuilder.addAction(
                NotificationCompat.Action.Builder(
                    0,
                    context.getString(R.string.service_resume),
                    stopWatchHelper.resumePendingIntent()
                ).build()
            )
        }

        if (showCancel) {
            notificationBuilder.addAction(
                NotificationCompat.Action.Builder(
                    0,
                    context.getString(R.string.service_cancel),
                    stopWatchHelper.cancelPendingIntent()
                ).build()
            )
        }

        if (showFinish) {
            notificationBuilder.addAction(
                NotificationCompat.Action.Builder(
                    0,
                    context.getString(R.string.service_finish),
                    stopWatchHelper.finishPendingIntent()
                ).build()
            )
        }

        return notificationBuilder
    }
}