package aeb.proyecto.stopwatch.di

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.constants.NOTIFICATION_CHANNEL_ID
import aeb.proyecto.stopwatch.helper.StopWatchHelper
import aeb.proyecto.stopwatch.notification.NotificationBuilderHelper
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

/**
 * Service-scoped dependency injection module provisioning notification builders,
 * local alert management abstractions, and Android OS notification channels.
 *
 * Placed within the [ServiceComponent] layer, these dependencies adhere to the exact
 * boundary lifecycles of the running background time-tracking infrastructure.
 */
@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    /**
     * Instantiates the core, immutable blueprint for the persistent Foreground Service alert.
     *
     * Configured with strict system constraints to support high-frequency update intervals
     * without generating disruptive sound or haptic feedback loops.
     *
     * @param context The systemic non-leaking application context framework link.
     * @param stopWatchHelper Local controller managing intent vectors for pending actions.
     * @return A pre-configured [NotificationCompat.Builder] instance.
     */
    @Provides
    @ServiceScoped
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        stopWatchHelper: StopWatchHelper
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_achievement)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOnlyAlertOnce(true)
            .setContentIntent(stopWatchHelper.clickPendingIntent())
    }

    /**
     * Provisions a high-level UI decorator helper tasked with mutating and pushing running timer
     * metrics to the active notification container layout.
     *
     * @param context The systemic non-leaking application context framework link.
     * @param stopWatchHelper Local controller managing intent vectors for pending actions.
     * @param notificationBuilder The low-level component container tracking baseline styling rules.
     * @return A concrete instance of [NotificationBuilderHelper].
     */
    @ServiceScoped
    @Provides
    fun provideNotificationBuilderHelper(
        @ApplicationContext context: Context,
        stopWatchHelper: StopWatchHelper,
        notificationBuilder: NotificationCompat.Builder
    ): NotificationBuilderHelper  {
        return NotificationBuilderHelper(context, stopWatchHelper, notificationBuilder)
    }

    /**
     * Resolves and binds the concrete hardware-level Android OS system Notification Service.
     *
     * @param context The systemic non-leaking application context framework link.
     * @return The platform-level [NotificationManager] broker.
     */
    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ):NotificationManager{
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}