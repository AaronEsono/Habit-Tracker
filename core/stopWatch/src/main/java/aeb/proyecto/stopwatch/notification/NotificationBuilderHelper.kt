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
    private val stopWatchHelper: StopWatchHelper
) {

    fun buildNotification(
        title: String = "Stopwatch",
        contentText: String = "00:00:00",
        showStop: Boolean = true,
        showCancel: Boolean = true,
        showFinish: Boolean = false,
        showResume: Boolean = false
    ): NotificationCompat.Builder {

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(contentText)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.ic_achievement)
            .setContentIntent(stopWatchHelper.clickPendingIntent())

        if (showStop) {
            builder.addAction(
                0,
                context.getString(R.string.service_stop),
                stopWatchHelper.stopPendingIntent()
            )
        }

        if (showResume) {
            builder.addAction(
                0,
                context.getString(R.string.service_resume),
                stopWatchHelper.resumePendingIntent()
            )
        }

        if (showCancel) {
            builder.addAction(
                0,
                context.getString(R.string.service_cancel),
                stopWatchHelper.cancelPendingIntent()
            )
        }

        if (showFinish) {
            builder.addAction(
                0,
                context.getString(R.string.service_finish),
                stopWatchHelper.finishPendingIntent()
            )
        }

        return builder
    }

}