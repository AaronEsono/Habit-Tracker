package aeb.proyecto.datastore.di

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.repository.DatastoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Architectural Binding Repository Module for local DataStore implementations.
 *
 * This configuration acts as the dependency inversion bridge, mapping the abstract domain
 * contracts directly to infrastructure-level serialization engines. By utilizing Hilt's
 * optimization interfaces, it ensures clean decoupling between consumers and file-system managers.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DatastoreRepositoryModule {

    /**
     * Binds the declarative preference interface contract to its concrete data repository engine.
     *
     * ### Compilation & Performance Topology:
     * * **Zero-Allocation Execution:** Using [@Binds] instead of [@Provides] instructs the Hilt
     * processor to skip creating redundant factory class wrappers or runtime invocation overhead,
     * casting the reference directly at the bytecode level.
     * * **Encapsulation Boundary:** Marked as [internal] to restrict concrete implementation leakage
     * across different application modules, explicitly honoring API exposition guidelines.
     * * **Lifecycle Persistence:** Inherits the parent [SingletonComponent] topology, making the
     * binding reference globally reusable and thread-safe.
     *
     * @param datastoreRepository The concrete infrastructure implementation handling raw preference nodes.
     * @return A scoped, abstract [DatastoreInterface] identity provider mapping.
     */
    @Binds
    internal abstract fun bindDatastoreInterface(
        datastoreRepository: DatastoreRepository
    ): DatastoreInterface

}