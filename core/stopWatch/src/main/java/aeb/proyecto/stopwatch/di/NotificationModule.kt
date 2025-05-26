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

@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

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

    @ServiceScoped
    @Provides
    fun provideNotificationBuilderHelper(
        @ApplicationContext context: Context,
        stopWatchHelper: StopWatchHelper,
        notificationBuilder: NotificationCompat.Builder
    ): NotificationBuilderHelper  {
        return NotificationBuilderHelper(context, stopWatchHelper, notificationBuilder)
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ):NotificationManager{
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}