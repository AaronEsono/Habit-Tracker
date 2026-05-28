package aeb.proyecto.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Global application-scoped property extension hosting the underlying [DataStore] delegate.
 *
 * Enforcing the [preferencesDataStore] delegate syntax directly as a [Context] extension guarantees
 * reactive file-system serialization semantics. This architecture strictly pattern-prevents
 * concurrent multi-instance pointer initialization over the underlying physical "user_prefs.preferences_pb"
 * binary file, suppressing potential [androidx.datastore.core.CorruptionException] mutations.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

/**
 * Dependency Injection Architecture Module for local reactive key-value storage.
 *
 * This configuration boundary isolates, provisions, and exposes the stateless [DataStore] engine
 * across all internal feature modules. It hooks directly into Hilt's global [SingletonComponent]
 * to structuralize uniform data reads/writes.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
     * Provides a thread-safe, atomic, single-source-of-truth [DataStore] preferences instance.
     *
     * ### Lifecycle & Threading Topology:
     * * **Scope Boundary:** Bound to [@Singleton]. The underlying preference reference graph remains allocated
     * throughout the global application process lifetime framework, eliminating redundant I/O descriptors.
     * * **Context Injection:** Anchored via [@ApplicationContext] to secure the root process lifecycle context safely.
     * This explicitly prevents memory leak trajectories associated with short-lived `Activity` contexts.
     * * **Internal Coroutine Context:** DataStore natively handles thread-switching underneath, offloading internal
     * data serialization to [kotlinx.coroutines.Dispatchers.IO] automatically.
     *
     * @param context The root, process-bound structural application context payload.
     * @return An operational single-pointer instance of [DataStore] handling raw [Preferences].
     */
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}