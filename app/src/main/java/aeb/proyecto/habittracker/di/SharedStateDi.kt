package aeb.proyecto.habittracker.di

import aeb.proyecto.habittracker.utils.SharedState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedStateDi {

    @Provides
    @Singleton
    fun provideSharedState() = SharedState()
}