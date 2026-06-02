package aeb.proyecto.stopwatch.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency Injection module leveraging Hilt to explicitly expose and scope
 * the global Application Context within the architecture.
 *
 * This structural configuration guarantees that long-running system background processes,
 * such as Foreground Services and WindowManager window overlays, can securely access a
 * non-leaking context binder across the entire app lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object ContextModule {

    /**
     * Explicitly provisions the global, lifecycle-safe Android Application Context
     * into the dependency injection dependency graph.
     *
     * @param context The framework-provisioned [@ApplicationContext] instance.
     * @return The absolute structural application [Context] wrapper.
     */
    @Provides
    @Singleton
    fun provideContext(
        @ApplicationContext context:Context
    ):Context {
        return context
    }

}