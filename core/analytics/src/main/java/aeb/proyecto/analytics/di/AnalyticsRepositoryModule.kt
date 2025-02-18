package aeb.proyecto.analytics.di

import aeb.proyecto.analytics.AnalyticsManager
import aeb.proyecto.analytics.AnalyticsManagerInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsRepositoryModule {

    @Binds
    internal abstract fun bindAnalyticsManager(
        analyticsManager: AnalyticsManager
    ): AnalyticsManagerInterface

}