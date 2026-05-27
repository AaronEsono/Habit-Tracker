package aeb.proyecto.authentication.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A specialized dependency injection provider factory responsible for instantiating external
 * cloud authentication infrastructure.
 *
 * Configured as a high-performance static Kotlin [object] within the [SingletonComponent] scope,
 * this module initializes the core native identity subsystem. It acts as the single source of truth
 * for user session token lifecycle allocations across the application graph.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Provisions a thread-safe, application-wide instance of the native [FirebaseAuth] engine.
     *
     * This singleton instance maintains the active local user cache tokens and manages remote communication
     * sync pipelines with Google Cloud Identity services.
     *
     * @return The initialized [FirebaseAuth] core instance ready to anchor credential operations.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}