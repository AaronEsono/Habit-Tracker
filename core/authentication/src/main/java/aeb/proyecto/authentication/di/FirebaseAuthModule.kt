package aeb.proyecto.authentication.di

import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.authentication.AuthenticationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * A central dependency injection registry governing core identity verification and session authentication contracts.
 *
 * Configured via [SingletonComponent] to ensure the authentication state persists seamlessly across the entire
 * application lifecycle, this abstract class coordinates the decoupled mapping of the identity system.
 *
 * By enforcing an [internal] visibility scope on the binding mechanism, the underlying concrete authentication engine
 * (such as Firebase handlers) remains encapsulated inside the module boundaries. This loose coupling shields external
 * functional consumer features, allowing future implementation refactoring (e.g., migrating to asynchronous coroutine
 * suspension pipelines) without altering the dependency graph consumers.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseAuthModule {

    /**
     * Binds the internal [AuthenticationManager] workflow engine onto the public [AuthenticationInterface] contract.
     *
     * @param authenticationManager The concrete identity driver orchestrating credential processing.
     * @return A scoped [AuthenticationInterface] instance accessible throughout the global application architecture.
     */
    @Binds
    internal abstract fun bindAuthenticationInterface(
        authenticationManager: AuthenticationManager
    ): AuthenticationInterface
}