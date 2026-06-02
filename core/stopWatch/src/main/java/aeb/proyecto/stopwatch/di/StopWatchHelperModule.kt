package aeb.proyecto.stopwatch.di

import aeb.proyecto.stopwatch.helper.StopWatchHelper
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Singleton-scoped dependency injection module provisioning the primary system intent broker
 * and pending navigation routing components for the time tracking ecosystem.
 *
 * Placed within the [SingletonComponent] to ensure that deep-linking pathways, action callbacks,
 * and target pending tokens remain globally unified across both foreground presentation screens
 * and background system service boundaries.
 */
@Module
@InstallIn(SingletonComponent::class)
object StopWatchHelperModule {

    /**
     * Provisions a globally shared, lifecycle-safe instance of the intent orchestration layer.
     *
     * @param context The framework-provisioned, non-leaking [@ApplicationContext] link.
     * @return A concrete singleton instance of [StopWatchHelper].
     */
    @Provides
    @Singleton
    fun providesStopWatchHelper(
        @ApplicationContext context:Context
    ): StopWatchHelper {
        return StopWatchHelper(context)
    }

}