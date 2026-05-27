package aeb.proyecto.analytics.di

import aeb.proyecto.analytics.AnalyticsManager
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A central dependency injection provider factory responsible for instantiating external
 * telemetry infrastructure.
 *
 * Configured as a high-performance static Kotlin [object] within the [SingletonComponent] scope,
 * this module handles the creation and lifecycle management of foreign SDK instances that cannot
 * be natively annotated with constructor injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    /**
     * Provisions a thread-safe, application-wide instance of the native [FirebaseAnalytics] SDK.
     *
     * @param context The globally scoped [@ApplicationContext] required by the Firebase framework
     * to safely register system-level telemetry channels and device metadata handlers.
     * @return The initialized [FirebaseAnalytics] singleton core instance.
     */
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    /**
     * Provisions the concrete implementation of the [AnalyticsManager], anchoring it within
     * the global singleton scope.
     *
     * @param firebaseAnalytics The pre-provisioned native framework wrapper utilized as the underlying
     * data propagation engine.
     * @return A standalone, thread-safe [AnalyticsManager] instance ready to orchestrate event processing.
     */
    @Provides
    @Singleton
    fun provideAnalyticsManager(
        firebaseAnalytics: FirebaseAnalytics
    ): AnalyticsManager {
        return AnalyticsManager(firebaseAnalytics)
    }
}