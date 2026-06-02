package aeb.proyecto.stopwatch.notification

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.constants.NOTIFICATION_CHANNEL_ID
import aeb.proyecto.stopwatch.helper.StopWatchHelper
import android.content.Context
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * UI decorator helper operating as a mutation abstraction layer over the system notification architecture.
 *
 * This component decouples core tracking loop calculations from Android framework layout updates,
 * orchestrating real-time action injections, sub-text updates, and dynamic title rendering changes.
 *
 * @property context The systemic non-leaking application context framework link.
 * @property stopWatchHelper The intent broker providing tokenized actions for background operations.
 * @property notificationBuilder The underlying baseline framework notification stylist container.
 */
class NotificationBuilderHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stopWatchHelper: StopWatchHelper,
    private val notificationBuilder: NotificationCompat.Builder
) {

    /**
     * Initializes and prepares the baseline notification container structure.
     * Sets the active creation timestamp anchor to ensure chronological tracking parity inside the system curtain.
     *
     * @return The initialized [NotificationCompat.Builder] instance.
     */
    fun createNotification(): NotificationCompat.Builder {
        return notificationBuilder.setWhen(System.currentTimeMillis())
    }

    /**
     * Mutates, repaints, and pushes synchronized tracking metrics into the notification view template.
     * Clears internal operational action buffers on every tick to safeguard against visual duplicate artifacts.
     *
     * @param newTitle The main contextual heading text describing the ongoing status.
     * @param newTime The formatted real-time chronological string (e.g., "02:15:09").
     * @param subText Optional contextual subtitle detailing the linked database habit metadata profile.
     * @param showStop Conditional flag to inject the interactive systemic 'Pause/Stop' button action.
     * @param showResume Conditional flag to inject the interactive systemic 'Resume' button action.
     * @param showCancel Conditional flag to inject the destructive systemic 'Cancel/Reset' button action.
     * @param showFinish Conditional flag to inject the terminal systemic 'Finish' execution button action.
     * @return The updated [NotificationCompat.Builder] instance ready to be dispatched by the manager.
     */
    fun updateNotification(
        newTitle:String,
        newTime: String,
        subText: String? = null,
        showStop: Boolean = false,
        showResume: Boolean = false,
        showCancel: Boolean = false,
        showFinish: Boolean = false
    ): NotificationCompat.Builder {
        notificationBuilder.setContentTitle(newTitle)
        notificationBuilder.setContentText(newTime)
        notificationBuilder.clearActions() // Critical: Purges action array stacks to prevent OS duplication bugs

        // Manage contextual relationship sub-texts safely
        subText?.let {
            notificationBuilder.setSubText(subText)
        }?: run {
            notificationBuilder.setSubText(null) // Clears the canvas space for unlinked/global instruments
        }

        // Evaluate state flags and inject corresponding Pendings actions
        if (showStop) {
            notificationBuilder.addAction(
                NotificationCompat.Action.Builder(
                    0, // Icons omitted in modern Android standards to optimize notification line real estate
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