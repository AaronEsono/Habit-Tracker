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

    @Binds
    internal abstract fun bindFirestoreManager(
        firestoreManager: FirestoreManager
    ): FirestoreInterface
}