package aeb.proyecto.stopwatch.di

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.constants.NOTIFICATION_CHANNEL_ID
import aeb.proyecto.stopwatch.helper.StopWatchHelper
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

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        stopWatchHelper: StopWatchHelper
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Stopwatch")
            .setContentText("00:00:00")
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_achievement)
            .addAction(0,"Stop",stopWatchHelper.stopPendingIntent())
            .addAction(0,"Cancel",stopWatchHelper.cancelPendingIntent())
            .setContentIntent(stopWatchHelper.clickPendingIntent())
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ):NotificationManager{
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}