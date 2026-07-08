package aeb.proyecto.alarmmanager.di

import dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import java.time.Clock

/**
 * Dependency injection module responsible for provisioning time-bound infrastructure components.
 *
 * This module abstractly decouples the system's chronological anchor from the underlying hardware
 * clock. By injecting [Clock] instead of invoking static native primitives (e.g., `System.currentTimeMillis`),
 * the application boundary secures absolute determinism across business pipelines, enabling hermetic
 * test environments to programmatically mock or freeze timeline execution frames via static clock providers.
 */
@Module
@InstallIn(SingletonComponent::class)
class ClockModule {

    /**
     * Provides a singleton instance of the system [Clock] tied to the default device time-zone.
     *
     * @return A [Clock] tracking real-time hardware clock movements relative to the user's localized zone context.
     */
    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.systemDefaultZone()

}