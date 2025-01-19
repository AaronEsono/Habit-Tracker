package aeb.proyecto.datastore.di

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.repository.DatastoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatastoreModule {

    @Binds
    internal abstract fun bindDatastoreInterface(
        datastoreRepository: DatastoreRepository
    ): DatastoreInterface

}