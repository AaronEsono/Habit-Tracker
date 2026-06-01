package aeb.proyecto.firestore.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    /**
     * Provides a centralized, thread-safe instance of [FirebaseFirestore] managed as a Singleton.
     *
     * This infrastructure component serves as the primary entry point for all remote
     * NoSQL database operations, collection references, and real-time synchronization pipelines.
     *
     * @return The global operational [FirebaseFirestore] database gateway.
     */
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

}