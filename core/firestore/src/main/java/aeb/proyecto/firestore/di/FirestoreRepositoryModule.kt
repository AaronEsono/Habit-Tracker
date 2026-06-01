package aeb.proyecto.firestore.di

import aeb.proyecto.firestore.FirestoreInterface
import aeb.proyecto.firestore.FirestoreManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirestoreRepositoryModule {

    /**
     * Binds the internal infrastructure implementation of the Firestore manager
     * to its corresponding domain abstraction interface.
     *
     * This binding architecture ensures that dependent business layers decoupling
     * from direct cloud database operations remain highly testable via test doubles.
     *
     * @param firestoreManager The core concrete NoSQL operations orchestrator.
     * @return A thread-safe exposed reference of [FirestoreInterface].
     */
    @Binds
    internal abstract fun bindFirestoreManager(
        firestoreManager: FirestoreManager
    ): FirestoreInterface
}