package aeb.proyecto.stopwatch.di

import aeb.proyecto.stopwatch.helper.StopWatchHelper
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StopWatchHelperModule {

    @Provides
    @Singleton
    fun providesStopWatchHelper(
        @ApplicationContext context:Context
    ): StopWatchHelper {
        return StopWatchHelper(context)
    }

}