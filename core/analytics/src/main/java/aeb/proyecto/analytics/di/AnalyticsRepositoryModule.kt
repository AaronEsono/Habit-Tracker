package aeb.proyecto.analytics.di

import aeb.proyecto.analytics.AnalyticsManager
import aeb.proyecto.analytics.AnalyticsManagerInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * A central dependency injection registry governing core telemetry and analytic engine bindings.
 *
 * Configured via [SingletonComponent] to persist across the absolute application execution lifecycle,
 * this abstract class utilizes high-performance [@Binds] routing to map analytics interfaces to their
 * concrete internal processing managers.
 *
 * By maintaining an [internal] visibility scope on the binding mechanism, the underlying concrete
 * engine implementation remains safely encapsulated within the analytics module boundaries, exposing
 * only abstract contracts to external client features.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsRepositoryModule {

    /**
     * Binds the internal [AnalyticsManager] tracking implementation onto the public [AnalyticsManagerInterface] contract.
     *
     * @param analyticsManager The concrete tracking engine orchestrating event propagation.
     * @return A scoped [AnalyticsManagerInterface] handle provisioned across the global dependency graph.
     */
    @Binds
    internal abstract fun bindAnalyticsManager(
        analyticsManager: AnalyticsManager
    ): AnalyticsManagerInterface

}