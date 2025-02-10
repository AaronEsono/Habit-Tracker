package aeb.proyecto.authentication.di

import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.authentication.AuthenticationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseAuthModule {

    @Binds
    internal abstract fun bindAuthenticationInterface(
        authenticationManager: AuthenticationManager
    ): AuthenticationInterface
}